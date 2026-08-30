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

Dois domínios existem hoje apenas **parcialmente**: `recepcionista` e `historicopaciente` têm entidade, DTO e repositório, mas ainda não têm mapper, service nem controller. As tabelas `recepcionista` e `historico_paciente` já fazem parte do schema criado por este serviço e são consumidas pela HistoricoAPI. Ao completar um desses domínios, siga a fatia vertical acima — a migration correspondente já existe.

### Fluxo de uma requisição

`Controller` → `Service` → (`Mapper` + `Repository`) → resposta. Controllers devolvem `ResponseEntity<MensagemSucessoResponse>` em escritas e `ResponseEntity<PageResponse<XDTO>>` em listagens. Exceções sobem até o `GlobalExceptionHandler`, que também implementa `AuthenticationEntryPoint` e `AccessDeniedHandler` — todo erro sai no formato `ErrorResponseDTO`.

### Convenções importantes

* **Idioma:** todo o código, mensagens de log, mensagens de erro e nomes de campo são em **português**. Mantenha esse padrão.
* **Records para DTO/Request**, classes com Lombok para entidades.
* **Sem Javadoc e sem comentários — em nenhum arquivo:** não há `/** ... */`, `//` ou `#` explicativos nas classes Java, no `build.gradle.kts`, nos arquivos Compose, no `Dockerfile`, no `application.yaml` nem nos arquivos de ignore. A documentação da API vive nas anotações do SpringDoc (`@Schema`, `@Operation`) e o restante do contexto neste arquivo e no `README.md`. **Não acrescente comentários ao criar ou alterar arquivos aqui** — se algo precisa de explicação, ela vai para a documentação.
* **Exclusão é lógica, não física:** `deletar(...)` troca a `situacaoCadastro` para `SituacaoCadastro.EXCLUIDO` dentro de um método `@Transactional`, contando com o dirty checking do JPA. Não use `repository.delete(...)`.
* **Atualizações usam `PATCH`** e passam por `mapper.updateEntity(request, entidade)` — o mapper ignora `id`, `usuario`, `dataCadastro` e `situacaoCadastro`. Os mappers de `medico`, `enfermeiro` e `paciente` declaram `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE`, então campo ausente no JSON **mantém** o valor atual em vez de gravar `null` — é o que dá ao `PATCH` semântica de atualização parcial. `AgendamentoService.atualizar(...)` não usa mapper e faz essa checagem de `null` campo a campo, na mão.
* **Paginação:** `@PageableDefault(size = 100, sort = "id")` com `one-indexed-parameters: true` (a página 1 é a primeira). Use sempre `PageResponse.from(page, DTO::new)`.
* **Senhas** passam por `BCryptPasswordEncoder` em `UsuarioService`; nunca persista texto puro nem exponha o hash em DTO.
* **Logging:** `@Slf4j` no service, com log de início e de conclusão da operação (`log.info("Salvando Paciente... - Nome: {}", ...)`).
* **Jackson está em modo estrito** (`fail-on-unknown-properties: true`): um campo extra no JSON derruba a requisição com 400. Ao adicionar campo a um request, atualize também os JSONs em `src/test/resources/`.
* **Datas:** `LocalDate` serializa como `dd/MM/yyyy`; `LocalDateTime` como `dd/MM/yyyy - HH:mm:ss`, sempre via `@JsonFormat`.

## Banco de Dados

