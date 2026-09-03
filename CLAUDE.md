# CLAUDE.md

Orientações para o Claude Code trabalhar neste repositório.

## Visão Geral

**AgendamentoAPI** — API REST de gerenciamento de consultas médicas (agendamento de consultas, cadastro de médicos, enfermeiros e pacientes, autenticação JWT), além de ser a dona do schema compartilhado da fase (incluindo `recepcionista` e `historico_paciente`). Projeto acadêmico da pós-graduação FIAP (turma 12ADJT), pensado para evoluir como parte de um ecossistema de microsserviços hospitalares.

* **Java 21** (toolchain configurada no Gradle)
* **Spring Boot 4.0.5** + Gradle Kotlin DSL (`build.gradle.kts`)
* **PostgreSQL 18** com migrações **Flyway**
* **Spring Security** + **JWT** (jjwt 0.12.6)
* **MapStruct 1.6.3** para mapeamento entity ↔ request
* **Lombok**, **Log4j2** (Logback é excluído globalmente), **SpringDoc OpenAPI 3.0.2**
* **JaCoCo** para cobertura de testes

Context path da aplicação: **`/AgendamentoAPI`** (definido em `application.yaml`).

## Comandos

```bash
# Build completo (compila + roda testes + gera relatório JaCoCo)
./gradlew build

# Build sem testes
./gradlew clean build -x test

# Somente os testes (exige PostgreSQL no ar — ver seção Testes)
./gradlew test

# Executar em desenvolvimento (porta 9017)
./gradlew bootRun --args="--spring.profiles.active=dev"

# Executar em produção local (porta 9027)
./gradlew bootRun --args="--spring.profiles.active=prod"

# Subir apenas o PostgreSQL de desenvolvimento (host 8745 → container 5432)
docker compose -f docker-compose-postgres-dev.yml up -d --wait

> Na branch `ADJ-8` o único compose de banco é `docker-compose-postgres.yml` (host 8745 → container 5432); os dois arquivos `-dev`/`-prod` vêm da `ADJ-7`. Depois do merge, use os nomes acima.

# Subir em modo produção (requer .env preenchido) — banco primeiro, depois a API
docker compose -f docker-compose-postgres-prod.yml up -d --wait
docker compose -f docker-compose-agendamentoapi.yml up -d
```

> No Windows/PowerShell use `.\gradlew.bat` no lugar de `./gradlew`.

O IntelliJ tem configurações prontas em `.run/`: `BootRun - DEV`, `BootRun - PROD`, `Clean Build - [Without Tests]` e `Testes de Integração`. Esses arquivos carregam as variáveis de ambiente do banco — se você alterar credenciais locais, ajuste-os também. **`.run/` está no `.gitignore`**, então mudanças ali não são versionadas.

## Arquitetura

O pacote raiz é `br.com.fiap.agendamentoapi`. A organização é **por camada e depois por domínio** — cada domínio (`paciente`, `medico`, `enfermeiro`, `recepcionista`, `agendamento`, `historicopaciente`, `usuario`, `auth`, `tipousuario`, `situacaocadastro`) ganha um subpacote dentro de cada camada:

```
controller/<dominio>/       → REST, sem regra de negócio; só delega ao service
service/<dominio>/          → regra de negócio, @Transactional, logging
repository/<dominio>/       → interfaces JpaRepository
model/entity/<dominio>/     → entidades JPA (classes com @Getter/@Setter do Lombok)
model/dto/<dominio>/        → records de saída; construtor que recebe a entidade
model/request/<dominio>/    → records de entrada (Salvar…Request / Atualizar…Request)
model/mapper/<dominio>/     → interfaces MapStruct (componentModel = "spring")
model/response/             → PageResponse, MensagemSucessoResponse, TokenResponse
exceptions/                 → exceções de negócio + GlobalExceptionHandler
config/                     → DataBaseConfig, SecurityConfig, SwaggerConfig
enums/                      → TipoUsuario, SituacaoCadastro (espelham tabelas de domínio)
```

**Ao criar um novo domínio, replique essa fatia vertical completa** (entity → repository → mapper → request/DTO → service → controller → migration → testes de service e de controller).

Todos os domínios listados acima possuem a fatia vertical completa. `recepcionista` e `historicopaciente` foram os últimos a ser concluídos: até então tinham apenas entidade, DTO e repositório. As tabelas `recepcionista` e `historico_paciente` fazem parte do schema criado por este serviço e são consumidas pela HistoricoAPI.

### Fluxo de uma requisição

