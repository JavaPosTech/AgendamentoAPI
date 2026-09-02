<div align="center"> <br> 
  <img align="center" alt="guru-java" height="150" width="150" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg" />
</div> 

<br> 

<div align="center">
    Turma 12ADJT – Projeto desenvolvido na pós-graduação em Arquitetura e Desenvolvimento em Java da FIAP. O objetivo é desenvolver um sistema hospitalar baseado em microsserviços, com serviços independentes para o gerenciamento de agendamentos e envio de notificações, utilizando Spring Security para autenticação e autorização e comunicação assíncrona entre os serviços.
</div> 

 <br> <br> 

## 🧰 Ferramentas Utilizadas

* ☕️ Java 21

* 🦅 Flyway

* 📝 Log4j2

* 🐘 PostgreSQL 18

* 🧪 JUnit 5 + JaCoCo

* 🟢 Spring Boot 4.0.5

* 🔄 MapStruct + Lombok

* 🔐 Spring Security + JWT

* 🛠️ Gradle 9.7 (Kotlin DSL)

* 🐳 Docker / Docker Compose

* 📑 SpringDoc OpenAPI (Swagger UI)

<br> 

## 📁 Estrutura do Projeto

O código é organizado **por camada e, dentro de cada camada, por domínio** (`paciente`, `medico`, `enfermeiro`, `recepcionista`, `agendamento`, `historicopaciente`, `usuario`, `auth`):

```
src/main/java/br/com/fiap/agendamentoapi/
├── config/           # DataBaseConfig, SecurityConfig, SwaggerConfig e SecurityFilter
├── controller/       # Endpoints REST
├── service/          # Regras de negócio
├── repository/       # Interfaces JpaRepository
├── model/
│   ├── entity/       # Entidades JPA
│   ├── dto/          # Modelos de saída
│   ├── request/      # Modelos de entrada
│   ├── mapper/       # Mapeamentos MapStruct
│   └── response/     # PageResponse, MensagemSucessoResponse, TokenResponse
├── exceptions/       # Exceções de negócio e GlobalExceptionHandler
└── enums/            # TipoUsuario, SituacaoCadastro

src/main/resources/
├── application.yaml              # Perfis dev, prod e test
├── log4j2.xml                    # Console em dev; arquivo rotativo em prod
└── db/migration/                 # Migrações Flyway (V1.0, V1.1, ...)
```

O esquema do banco é criado **exclusivamente pelo Flyway** — não há `ddl-auto`. Toda alteração de entidade exige a alteração da migração correspondente: enquanto o projeto está em desenvolvimento, coluna nova entra direto no `CREATE TABLE` da `V1.0` e dado de apoio na `V1.1`, em vez de ganhar uma versão nova. Como isso muda o checksum, o caminho esperado é recriar o schema do zero.

> ℹ️ Todos os domínios possuem a fatia vertical completa (entidade, repositório, mapper, request/DTO, service e controller). As tabelas `recepcionista` e `historico_paciente` fazem parte do schema da fase e também são consumidas pela HistoricoAPI.

> ℹ️ O projeto **não utiliza Javadoc nem comentários explicativos** — nem no código Java, nem nos arquivos de build e de infraestrutura (`build.gradle.kts`, Compose, `Dockerfile`). A documentação dos modelos e das rotas fica nas anotações do SpringDoc (`@Schema`, `@Operation`), publicadas no Swagger UI, e todo o contexto de arquitetura, execução e infraestrutura neste `README.md`.

<br> 

## ⚙️ Configurações Disponíveis

🔹 `BootRun - DEV`, executa a API no perfil de desenvolvimento, ideal para desenvolvimento local, criação de novas funcionalidades e realização de testes durante a implementação.

<br> 

🔹 `BootRun - PROD`, executa a API utilizando o perfil de produção localmente, permitindo simular o comportamento da aplicação em ambiente de produção.

<br> 

🔹 `Clean Build - [Without Tests]`, realiza o processo de build da aplicação sem executar os testes automatizados, limpando arquivos anteriores e recompilando o projeto de forma mais rápida. 

<br> 

🔹 `Testes de Integração`, executa toda a suíte de testes automatizados do projeto.

<br> 

Caso prefira o terminal, os mesmos comandos estão disponíveis via Gradle Wrapper:

