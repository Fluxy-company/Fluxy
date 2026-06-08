# Fluxy

Repositório grupo 7 - Fluxy

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/) instalados

## Como rodar a aplicação

### 1. Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DATABASE_NAME=iefc_db
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha

MAILTRAP_USERNAME=seu_mailtrap_username
MAILTRAP_PASSWORD=seu_mailtrap_password
RELATORIO_EMAIL_DESTINATARIO=email@destino.com
```

> As credenciais do Mailtrap podem ser obtidas em [mailtrap.io](https://mailtrap.io) (SMTP Settings → Integrations → Java).

### 2. Subir os containers

```bash
docker compose up --build -d
```

O Docker irá:
1. Criar o banco de dados PostgreSQL
2. Compilar e iniciar o backend Spring Boot (aguarda o banco ficar saudável)
3. Iniciar os dois frontends (aguardam o backend ficar saudável)

> A primeira execução pode levar alguns minutos para baixar as dependências Maven. As execuções seguintes serão mais rápidas graças ao cache.

### 3. Acessar a aplicação

| Serviço | URL |
|---|---|
| Backend (API) | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Frontend Fluxy (Dashboard) | http://localhost:5173 |
| Frontend IEFC (Institucional) | http://localhost:5174 |

### 4. Credenciais padrão

A aplicação cria automaticamente dois usuários ao iniciar:

| Usuário | Email | Senha | Role |
|---|---|---|---|
| Admin | admin@admin.com | Admin@123 | ADMIN |
| Usuário | user@user.com | User@123 | BASIC |

### 5. Parar a aplicação

```bash
docker compose down
```

Para remover também os volumes (banco de dados e cache Maven):

```bash
docker compose down -v
```

## Estrutura do Projeto

```
├── docker-compose.yml          # Orquestração dos 4 serviços
├── .env                        # Variáveis de ambiente
├── back-end/iefc-backend/      # API Spring Boot (Java 21)
├── fluxy-frontend/             # Dashboard React + Vite
└── iefc-institucional/         # Site institucional React + Vite
```

## API - Endpoints Principais

### Autenticação
- `POST /api/v1/login` — Login (retorna JWT)
- `POST /api/v1/usuarios` — Cadastro de novo usuário

### Usuários (ADMIN)
- `GET /api/v1/usuarios` — Listar todos
- `GET /api/v1/usuarios/{id}` — Buscar por ID
- `DELETE /api/v1/usuarios/{id}` — Deletar

### Empresas
- `POST /api/v1/empresas` — Criar (ADMIN)
- `GET /api/v1/empresas` — Listar (BASIC)
- `PUT /api/v1/empresas/{id}` — Atualizar (ADMIN)
- `DELETE /api/v1/empresas/{id}` — Deletar (ADMIN)

### Projetos (BASIC)
- `POST /api/v1/projetos` — Criar
- `GET /api/v1/projetos` — Listar
- `GET /api/v1/projetos/status/{status}` — Filtrar por status
- `PUT /api/v1/projetos/{id}` — Atualizar
- `DELETE /api/v1/projetos/{id}` — Deletar

### Relatórios
- `POST /api/v1/relatorio/gerar` — Gerar relatório PDF

> Documentação completa disponível no Swagger UI: http://localhost:8080/swagger-ui.html