`Controller` → `Service` → (`Mapper` + `Repository`) → resposta. Controllers devolvem `ResponseEntity<MensagemSucessoResponse>` em escritas e `ResponseEntity<PageResponse<XDTO>>` em listagens. Exceções sobem até o `GlobalExceptionHandler`, que também implementa `AuthenticationEntryPoint` e `AccessDeniedHandler` — todo erro sai no formato `ErrorResponseDTO`.

### Convenções importantes

* **Idioma:** todo o código, mensagens de log, mensagens de erro e nomes de campo são em **português**. Mantenha esse padrão.
* **Records para DTO/Request**, classes com Lombok para entidades.
* **Sem Javadoc e sem comentários — em nenhum arquivo:** não há `/** ... */`, `//` ou `#` explicativos nas classes Java, no `build.gradle.kts`, nos arquivos Compose, no `Dockerfile`, no `application.yaml` nem nos arquivos de ignore. A documentação da API vive nas anotações do SpringDoc (`@Schema`, `@Operation`) e o restante do contexto neste arquivo e no `README.md`. **Não acrescente comentários ao criar ou alterar arquivos aqui** — se algo precisa de explicação, ela vai para a documentação.
* **Exclusão é lógica, não física:** `deletar(...)` troca a `situacaoCadastro` para `SituacaoCadastro.EXCLUIDO` dentro de um método `@Transactional`, contando com o dirty checking do JPA. Não use `repository.delete(...)`. **A exclusão também desativa as credenciais**: o `deletar(...)` de `medico`, `enfermeiro`, `recepcionista` e `paciente` chama `usuarioService.desativar(entidade.getUsuario())`, que marca a coluna `id_situacaocadastro` da tabela `usuario` como `EXCLUIDO`. A partir daí o login responde `403 Usuário Inativo!` e um token emitido antes da exclusão é recusado com `401`. As exceções são `HistoricoPacienteService.deletar(...)` e `AgendamentoService.cancelar(...)`, que apagam o registro de verdade porque `historico_paciente` e `agendamento` não têm coluna de situação.
* **Atualizações usam `PATCH`** e passam por `mapper.updateEntity(request, entidade)` — o mapper ignora `id`, `usuario`, `dataCadastro` e `situacaoCadastro`. Todos os mappers de atualização declaram `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE` e um método `preenchido(String)` anotado com `@Condition`: campo ausente no JSON, `null`, string vazia ou só com espaços **mantêm** o valor atual em vez de gravar `null` ou apagar o conteúdo. É isso que dá ao `PATCH` a semântica de atualização parcial. `AgendamentoService.atualizar(...)` é a única exceção: não usa mapper e faz a checagem de `null` campo a campo, na mão.
* **Nenhum record `Atualizar…Request` tem campo obrigatório.** Como o `PATCH` é parcial, `@NotBlank` e `@NotNull` não aparecem nesses records — só nos `Salvar…Request`. O que permanece nos records de atualização são as validações de **formato**, que valem apenas quando o campo é enviado preenchido (`@Size` de `cpf`, `telefone`, `email` e `queixaPrincipal`; `@Email` de `email`). Ao criar um novo domínio, siga esse padrão e descreva a semântica no `@Schema` do record ("Os campos são opcionais: os que forem omitidos mantêm o valor atual.").
* **Agenda do médico:** `AgendamentoService` valida, no `salvar(...)` e no `atualizar(...)`, se o médico já tem consulta dentro de um **intervalo mínimo de 1 hora** (`INTERVALO_MINIMO_ENTRE_CONSULTAS`) do horário pedido. O intervalo é aberto nas pontas — uma consulta exatamente 1 hora antes ou depois é permitida, o que preserva a agenda de hora em hora do seed da `V1.1`. Na atualização, a própria consulta é excluída da busca pelo `IdNot`. Conflito lança `MedicoIndisponivelException`, que o `GlobalExceptionHandler` traduz em `409`. Isso complementa a constraint `uk_agendamento_medico_horario`, que continua barrando o horário exatamente igual no banco.
* **Paginação:** `@PageableDefault(size = 100, sort = "id")` com `one-indexed-parameters: true` (a página 1 é a primeira). Use sempre `PageResponse.from(page, DTO::new)`.
* **Senhas** passam por `BCryptPasswordEncoder` em `UsuarioService`; nunca persista texto puro nem exponha o hash em DTO.
* **Logging:** `@Slf4j` no service, com log de início e de conclusão da operação (`log.info("Salvando Paciente... - Nome: {}", ...)`).
* **Jackson está em modo estrito** (`fail-on-unknown-properties: true`): um campo extra no JSON derruba a requisição com 400. Ao adicionar campo a um request, atualize também os JSONs em `src/test/resources/`.
* **Datas:** `LocalDate` serializa como `dd/MM/yyyy`; `LocalDateTime` como `dd/MM/yyyy - HH:mm:ss`, sempre via `@JsonFormat`.
* **Conflito de horário no agendamento:** `AgendamentoService.salvar(...)` recusa marcar uma consulta se o **médico** ou o **paciente** já tiver outra no mesmo `dataHoraConsulta` — `AgendamentoRepository.existsByMedicoIdAndDataHoraConsulta` / `existsByPacienteIdAndDataHoraConsulta`, lançando `HorarioConsultaIndisponivelException` → `409`. Por isso `dataHoraConsulta` é `@NotNull` no `SalvarAgendamentoRequest` (exceção à regra de que só `Salvar…Request` de cadastro tem campo obrigatório). O `PATCH` (remarcar) ainda **não** revalida o horário.