```bash
# Build completo (com testes e relatório de cobertura)
./gradlew build

# Build sem testes
./gradlew clean build -x test

# Executar a suíte de testes
./gradlew test

# Executar a API no perfil desejado
./gradlew bootRun --args="--spring.profiles.active=dev"
```

> ℹ️ No Windows, utilize `.\gradlew.bat` no lugar de `./gradlew`.

<br> 

## 🐳 Banco Compartilhado e Docker Compose

Os microsserviços da Fase 3 compartilham **um único PostgreSQL**. O banco sobe de forma independente, cria a rede `shared-net`, e cada serviço se conecta a ela como rede externa:

```
                       ┌──────────────────────┐
                       │   rede: shared-net   │
                       │                      │
   host:8745  ────────▶│  postgres:5432       │◀──── AgendamentoAPI  (host:9027)
                       │                      │◀──── HistoricoAPI    (host:9028)
                       └──────────────────────┘
```

Dentro da rede, o banco é sempre alcançado pelo hostname **`postgres`** na porta interna **`5432`**. A porta `8745` é apenas a exposição no host, para acesso via IDE ou cliente SQL.

O projeto disponibiliza três arquivos Compose:

| Arquivo | Finalidade |
| --- | --- |
| `docker-compose-postgres-dev.yml` | PostgreSQL de desenvolvimento, com credenciais fixas e sem dependência do `.env`. |
| `docker-compose-postgres-prod.yml` | PostgreSQL de produção: lê o `.env`, possui *healthcheck* e cria a rede `shared-net`. |
| `docker-compose-agendamentoapi.yml` | Apenas a API, no perfil `prod`, conectando-se à `shared-net` já existente. |

> ℹ️ Os dois arquivos do PostgreSQL são **idênticos aos da HistoricoAPI** e utilizam nome de projeto e de volume fixos. Isso significa que tanto faz de qual projeto o banco é iniciado: o container e os dados serão sempre os mesmos. Suba o banco **uma vez**, a partir de qualquer um dos repositórios.

> ⭐ **Este é o serviço dono do schema.** As migrations Flyway em `src/main/resources/db/migration/` criam todas as tabelas da fase e inserem a carga inicial de dados. Os demais microsserviços apenas consomem esse schema, portanto a AgendamentoAPI deve ser a **primeira** a subir contra um banco novo.

<br> 

## 🛠️ Desenvolvimento 

Para o ambiente de desenvolvimento, o projeto disponibiliza o arquivo `docker-compose-postgres-dev.yml`, já configurado com todas as credenciais necessárias para conexão com o banco de dados, sem exigir nenhuma configuração adicional.

Para iniciar o serviço do PostgreSQL, execute no terminal: 

```bash
docker compose -f docker-compose-postgres-dev.yml up -d --wait
```

Em seguida, execute a aplicação utilizando a opção `BootRun - DEV`. Dessa forma, a API será conectada automaticamente ao banco de dados configurado no Docker Compose, facilitando a execução do projeto em ambiente local e ficando disponível na porta `9017`.

Na primeira execução o Flyway aplica as migrations e cria todo o schema — a partir daí os demais microsserviços da fase já conseguem ler os dados.

> ℹ️ A conexão com o banco é montada em `DataBaseConfig` a partir das variáveis `DATABASE_IP`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER` e `DATABASE_PASSWORD`. As configurações de execução do IntelliJ (`.run/`) já definem esses valores; ao rodar pelo terminal, exporte-os antes de iniciar a aplicação.

> ℹ️ Ao rodar localmente pela IDE, a aplicação acessa o banco em `localhost:8745`. O valor `5432` só é utilizado pelos containers, que enxergam o PostgreSQL pela rede interna do Docker.

<br> 

## 🚀 Produção

Para execução em ambiente de produção, o projeto disponibiliza os arquivos `docker-compose-postgres-prod.yml` e `docker-compose-agendamentoapi.yml`. Antes de iniciar a aplicação, é necessário configurar o arquivo `.env` na raiz do projeto:

```bash
# DATABASE_NAME
$ Exemplo: postgres

# DATABASE_USER
$ Exemplo: postgres

# DATABASE_PASSWORD
$ Exemplo: postgres@2026

