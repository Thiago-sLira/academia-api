# Changelog

Todas as mudanças relevantes deste projeto serão documentadas neste arquivo.

## Padrão de atualização

Cada versão segue o formato **[MAJOR.MINOR.PATCH]** conforme o [Versionamento Semântico](https://semver.org/lang/pt-BR/):

- **MAJOR** — mudanças incompatíveis com versões anteriores (breaking changes).
- **MINOR** — novas funcionalidades compatíveis com versões anteriores.
- **PATCH** — correções de bugs compatíveis com versões anteriores.

### Categorias utilizadas em cada entrada

| Categoria | Quando usar |
|-----------|-------------|
| `Added`   | Novas funcionalidades |
| `Changed` | Alterações em funcionalidades existentes |
| `Deprecated` | Funcionalidades que serão removidas em versões futuras |
| `Removed` | Funcionalidades removidas |
| `Fixed`   | Correções de bugs |
| `Security` | Correções de vulnerabilidades ou melhorias de segurança |

### Como registrar uma nova versão

1. Adicione um novo bloco **acima** da versão mais recente, seguindo o modelo abaixo.
2. Substitua `Unreleased` pela versão e data de lançamento no formato `YYYY-MM-DD`.
3. Liste apenas as categorias que possuem entradas.
4. Atualize também a versão no `pom.xml`.

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Added
- Descrição da nova funcionalidade.

### Fixed
- Descrição da correção aplicada.
```

---

## [1.1.0] - 2026-10-06

### Added
- **Módulo Tipo de Treino** — entidade `TipoTreino`, repositório e endpoint `GET /api/tipos-treino` para listagem de todos os tipos de treino cadastrados.
- **Módulo Plano de Treino** — entidade `PlanoTreino` com relacionamentos para `TipoTreino` e `Funcionario`; endpoint `POST /api/planos-treino` para criação de planos com validação completa de campos.
- **GET paginado de Planos de Treino** — endpoint `GET /api/planos-treino` com paginação padrão (página 0, tamanho 10) e filtros opcionais por `idTipoTreino`, `idProfessorCriador` e `nivelRecomendado` via `JpaSpecificationExecutor`.
- **Validação de perfil no POST de Plano de Treino** — apenas funcionários com perfil `PROFESSOR` podem criar planos; perfis `ADMIN` e `ALUNO` retornam `403 Forbidden`.
- **Novos tratamentos de erro** — handlers para `TipoTreinoNaoEncontradoException`, `PlanoTreinoNaoEncontradoException`, `ProfessorNaoEncontradoException` (`404`) e `PerfilNaoAutorizadoException` (`403`) no `GlobalExceptionHandler`.
- **Testes de integração — Plano de Treino** — 15 cenários para `POST /api/planos-treino` e 7 cenários para `GET /api/planos-treino` cobrindo paginação, filtros e todos os erros de negócio.
- **Testes de integração — Tipo de Treino** — 2 cenários para `GET /api/tipos-treino`.

### Changed
- **Validações do Plano de Treino** — campo `titulo` reduzido para máximo de 100 caracteres; campo `descricao` tornado obrigatório com máximo de 250 caracteres.
- **Datasets de teste de Funcionário** — todos os 6 arquivos YAML passaram a incluir `tb_tipo_treino: []` e `tb_plano_treino: []` para garantir limpeza correta das tabelas filhas independente da ordem de execução dos testes.

### Fixed
- **FK violation nos testes do IntelliJ** — datasets de funcionário não declaravam `tb_plano_treino`, causando `JdbcSQLIntegrityConstraintViolationException` ao tentar deletar `tb_funcionario` quando testes de plano rodavam primeiro. Corrigido adicionando as tabelas dependentes no final de cada dataset.

---

## [1.0.0] - 2026-09-05

### Added
- **CRUD de Aluno** — endpoints para criação, consulta, atualização e remoção de alunos, com validação de campos obrigatórios, gênero e nível de experiência.
- **CRUD de Funcionário** — endpoints para criação, consulta, atualização e remoção de funcionários, com suporte a perfis de acesso.
- **Autenticação JWT** — endpoint de login que valida credenciais e retorna um token JWT; demais rotas protegidas por verificação do token.
- **Tratamento global de erros** — respostas padronizadas para erros de validação (`400`) e credenciais inválidas (`401`), com corpo estruturado em `ErroRespostaDTO` e lista de `CampoErroDTO`.
- **Documentação OpenAPI/Swagger** — interface Swagger UI disponível em `/swagger-ui.html` com título, descrição, contato e versão lidos automaticamente do `pom.xml`.