## Banco de Dados

O `DataSource` **não** é montado pelo `spring.datasource` do Spring Boot: `config/DataBaseConfig.java` (perfis `dev` e `prod`) monta a URL a partir das variáveis `DATABASE_IP`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER` e `DATABASE_PASSWORD`. Sem essas variáveis a aplicação não sobe. O perfil `test` usa `config/TestDataBaseConfig.java`, que traz defaults apontando para `localhost:8745`.

Migrações Flyway ficam em `src/main/resources/db/migration/`, no padrão `V<versão>__<Descrição>.sql`:

* `V1.0__CreateTables.sql` — esquema completo (`tipo_usuario`, `situacao_cadastro`, `usuario`, `medico`, `enfermeiro`, `recepcionista`, `paciente`, `historico_paciente`, `agendamento`). A tabela `usuario` carrega `id_situacaocadastro` (default `ATIVO`), que é o que permite desativar o login de um cadastro excluído.
* `V1.1__Inserts.sql` — dados de domínio + seed de usuários, médicos, enfermeiros e pacientes

**Enquanto o projeto estiver em desenvolvimento, alterações de schema entram nas migrações que já existem** — coluna nova vai direto para o `CREATE TABLE` correspondente na `V1.0`, dado de apoio vai para a `V1.1`. Não crie uma versão nova para isso. A consequência é que o checksum muda e o Flyway passa a recusar a validação contra um banco que já tinha a versão antiga aplicada (`FlywayValidateException: Validate failed`); o caminho esperado é recriar o schema do zero, o que também serve de verificação de que as migrações continuam funcionando desde o início. Confirme antes de apagar o banco de alguém. Quando houver dados reais em jogo, essa regra se inverte: aí uma migração nova é a única opção. Em qualquer cenário, ao alterar uma entidade JPA a migração correspondente é obrigatória: `ddl-auto` não está habilitado, o esquema vem inteiro do Flyway.

### Banco compartilhado entre os microsserviços

O PostgreSQL é **um só para toda a Fase 3**, e este projeto é o **dono do schema**: as migrations daqui criam todas as tabelas e a carga inicial que os demais serviços consomem. A `HistoricoAPI`, por exemplo, lê `paciente`, `historico_paciente` e `agendamento` sem ter Flyway nem `ddl-auto` — ela depende inteiramente destas migrations.

Consequências práticas:

* **Uma alteração de schema aqui pode quebrar outro serviço.** Antes de renomear ou remover coluna das tabelas compartilhadas, verifique as entidades dos projetos irmãos no diretório pai.
* **Este serviço sobe primeiro** contra um banco novo, para que o schema exista antes dos demais.
* Migration solicitada por outro serviço da fase é escrita **aqui**, não lá.

A topologia Docker é: `docker-compose-postgres-prod.yml` cria o banco e a rede `shared-net`; cada serviço tem um compose só com a própria aplicação, declarando `shared-net` como rede **externa**. Dentro da rede o banco é `postgres:5432`; a porta `8745` é apenas exposição no host. Os dois compose de PostgreSQL (`docker-compose-postgres-prod.yml` e `docker-compose-postgres-dev.yml`) são **idênticos aos da HistoricoAPI**, com `name:` de projeto e de volume fixos — assim o banco é o mesmo independentemente do repositório de onde for iniciado. **Ao alterar um deles, replique no repositório irmão.**

Um único conjunto de variáveis (`DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASSWORD`) cria o container e conecta os serviços, então as credenciais não têm como divergir. `DATABASE_PORT` não é lido pelos compose: dentro da rede é sempre `5432`.

Todas as APIs da fase escutam em `9027` dentro do container no perfil `prod`, então se diferenciam pela porta publicada no host: AgendamentoAPI em `9027`, HistoricoAPI em `9028`. Ao adicionar um serviço, escolha a próxima porta livre no host e mantenha `9027` do lado do container.

## Segurança

`SecurityConfig` deixa públicos apenas `POST /v1/auth/login`, `/v3/api-docs/**`, `/swagger-ui/**` e `/swagger-ui.html`. **Não há autocadastro:** todo cadastro exige token. As regras abaixo são declaradas por método HTTP no `SecurityConfig`; o que não casa com nenhuma regra cai em `anyRequest().authenticated()` (qualquer usuário logado).

**Cadastros** — a role `PACIENTE` não lê nem gerencia cadastro nenhum (só enxerga as próprias consultas); o `PERFIS_NAO_PACIENTE` são os outros quatro perfis:

* **Médico, enfermeiro e recepcionista** (matcher `GESTAO_PESSOAL`): `POST`/`PATCH`/`DELETE` → **só `ADMINISTRADOR`**; `GET` (listar) → `PERFIS_NAO_PACIENTE`.
* **Paciente** (matcher `PACIENTE_CADASTRO`): `POST` → `RECEPCIONISTA` (o admin **não** cria paciente — divisão estrita); `DELETE` → **só `ADMINISTRADOR`**; `GET`/`PATCH` → `PERFIS_NAO_PACIENTE`.

**Histórico do paciente (`/v1/historico-paciente`)** — o `ADMINISTRADOR` fica de fora, exceto para excluir:

* `POST`, `PATCH` → `MEDICO` ou `RECEPCIONISTA`.
* `GET` → `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO`.
* `DELETE` → `ADMINISTRADOR`.

**Agendamento / consultas (`/v1/agendamento`)** — o `ADMINISTRADOR` não acessa:

* `POST` (marcar) → `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO`.
* `PATCH` (remarcar) e `DELETE` (cancelar) → **só `RECEPCIONISTA`**. `AgendamentoService.cancelar(...)` apaga o registro de verdade (`agendamentoRepository.delete(...)`) — `agendamento` não tem coluna de situação, então a exclusão é física, como em `historico_paciente`.
* `GET` (ver consultas) → `MEDICO`, `RECEPCIONISTA`, `ENFERMEIRO` ou `PACIENTE`. É o **único** endpoint que a role `PACIENTE` acessa. **Quando quem chama tem a role `PACIENTE`, o `GET` devolve só as consultas do próprio paciente** — `AgendamentoService.getAgendamentos(...)` recebe o `Authentication`, e se a role for `PACIENTE` resolve o `paciente` por `pacienteRepository.findByUsuarioLogin(authentication.getName())` e filtra por `agendamentoRepository.findByPacienteId(...)`; login de paciente sem cadastro correspondente vê lista vazia. Os demais papéis veem todas.

A sessão é `STATELESS` e o CSRF está desabilitado.

A role vem do `tipoUsuario` do usuário, prefixada com `ROLE_` em `UsuarioDetailsImpl`, e é comparada com `hasRole(...)` no `SecurityConfig`. O `SecurityFilter` valida o token, carrega o `UserDetails` e **só popula o `SecurityContext` se `isEnabled()` for verdadeiro** — token inválido, expirado ou pertencente a um usuário excluído apenas gera um `log.warn` e limpa o contexto; quem devolve o `401` é o `GlobalExceptionHandler`, que também responde `403 Acesso Negado!` quando a role não basta.

`JWT_SECRET` e `JWT_EXPIRATION_MS` têm defaults embutidos em `TokenService` para desenvolvimento. **Em produção sempre defina `JWT_SECRET` via ambiente** — o default está no código-fonte e, portanto, é público.

## Testes

Os testes são de **integração de verdade: exigem um PostgreSQL acessível**. Não há Testcontainers nem banco em memória. Suba o compose do PostgreSQL antes de rodar `./gradlew test`, ou os testes falham já na inicialização do contexto. Os defaults de `TestDataBaseConfig` (`localhost:8745`, `postgres` / `fiap@2026`) são exatamente as credenciais desse compose — se alterar um, altere o outro. Use `./gradlew cleanTest test` para forçar a reexecução quando nada tiver mudado.

* `AbstractTest` — base dos testes de service: `@Transactional`, perfil `test`, importa `TestDataBaseConfig`.
* `AbstractControllerTest` — base dos testes de controller: `MockMvc` + `@WithMockUser` e helpers `testGet`/`testPost`/`testPatch`/`testDelete`. O `@WithMockUser` da classe base autentica com uma role genérica; testes de domínios restritos sobrescrevem a role na classe ou no método. Hoje: `MedicoControllerTest`, `EnfermeiroControllerTest` e `RecepcionistaControllerTest` rodam como `ADMINISTRADOR` (é quem cria/edita/exclui esses três); `PacienteControllerTest`, `AgendamentoControllerTest` e `HistoricoPacienteControllerTest` como `RECEPCIONISTA`; o `deletarTest` de `HistoricoPacienteControllerTest` sobrescreve para `ADMINISTRADOR`.
* `security/` — testes de autenticação e autorização, que **não** contornam a segurança: `AutorizacaoAdministradorTest` cobre a gestão de `/v1/medico`, `/v1/enfermeiro` e `/v1/recepcionista` (listar para todos menos `PACIENTE`; criar/editar/excluir só admin) e o `DELETE /v1/paciente/{id}` (só admin), `AutorizacaoLeituraCadastrosTest` cobre quem lista médicos/enfermeiros/pacientes/recepcionistas (todos menos `PACIENTE`) e trava a role `PACIENTE` fora de tudo que não seja `GET /v1/agendamento`, `AutorizacaoCadastroTest` cobre as regras de criação de cadastro por perfil, `AutorizacaoConsultaHistoricoTest` percorre a matriz de papéis contra `/v1/historico-paciente` e `/v1/agendamento`, e `SecurityFilterTest` exercita o fluxo real de JWT, incluindo token de usuário excluído. Padrão dos testes de autorização: `POST`/`PATCH` com corpo `{}` devolve `400` quando a role passa e `403` quando não passa — é assim que se distingue autorização de validação. Classes de teste comuns não moram em `config/`, que é reservado à infraestrutura da suíte.
* Payloads de controller vêm de arquivos JSON em `src/test/resources/<dominio>/`, lidos por caminho relativo — **rode os testes a partir da raiz do projeto**.
* Os testes de service assertam principalmente `assertDoesNotThrow`; ao adicionar um campo a um record de request, os construtores posicionais nos testes quebram e precisam ser atualizados junto.
* A semântica parcial do `PATCH` é travada por teste em cada domínio: no service, um `atualizarMantemCamposOmitidos…Test` envia `null` e strings em branco e confere que os valores originais permaneceram; no controller, um `atualizarParcialTest` envia o payload reduzido `atualizar<Dominio>ParcialRequest.json` e espera `200`. Replique os dois ao criar um domínio novo.
* JaCoCo exclui da cobertura `config/`, `enums/`, `exceptions/`, `model/` e a classe principal — o escopo medido é `controller`, `service` e `repository`, hoje em **100%** de instruções e de branches. Relatório HTML em `build/reports/jacoco/test/html/index.html`. Ao adicionar um caminho de erro (um `orElseThrow`, por exemplo), acrescente o teste do caso negativo junto, ou a cobertura cai.

## Pontos de Atenção Conhecidos

* **`TipoUsuario` e `SituacaoCadastro` carregam IDs fixos:** os enums em `enums/` replicam, no código, os ids gerados pela ordem de inserção das tabelas de domínio `tipo_usuario` e `situacao_cadastro` em `V1.1__Inserts.sql` (`ADMINISTRADOR` 1, `MEDICO` 2, `ENFERMEIRO` 3, `RECEPCIONISTA` 4, `PACIENTE` 5). Os services gravam esses ids diretamente (`TipoUsuario.PACIENTE.getId()`), então **qualquer alteração no seed dessas tabelas exige alterar o enum na mesma mudança** — uma divergência aqui não quebra a compilação nem os testes, apenas grava o tipo errado silenciosamente. Inserir um tipo novo no meio da lista desloca todos os ids seguintes; prefira sempre acrescentar no fim.
* **Nome do jar no `Dockerfile`:** o artefato é copiado como `RestauranteAPI.jar` — resquício de outro projeto. Funciona, mas é enganoso.
* **Context path é case-sensitive:** é `/AgendamentoAPI`, não `/agendamentoapi`.
* **Credenciais de banco em arquivos versionáveis:** `TestDataBaseConfig` e os arquivos `.run/` carregam senhas como valor default/literal. Não propague esse padrão e nunca acrescente segredos novos ao código.

## Convenção de Commits

Commits seguem o identificador da tarefa do board (ex.: `ADJ-5`), e o merge para `main` é feito por Pull Request — o workflow `.github/workflows/workflow.yml` roda `./gradlew build` em todo PR contra `main`.