# JWT_SECRET
$ Exemplo: uma string aleatória com pelo menos 32 caracteres

# JWT_EXPIRATION_MS
$ Exemplo: 86400000 (24 horas)
```

As variáveis `DATABASE_*` são utilizadas tanto para **criar** o container do PostgreSQL quanto para a API se **conectar** a ele, de modo que as credenciais não têm como divergir. Se `DATABASE_PASSWORD` ou `JWT_SECRET` não estiverem preenchidas, o Compose interrompe a execução com uma mensagem explícita, em vez de subir com valores em branco.

> ⚠️ `JWT_SECRET` e `JWT_EXPIRATION_MS` possuem valores padrão embutidos no código apenas para facilitar o desenvolvimento local. Em produção, defina obrigatoriamente um `JWT_SECRET` próprio — o valor padrão é público, pois está versionado no repositório.

> ℹ️ Não é necessário configurar a porta do banco: dentro da rede `shared-net` a conexão é sempre feita em `postgres:5432`, valor já fixado nos arquivos Compose.

<br> 

Após configurar o arquivo `.env`, inicie primeiro o banco de dados e, em seguida, a API:

```bash
# 1. PostgreSQL — também cria a rede shared-net (execute apenas uma vez)
docker compose -f docker-compose-postgres-prod.yml up -d --wait

# 2. AgendamentoAPI — aplica as migrations Flyway e cria o schema
docker compose -f docker-compose-agendamentoapi.yml up -d
```

Dessa forma, a API será iniciada utilizando as variáveis definidas no arquivo `.env` e ficará disponível na porta `9027`.

> ℹ️ Os demais microsserviços da fase entram na mesma rede e utilizam portas distintas no host — a HistoricoAPI, por exemplo, é publicada em `9028`. Todos escutam em `9027` dentro do próprio container, então chamadas entre containers usam o nome do container e a porta interna (ex.: `http://AgendamentoAPI:9027`).

> ℹ️ Quando a API é executada em produção, é criada automaticamente uma pasta chamada `logs` no diretório onde a aplicação está sendo executada. Essa pasta é responsável por armazenar todos os logs gerados pela API, sendo organizados de forma diária, ou seja, a cada novo dia é gerado um arquivo específico contendo a data correspondente, facilitando a rastreabilidade e análise das execuções. Além disso, a aplicação possui uma política de limpeza automática, na qual os arquivos de `logs` são mantidos por um período de 30 dias. Após esse prazo, os `logs` mais antigos são excluídos automaticamente, garantindo melhor gerenciamento de armazenamento.

<br> 

## 🔐 Autenticação

A API utiliza autenticação via **JWT (JSON Web Token)**. Antes de acessar qualquer endpoint protegido, é necessário realizar login para obter um token de acesso.

```bash
# Endpoint de login
POST /v1/auth/login
```

A resposta traz o token que deve ser enviado no header `Authorization` das próximas requisições:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer"
}
```

```bash
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

O token expira em 24 horas (configurável via `JWT_EXPIRATION_MS`). Após expirar, é necessário realizar login novamente.

> ⚠️ **Não há autocadastro:** todo cadastro (`POST`) exige token.
> - **Médico, Enfermeiro e Recepcionista:** criar, atualizar e excluir → **só `ADMINISTRADOR`**; listar → todos os perfis **exceto `PACIENTE`**.
> - **Paciente:** criar → só `RECEPCIONISTA`; **excluir → só `ADMINISTRADOR`**; listar/atualizar → todos **exceto `PACIENTE`**.
> - A role `PACIENTE` não acessa nenhum cadastro — só `GET /v1/agendamento` (as próprias consultas).
> - **Histórico do Paciente:** criar e atualizar → `MEDICO` ou `RECEPCIONISTA`; visualizar → `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO`; excluir → `ADMINISTRADOR`. Fora do `DELETE`, o `ADMINISTRADOR` não acessa o histórico.
> - **Consultas (agendamento):** marcar → `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO`; remarcar (`PATCH`) e cancelar (`DELETE`) → **só `RECEPCIONISTA`**; visualizar → `MEDICO`, `RECEPCIONISTA`, `ENFERMEIRO` ou `PACIENTE`. O `ADMINISTRADOR` não acessa consultas. Quando quem consulta é um `PACIENTE`, o `GET` retorna **apenas as consultas dele**; os demais papéis veem todas. O cancelamento remove o registro de fato (a tabela `agendamento` não tem situação de cadastro).

