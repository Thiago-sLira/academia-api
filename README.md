# Academia API

> ⚠️ **Projeto estudantil** — desenvolvido para fins de aprendizado acadêmico. Não destinado a uso em produção.

API REST para gerenciamento de academia, cobrindo alunos, funcionários e autenticação via JWT.

---

## Rodando localmente

### 1. Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL** — banco de dados acessível localmente ou remoto

Verifique as versões instaladas:

```bash
java -version    # deve exibir 17 ou superior
mvn -version     # deve exibir 3.8 ou superior (opcional se usar ./mvnw)
```

### 2. Variáveis de ambiente

Crie um arquivo `.env` ou exporte as variáveis no terminal antes de iniciar:

```bash
export DB_URL=jdbc:postgresql://<host>:<porta>/<banco>?sslmode=require
export DB_USER=<usuario>
export DB_PASSWORD=<senha>

# Opcionais — possuem valores padrão para desenvolvimento
export JWT_SECRET=<chave-hex-de-64-chars>   # padrão de dev já incluso
export JWT_EXPIRATION=86400000               # 24 horas em ms (padrão)
```

| Variável | Obrigatório | Descrição |
|---|---|---|
| `DB_URL` | ✅ Sim | URL JDBC do PostgreSQL |
| `DB_USER` | ✅ Sim | Usuário do banco |
| `DB_PASSWORD` | ✅ Sim | Senha do banco |
| `JWT_SECRET` | Não | Chave hex para assinar tokens JWT (padrão de dev incluso) |
| `JWT_EXPIRATION` | Não | Expiração do token em ms (padrão: `86400000` = 24 h) |

> O banco deve ter o schema criado previamente. O Hibernate valida (`ddl-auto=validate`) mas **não cria** as tabelas automaticamente.

### 3. Clonar e executar

```bash
# 1. Clonar o repositório
git clone <url-do-repositorio>
cd academia-api

# 2. Compilar o projeto
./mvnw compile

# 3. Iniciar a aplicação
./mvnw spring-boot:run
```

A API estará disponível em **`http://localhost:8080`**.

O Swagger UI estará em **`http://localhost:8080/swagger-ui.html`**.

### 4. Rodando os testes

Os testes **não precisam de PostgreSQL** — usam um banco H2 em memória:

```bash
./mvnw test
```

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.2 |
| Maven | 3.x (wrapper incluso) |
| PostgreSQL | Driver 42.x (gerenciado pelo Spring Boot) |
| Spring Data JPA / Hibernate | Gerenciado pelo Spring Boot 3.3.2 |
| Spring Security Crypto (BCrypt) | Gerenciado pelo Spring Boot 3.3.2 |
| JWT | 0.12.6 |
| Springdoc OpenAPI (Swagger UI) | 2.5.0 |
| Lombok | Gerenciado pelo Spring Boot 3.3.2 |
| Bean Validation (Hibernate Validator) | Gerenciado pelo Spring Boot 3.3.2 |

**Testes**

| Tecnologia | Versão |
|---|---|
| JUnit 5 + Spring Boot Test | Gerenciado pelo Spring Boot 3.3.2 |
| H2 Database (in-memory) | Gerenciado pelo Spring Boot 3.3.2 |
| Database Rider (DBUnit) | 1.44.0 |

---

## Endpoints

### Alunos — `/api/alunos`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/alunos` | Listar todos os alunos |
| `GET` | `/api/alunos/{id}` | Buscar aluno por ID |
| `POST` | `/api/alunos` | Cadastrar novo aluno |
| `PUT` | `/api/alunos/{id}` | Atualizar dados de um aluno |
| `DELETE` | `/api/alunos/{id}` | Remover um aluno |

### Funcionários — `/api/funcionarios`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/funcionarios` | Listar todos os funcionários |
| `GET` | `/api/funcionarios/ativos` | Listar apenas funcionários ativos |
| `GET` | `/api/funcionarios/{id}` | Buscar funcionário por ID |
| `POST` | `/api/funcionarios` | Cadastrar novo funcionário |
| `PUT` | `/api/funcionarios/{id}` | Atualizar dados de um funcionário |
| `DELETE` | `/api/funcionarios/{id}` | Remover um funcionário |
| `POST` | `/api/funcionarios/login` | Autenticar e obter token JWT |

---

## Respostas de erro

Todos os erros retornam o mesmo formato estruturado:

```json
{
  "timestamp": "2025-07-10T14:30:00",
  "status": 400,
  "erro": "Erro de validação nos campos informados",
  "detalhes": [
    {
      "campo": "email",
      "mensagem": "O campo 'email' deve ser um e-mail válido",
      "userHelp": null
    }
  ]
}
```

| Status | Situação |
|---|---|
| `400` | Validação de campos falhou ou JSON malformado |
| `401` | Credenciais inválidas no login |
| `404` | Recurso não encontrado pelo ID informado |
| `409` | E-mail já está em uso (violação de unicidade) |
| `415` | `Content-Type` diferente de `application/json` |

---

## Executando os testes

```bash
./mvnw test
```

Os testes de integração sobem um banco H2 em memória com o schema definido em
`src/test/resources/schema-test.sql` e usam [Database Rider](https://database-rider.github.io/)
para controle de datasets por teste via `@DataSet` e `@ExpectedDataSet`.

### Estrutura dos testes

```
src/test/java/com/academia/api/
├── BaseIntegrationTest.java          # Configuração base (MockMvc, ObjectMapper, DBRider)
├── aluno/
│   ├── CadastrarAlunoIntegrationTest.java
│   ├── ListarAlunosIntegrationTest.java
│   ├── BuscarAlunoPorIdIntegrationTest.java
│   ├── AtualizarAlunoIntegrationTest.java
│   └── DeletarAlunoIntegrationTest.java
└── funcionario/
    ├── AuthIntegrationTest.java
    ├── CadastrarFuncionarioIntegrationTest.java
    ├── ListarFuncionariosIntegrationTest.java
    ├── BuscarFuncionarioPorIdIntegrationTest.java
    ├── AtualizarFuncionarioIntegrationTest.java
    └── DeletarFuncionarioIntegrationTest.java
```

---

## Estrutura do projeto

```
src/main/java/com/academia/api/
├── AcademiaApiApplication.java
├── configs/           # CorsConfig, OpenApiConfig, PasswordEncoderConfig
├── controllers/       # AlunoController, FuncionarioController
├── dtos/
│   ├── requests/      # AlunoRequestDTO, FuncionarioRequestDTO, LoginRequestDTO
│   └── responses/     # AlunoResponseDTO, FuncionarioResponseDTO, LoginResponseDTO
├── exceptions/        # GlobalExceptionHandler, exceções de domínio, ErroRespostaDTO
├── models/
│   ├── entities/      # Aluno, Funcionario
│   └── enums/         # Genero, NivelExperiencia, PerfilFuncionario
├── repositories/      # AlunoRepository, FuncionarioRepository
├── services/          # AlunoService, FuncionarioService, JwtService
└── validation/        # @ValueOfEnum, ValueOfEnumValidator, EnumNormalizer
```

---

## Changelog

Veja [CHANGELOG.md](CHANGELOG.md) para o histórico de versões.
