# IEFC × Fluxy

Repositório grupo 7 - Fluxy

Repositório com dois sistemas full-stack desenvolvidos para a Fluxy e a IEFC.

---

## Sobre o Projeto

### IEFC — Instituto Educacional Fabiana Costa
Plataforma de ensino que conecta estudantes a cursos, eventos e projetos da instituição. Conta com área pública institucional, dashboard do aluno, player de vídeo-aulas, controle de progresso e geração de relatórios anuais em PDF para a gestão.

### Fluxy
Sistema de gestão interna para controle de projetos, empresas parceiras e usuários vinculados à instituição.

---

## Tecnologias

| Camada | IEFC | Fluxy |
|---|---|---|
| Backend | Java 21 · Spring Boot 4 · Spring Security (JWT RSA) · JPA/Hibernate · PDFBox · Mailtrap | Java 21 · Spring Boot · Spring Security (JWT RSA) · JPA/Hibernate |
| Banco | PostgreSQL 16 | PostgreSQL 16 |
| Frontend | React 19 · Vite 8 · Tailwind CSS 4 · React Router 7 | React · Vite · Tailwind CSS |
| Container | Docker · Docker Compose | Docker · Docker Compose |

---

## Estrutura do Repositório

```
.
├── docker-compose.yml          ← orquestrador de todos os serviços
├── .env.example                ← template de variáveis de ambiente
├── .env                        ← NÃO commitado (você cria a partir do .env.example)
│
├── iefc/
│   ├── front/                  ← Frontend IEFC (React/Vite)
│   └── iefc-backend/           ← Backend IEFC (Spring Boot)
│       └── src/main/resources/
│           ├── app.key         ← chave privada RSA (não commitada)
│           └── app.pub         ← chave pública RSA (não commitada)
│
└── fluxy/
    ├── front/                  ← Frontend Fluxy (React/Vite)
    └── fluxy-backend/          ← Backend Fluxy (Spring Boot)
        └── src/main/resources/
            ├── app.key         ← chave privada RSA (não commitada)
            └── app.pub         ← chave pública RSA (não commitada)
```
---

## Como Rodar

### 1. Verificar as chaves RSA

Cada backend precisa de um par de chaves RSA para assinar e validar os tokens JWT. Verifique se os arquivos existem antes de subir os containers:

```bash
# IEFC
ls iefc/iefc-backend/src/main/resources/app.key
ls iefc/iefc-backend/src/main/resources/app.pub

# Fluxy
ls fluxy/fluxy-backend/src/main/resources/app.key
ls fluxy/fluxy-backend/src/main/resources/app.pub
```

Se algum arquivo estiver faltando, gere o par com `openssl`
---

### 2. Criar o arquivo `.env`

Copie o template e preencha as variáveis:

```bash
cp .env.example .env
```

Abra o `.env` e preencha os valores marcados com `# PREENCHER`:

```env
# Mailtrap — necessário para envio de e-mail ao gerar relatório IEFC
MAILTRAP_USERNAME=   # PREENCHER com seu usuário do Mailtrap
MAILTRAP_PASSWORD=   # PREENCHER com sua senha do Mailtrap
```

Os demais valores já vêm com padrões prontos para desenvolvimento local.

---

### 3. Subir todos os serviços

```bash
docker compose up --build
```

Na primeira execução o build pode levar alguns minutos (download das imagens base e compilação dos backends).

Para rodar em segundo plano:

```bash
docker compose up --build -d
```

Para parar tudo:

```bash
docker compose down
```

Para parar e apagar os dados dos bancos:

```bash
docker compose down -v
```

---

## URLs de Acesso

| Serviço | URL |
|---|---|
| **IEFC Frontend** | http://localhost:3000 |
| **IEFC API** | http://localhost:8080 |
| **IEFC Swagger** | http://localhost:8080/swagger-ui/index.html |
| **Fluxy Frontend** | http://localhost:3001 |
| **Fluxy API** | http://localhost:8081 |
| **Fluxy Swagger** | http://localhost:8081/swagger-ui/index.html |

---

## Funcionalidades Principais

### IEFC
- Área institucional pública (home, eventos, apoiadores)
- Cadastro e login de alunos com JWT
- Catálogo de cursos com filtro por tema
- Player de aulas com controle de progresso
- Dashboard "Meus Cursos"
- **Área Admin** — criação de cursos e temas *(requer login admin)*
- **Geração de Relatório PDF** anual com template institucional *(requer login admin)*
- Notificação por e-mail ao gerar relatório (Observer Pattern via Mailtrap)

### Fluxy
- Gestão de projetos, empresas e usuários
- Controle de acesso por roles (Admin / Basic)
