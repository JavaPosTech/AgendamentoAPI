<div align="center"> <br> 
  <img align="center" alt="guru-java" height="150" width="150" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg" />
</div> 

<br> 

<div align="center">
    Turma 12ADJT – Projeto desenvolvido na pós-graduação em Arquitetura e Desenvolvimento em Java da FIAP. O objetivo é desenvolver um sistema hospitalar baseado em microsserviços, com serviços independentes para o gerenciamento de agendamentos e envio de notificações, utilizando Spring Security para autenticação e autorização e comunicação assíncrona entre os serviços.
</div> 

 <br> <br> 

## 🧰 Ferramentas Utilizadas

* 🛠️ Gradle

* 🦅 Flyway

* ☕️ Java 21

* 🐘 Postgres

* 🧪 JUnit 5 + JaCoCo

* 🟢 Spring Boot 4.0.5

* 🔐 Spring Security + JWT

<br> 

## ⚙️ Configurações Disponíveis

🔹 `BootRun - DEV`, executa a API no perfil de desenvolvimento, ideal para desenvolvimento local, criação de novas funcionalidades e realização de testes durante a implementação.

<br> 

🔹 `BootRun - PROD`, executa a API utilizando o perfil de produção localmente, permitindo simular o comportamento da aplicação em ambiente de produção.

<br> 

🔹 `Clean Build - [Without Tests]`, realiza o processo de build da aplicação sem executar os testes automatizados, limpando arquivos anteriores e recompilando o projeto de forma mais rápida. 

<br> 

## 🛠️ Desenvolvimento 

Para o ambiente de desenvolvimento, o projeto disponibiliza o arquivo `docker-compose-postgres.yml`, já configurado com todas as variáveis necessárias para conexão com o banco de dados. 

Para iniciar o serviço do PostgreSQL, execute no terminal: 

```bash
docker compose -f docker-compose-postgres.yml up -d
```

Em seguida, execute a aplicação utilizando a opção `BootRun - DEV`. Dessa forma, a API será conectada automaticamente ao banco de dados configurado no Docker Compose, facilitando a execução do projeto em ambiente local e ficando disponível na porta `9017`.

<br> 

## 🚀 Produção

Para execução em ambiente de produção, o projeto disponibiliza o arquivo `docker-compose-agendamentoapi.yml`. Antes de iniciar a aplicação, é necessário configurar o arquivo `.env` com as variáveis de conexão do banco de dados, conforme o ambiente desejado:

```bash
# DATABASE_PORT
$ Exemplo: 5432

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

> ℹ️ Importante: a variável `DATABASE_PORT` representa a porta utilizada pela aplicação para se conectar ao banco de dados dentro da rede interna do Docker.
O valor padrão é `5432`. Caso deseje alterar essa porta no arquivo  `.env`, também será necessário ajustar o arquivo `docker-compose-agendamentoapi.yml`, atualizando a porta interna do container PostgreSQL para o mesmo valor configurado.

```yaml
ports:
  - "8745:5432"
```

Se alterar `DATABASE_PORT` para `5433`, o mapeamento deverá ser ajustado para:

```yaml
ports:
  - "8745:5433"