<br>

### Sobre as senhas no banco de dados

O projeto utiliza **BCrypt** para armazenar senhas. Isso significa que a senha de um usuário **nunca** fica salva em texto puro no banco — o que aparece na coluna `senha` (por exemplo, `$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a`) é um **hash criptográfico**, gerado a partir da senha real combinada com um valor aleatório (chamado de "sal"). Esse processo é de mão única: não existe forma de reverter o hash de volta para a senha original.

Para fazer login, você sempre usa a senha **em texto puro** que foi escolhida no cadastro — nunca o hash salvo no banco.

> ℹ️ Hoje, no seed de testes, todos os usuários compartilham o **mesmo hash** no banco — isso é só uma facilidade para os testes locais. Usuários cadastrados normalmente pela API terão hashes diferentes entre si mesmo usando a mesma senha, pois o BCrypt gera um sal aleatório novo a cada cadastro.

<br>

## 🌐 Endpoints

Todas as rotas abaixo são relativas ao context path **`/AgendamentoAPI`**.

| Método   | Rota                  | Autenticação | Descrição                                        |
| -------- | --------------------- | ------------ | ------------------------------------------------ |
| `POST`   | `/v1/auth/login`      | Pública      | Autentica o usuário e devolve o token JWT        |
| `GET`    | `/v1/medico`          | Autenticado, exceto `PACIENTE` | Lista os médicos (paginado)           |
| `POST`   | `/v1/medico`          | `ADMINISTRADOR` | Cadastra um médico                            |
| `PATCH`  | `/v1/medico/{id}`     | `ADMINISTRADOR` | Atualiza os dados de um médico                |
| `DELETE` | `/v1/medico/{id}`     | `ADMINISTRADOR` | Exclui logicamente um médico                  |
| `GET`    | `/v1/enfermeiro`      | Autenticado, exceto `PACIENTE` | Lista os enfermeiros (paginado)        |
| `POST`   | `/v1/enfermeiro`      | `ADMINISTRADOR` | Cadastra um enfermeiro                        |
| `PATCH`  | `/v1/enfermeiro/{id}` | `ADMINISTRADOR` | Atualiza os dados de um enfermeiro            |
| `DELETE` | `/v1/enfermeiro/{id}` | `ADMINISTRADOR` | Exclui logicamente um enfermeiro              |
| `GET`    | `/v1/paciente`        | Autenticado, exceto `PACIENTE` | Lista os pacientes (paginado)          |
| `POST`   | `/v1/paciente`        | `RECEPCIONISTA` | Cadastra um paciente                          |
| `PATCH`  | `/v1/paciente/{id}`   | Autenticado, exceto `PACIENTE` | Atualiza os dados de um paciente       |
| `DELETE` | `/v1/paciente/{id}`   | `ADMINISTRADOR` | Exclui logicamente um paciente                |
| `GET`    | `/v1/recepcionista`   | Autenticado, exceto `PACIENTE` | Lista os recepcionistas (paginado)    |
| `POST`   | `/v1/recepcionista`   | `ADMINISTRADOR` | Cadastra um recepcionista                     |
| `PATCH`  | `/v1/recepcionista/{id}` | `ADMINISTRADOR` | Atualiza os dados de um recepcionista      |
| `DELETE` | `/v1/recepcionista/{id}` | `ADMINISTRADOR` | Exclui logicamente um recepcionista        |
| `GET`    | `/v1/historico-paciente` | `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO` | Lista os históricos dos pacientes (paginado) |
| `POST`   | `/v1/historico-paciente` | `MEDICO` ou `RECEPCIONISTA` | Cadastra um histórico de paciente |
| `PATCH`  | `/v1/historico-paciente/{id}` | `MEDICO` ou `RECEPCIONISTA` | Atualiza um histórico de paciente |
| `DELETE` | `/v1/historico-paciente/{id}` | `ADMINISTRADOR` | Exclui um histórico de paciente       |
| `GET`    | `/v1/agendamento`     | `MEDICO`, `RECEPCIONISTA`, `ENFERMEIRO` ou `PACIENTE` | Lista as consultas agendadas (paginado); o `PACIENTE` recebe só as próprias |
| `POST`   | `/v1/agendamento`     | `MEDICO`, `RECEPCIONISTA` ou `ENFERMEIRO` | Agenda uma nova consulta            |
| `PATCH`  | `/v1/agendamento/{id}`| `RECEPCIONISTA` | Remarca a data/hora e atualiza a observação de uma consulta |
| `DELETE` | `/v1/agendamento/{id}`| `RECEPCIONISTA` | Cancela (remove) uma consulta agendada        |