O `DataSource` **não** é montado pelo `spring.datasource` do Spring Boot: `config/DataBaseConfig.java` (perfis `dev` e `prod`) monta a URL a partir das variáveis `DATABASE_IP`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER` e `DATABASE_PASSWORD`. Sem essas variáveis a aplicação não sobe. O perfil `test` usa `config/TestDataBaseConfig.java`, que traz defaults apontando para `localhost:8745`.

Migrações Flyway ficam em `src/main/resources/db/migration/`, no padrão `V<versão>__<Descrição>.sql`:

* `V1.0__CreateTables.sql` — esquema completo (`tipo_usuario`, `situacao_cadastro`, `usuario`, `medico`, `enfermeiro`, `recepcionista`, `paciente`, `historico_paciente`, `agendamento`)
* `V1.1__Inserts.sql` — dados de domínio + seed de usuários, médicos, enfermeiros e pacientes

**Nunca edite uma migração já aplicada** — crie uma nova versão. Ao alterar uma entidade JPA, a migração correspondente é obrigatória: `ddl-auto` não está habilitado, o esquema vem inteiro do Flyway.

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

`SecurityConfig` deixa públicos apenas:

* `POST /v1/auth/login`, `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`
* `POST` em `/v1/medico`, `/v1/paciente`, `/v1/enfermeiro` (autocadastro)

Todo o resto exige `Authorization: Bearer <token>`. A sessão é `STATELESS` e o CSRF está desabilitado. O `SecurityFilter` valida o token e popula o `SecurityContext`; token inválido apenas gera um `log.warn` e limpa o contexto — quem devolve o 401 é o `GlobalExceptionHandler`.

`JWT_SECRET` e `JWT_EXPIRATION_MS` têm defaults embutidos em `TokenService` para desenvolvimento. **Em produção sempre defina `JWT_SECRET` via ambiente** — o default está no código-fonte e, portanto, é público.

## Testes

Os testes são de **integração de verdade: exigem um PostgreSQL acessível**. Não há Testcontainers nem banco em memória. Suba `docker-compose-postgres-dev.yml` antes de rodar `./gradlew test`, ou os testes falham já na inicialização do contexto. Os defaults de `TestDataBaseConfig` (`localhost:8745`, `postgres` / `fiap@2026`) são exatamente as credenciais desse compose — se alterar um, altere o outro.

* `AbstractTest` — base dos testes de service: `@Transactional`, perfil `test`, importa `TestDataBaseConfig`.
* `AbstractControllerTest` — base dos testes de controller: `MockMvc` + `@WithMockUser` (a segurança é contornada, então os testes não cobrem o fluxo real de JWT) e helpers `testGet`/`testPost`/`testPatch`/`testDelete`.
* Payloads de controller vêm de arquivos JSON em `src/test/resources/<dominio>/`, lidos por caminho relativo — **rode os testes a partir da raiz do projeto**.
* Os testes de service assertam principalmente `assertDoesNotThrow`; ao adicionar um campo a um record de request, os construtores posicionais nos testes quebram e precisam ser atualizados junto.
* JaCoCo exclui da cobertura `config/`, `enums/`, `exceptions/`, `model/` e a classe principal. Relatório HTML em `build/reports/jacoco/test/html/index.html`.

## Pontos de Atenção Conhecidos

* **`TipoUsuario` e `SituacaoCadastro` carregam IDs fixos:** os enums em `enums/` replicam, no código, os ids gerados pela ordem de inserção das tabelas de domínio `tipo_usuario` e `situacao_cadastro` em `V1.1__Inserts.sql` (`ADMINISTRADOR` 1, `MEDICO` 2, `ENFERMEIRO` 3, `RECEPCIONISTA` 4, `PACIENTE` 5). Os services gravam esses ids diretamente (`TipoUsuario.PACIENTE.getId()`), então **qualquer alteração no seed dessas tabelas exige alterar o enum na mesma mudança** — uma divergência aqui não quebra a compilação nem os testes, apenas grava o tipo errado silenciosamente. Inserir um tipo novo no meio da lista desloca todos os ids seguintes; prefira sempre acrescentar no fim.
* **Nome do jar no `Dockerfile`:** o artefato é copiado como `RestauranteAPI.jar` — resquício de outro projeto. Funciona, mas é enganoso.
* **Context path é case-sensitive:** é `/AgendamentoAPI`, não `/agendamentoapi`.
* **Credenciais de banco em arquivos versionáveis:** `TestDataBaseConfig` e os arquivos `.run/` carregam senhas como valor default/literal. Não propague esse padrão e nunca acrescente segredos novos ao código.
* `UsuarioService.validarSenha(...)` está declarado, mas não é usado (a validação real acontece em `AuthService`).

## Convenção de Commits

Commits seguem o identificador da tarefa do board (ex.: `ADJ-5`), e o merge para `main` é feito por Pull Request — o workflow `.github/workflows/workflow.yml` roda `./gradlew build` em todo PR contra `main`.