```

Nesse exemplo:

* `8745` = porta externa utilizada pelo host para acessar o banco
* `5432` ou `5433` = porta interna utilizada pela API para se conectar ao PostgreSQL

<br> 

Após configurar o arquivo `.env` com as variáveis de conexão do banco de dados, execute no terminal:

```bash
docker compose -f docker-compose-agendamentoapi.yml up -d
```

Dessa forma, a API será iniciada utilizando as variáveis definidas no arquivo `.env` e ficará disponível na porta `9027`.

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

> ⚠️ Cadastro (`POST`) de Médico, Paciente e Enfermeiro **não exige token** (autocadastro livre). Todas as demais operações — listar, atualizar e excluir — exigem um usuário autenticado.

<br>

### Perfis de acesso

Cada usuário pertence a um **tipo** (`ADMINISTRADOR`, `MEDICO`, `ENFERMEIRO`, `RECEPCIONISTA` ou `PACIENTE`), que é gravado no token no momento do login e transformado na `role` usada pelo Spring Security. As regras aplicadas hoje são:

| Rotas | Quem pode acessar |
| --- | --- |
| `POST /v1/auth/login`, Swagger | Público |
| `POST /v1/medico`, `POST /v1/paciente`, `POST /v1/enfermeiro` | Público (autocadastro) |
| `/v1/recepcionista/**` | Apenas `ADMINISTRADOR` |
| `/v1/historico-paciente/**` | Apenas `ADMINISTRADOR` |
| Demais rotas | Qualquer usuário autenticado |

O cadastro de Recepcionista e o Histórico do Paciente são restritos ao `ADMINISTRADOR` em todos os métodos (`GET`, `POST`, `PATCH` e `DELETE`). Um usuário autenticado com outro perfil recebe `403 Acesso Negado!`; uma requisição sem token — ou com token inválido, expirado ou pertencente a um usuário excluído — recebe `401 Não Autorizado!`.

<br>

### Exclusão de cadastro e acesso

A exclusão de Médico, Enfermeiro, Recepcionista e Paciente é **lógica**: o cadastro passa para a situação `EXCLUIDO` e permanece no banco para fins de histórico. Junto com o cadastro, as **credenciais do usuário também são desativadas** — a coluna `id_situacaocadastro` da tabela `usuario` recebe `EXCLUIDO`. A partir daí:

- uma nova tentativa de `POST /v1/auth/login` responde `403 Usuário Inativo!`, mesmo com a senha correta;
- um token emitido **antes** da exclusão deixa de valer: o filtro de segurança recusa a requisição com `401 Não Autorizado!`.

Diferente dos demais, o Histórico do Paciente é excluído de forma **definitiva**, pois a tabela não guarda situação de cadastro.

<br>

### Sobre as senhas no banco de dados

O projeto utiliza **BCrypt** para armazenar senhas. Isso significa que a senha de um usuário **nunca** fica salva em texto puro no banco — o que aparece na coluna `senha` (por exemplo, `$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a`) é um **hash criptográfico**, gerado a partir da senha real combinada com um valor aleatório (chamado de "sal"). Esse processo é de mão única: não existe forma de reverter o hash de volta para a senha original.

Para fazer login, você sempre usa a senha **em texto puro** que foi escolhida no cadastro — nunca o hash salvo no banco.

> ℹ️ Hoje, no seed de testes, todos os usuários compartilham o **mesmo hash** no banco — isso é só uma facilidade para os testes locais. Usuários cadastrados normalmente pela API terão hashes diferentes entre si mesmo usando a mesma senha, pois o BCrypt gera um sal aleatório novo a cada cadastro.

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

<br> 

## 🧪 Testes

A suíte é composta por **testes de integração** que sobem o contexto completo do Spring (`@SpringBootTest`) contra um **PostgreSQL real** — não há banco em memória. Antes de rodar, garanta que o banco de desenvolvimento esteja no ar:

```bash
docker compose -f docker-compose-postgres.yml up -d
```

```bash
# Roda toda a suíte e gera o relatório de cobertura
./gradlew test

# Força a reexecução mesmo que nada tenha mudado
./gradlew cleanTest test
```

Os testes usam o perfil `test` e a classe `TestDataBaseConfig`, que monta o `DataSource` a partir das variáveis `DATABASE_IP`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER` e `DATABASE_PASSWORD` (com os mesmos padrões do `docker-compose-postgres.yml`). Cada teste roda dentro de uma transação que sofre *rollback* ao final, então o seed das migrations é preservado entre as execuções.

### Organização

```
config/                     → infraestrutura da suíte (AbstractTest, AbstractControllerTest, TestDataBaseConfig)
controller/<dominio>/       → testes de endpoint via MockMvc, com corpo lido de src/test/resources
service/<dominio>/          → testes de regra de negócio contra o banco
security/                   → testes de autenticação e autorização (perfis de acesso e filtro JWT)
```

### Cobertura

O relatório do JaCoCo é gerado automaticamente ao final do `./gradlew test`:

```bash
# Relatório de cobertura
$ build/reports/jacoco/test/html/index.html
```

A cobertura atual é de **100%** de instruções e de branches. O escopo medido é o de `controller`, `service` e `repository` — os pacotes `config`, `enums`, `exceptions` e `model` são excluídos do cálculo no `build.gradle.kts`, por serem majoritariamente estrutura, records e anotações.

<br> 

## ⚠️ Observação

Recomenda-se utilizar o IntelliJ IDEA como IDE para este projeto, pois ele já possui configurações prontas para execução e build, como `BootRun - DEV`, `BootRun - PROD` e `Clean Build - [Without Tests]`. Dessa forma, o uso do IntelliJ proporciona uma experiência mais prática e otimizada no desenvolvimento.
