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

## [1.0.0] - 2026-09-05

### Added
- **CRUD de Aluno** — endpoints para criação, consulta, atualização e remoção de alunos, com validação de campos obrigatórios, gênero e nível de experiência.
- **CRUD de Funcionário** — endpoints para criação, consulta, atualização e remoção de funcionários, com suporte a perfis de acesso.
- **Autenticação JWT** — endpoint de login que valida credenciais e retorna um token JWT; demais rotas protegidas por verificação do token.
- **Tratamento global de erros** — respostas padronizadas para erros de validação (`400`) e credenciais inválidas (`401`), com corpo estruturado em `ErroRespostaDTO` e lista de `CampoErroDTO`.
- **Documentação OpenAPI/Swagger** — interface Swagger UI disponível em `/swagger-ui.html` com título, descrição, contato e versão lidos automaticamente do `pom.xml`.