> ℹ️ O `PATCH` é uma **atualização parcial**: **nenhum campo é obrigatório**. Envie somente os que deseja alterar — os ausentes (e também os enviados como string vazia ou só com espaços) preservam o valor atual. As validações de formato continuam valendo para os campos que forem enviados preenchidos: um `email` inválido ou um `cpf` fora dos 11 caracteres ainda respondem `400`. Em contrapartida, o Jackson está em modo estrito, então um campo **desconhecido** no JSON derruba a requisição com `400`.

> ℹ️ O cadastro de paciente exige, além dos dados pessoais, `email` (único e validado) e `telefone`. O agendamento exige `medicoId`, `pacienteId` e `dataHoraConsulta`; `observacao` é opcional.

> ⚠️ **Conflito de horário:** não é possível marcar uma consulta se o médico **ou** o paciente já tiver outra agendada para a mesma data/hora — a API responde `409 Horário de Consulta Indisponível!`.

> ℹ️ A exclusão é **lógica**: o registro não é removido do banco, apenas tem sua situação de cadastro alterada para `EXCLUIDO` — e, junto com ele, o usuário perde o acesso (o login passa a responder `403`). A única exceção é o histórico do paciente, que não tem situação de cadastro e é removido de fato.

> ℹ️ As listagens são paginadas e a numeração começa em **1**. Utilize os parâmetros `page`, `size` e `sort` (por exemplo, `/v1/paciente?page=1&size=20&sort=nome`).

<br> 

## 📑 Swagger

Para acessar a documentação da API, inicie a aplicação utilizando a opção `BootRun - DEV` e acesse o link abaixo no seu navegador.

```bash
# URL para acessar a documentação da API 
$ http://localhost:9017/AgendamentoAPI/swagger-ui/index.html
```

<br> 

Caso inicie a aplicação utilizando a opção `BootRun - PROD` e acesse o link abaixo no seu navegador.

```bash
# URL para acessar a documentação da API 
$ http://localhost:9027/AgendamentoAPI/swagger-ui/index.html
```

> ⚠️ O context path diferencia maiúsculas de minúsculas: utilize `/AgendamentoAPI`, e não `/agendamentoapi`.

<br> 

## 🧪 Testes

A suíte de testes é composta por **testes de integração reais**: eles sobem o contexto do Spring e se conectam a um PostgreSQL de verdade. Por isso, **o banco precisa estar no ar antes de executar os testes**:

```bash
docker compose -f docker-compose-postgres-dev.yml up -d --wait
./gradlew test
```

Os testes utilizam o perfil `test`, que carrega a configuração de banco de `TestDataBaseConfig` (com valores padrão apontando para `localhost:8745`) e executam dentro de transações revertidas ao final de cada caso. Os payloads das requisições dos testes de controller ficam em `src/test/resources/<dominio>/`, lidos por caminho relativo — portanto, execute os testes a partir da raiz do projeto.

Ao final da execução, o **JaCoCo** gera o relatório de cobertura em:

```bash
build/reports/jacoco/test/html/index.html
```

<br> 

## 🔄 Integração Contínua

O workflow em `.github/workflows/workflow.yml` é acionado a cada Pull Request aberto contra a branch `main`. Ele sobe um container PostgreSQL, configura o Java 21 e executa `./gradlew build`, garantindo que a compilação e os testes automatizados passem antes do merge.

<br> 

## ⚠️ Observação

Recomenda-se utilizar o IntelliJ IDEA como IDE para este projeto, pois ele já possui configurações prontas para execução e build, como `BootRun - DEV`, `BootRun - PROD` e `Clean Build - [Without Tests]`. Dessa forma, o uso do IntelliJ proporciona uma experiência mais prática e otimizada no desenvolvimento.
