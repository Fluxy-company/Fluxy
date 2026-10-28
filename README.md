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

## CI e imagens Docker da IEFC

O workflow `.github/workflows/iefc-images.yml` verifica os dois backends com `mvn verify`, compila o frontend com `npm ci` e `npm run build`, e constrói as três imagens de produção. Ele executa em pull requests, em pushes para `main` e manualmente pela aba Actions. Pull requests e execuções manuais fora de `main` não publicam imagens.

Após todas as verificações passarem em `main`, o workflow publica as imagens como tags do repositório `fluxy` no namespace do usuário do Docker Hub (por exemplo, `matheusdtjb/fluxy`):

- `<usuario>/fluxy:iefc-backend-latest`
- `<usuario>/fluxy:email-service-latest`
- `<usuario>/fluxy:iefc-institucional-latest`

Cada imagem também recebe a tag `<componente>-sha-<commit completo>` para identificar a versão exata. A publicação das três imagens não é atômica; em caso de falha, confira o resultado de cada job na aba Actions. O workflow publica imagens, mas não faz deploy.

Em **Settings > Secrets and variables > Actions**, configure os secrets de repositório `MATHEUS_DOCKER_HUB_USERNAME` e `MATHEUS_DOCKER_HUB_PERSONAL_ACESS_TOKEN` (a grafia `ACESS` é intencional e deve corresponder ao secret existente). O token precisa de permissão de leitura e escrita no repositório `fluxy` do Docker Hub.

**Chave JWT:** o build no GitHub remove `app.key` do contexto Docker antes de construir a imagem do backend. Para executá-la, monte uma chave RSA privada em formato PKCS#8 como secret e defina `JWT_PRIVATE_KEY=file:/run/secrets/jwt-private.pem`. Após trocar o par de chaves, monte também a chave pública correspondente e defina `JWT_PUBLIC_KEY=file:/run/secrets/jwt-public.pem`. A chave privada já está rastreada no Git: considere-a comprometida, faça a rotação e remova-a do repositório e do histórico de acordo com o processo de segurança da equipe. Builds Docker locais que não removam a chave ainda podem incluí-la na imagem.

## Terraform no AWS CloudShell

1. Entre no AWS Console com o acesso apropriado e abra o CloudShell em uma região suportada. Sua identidade precisa de permissão para usar o CloudShell e para acessar os recursos que serão gerenciados. MFA ou informações adicionais da conta podem ser necessários no login.
2. No CloudShell, execute `aws sts get-caller-identity` para verificar a conta e a identidade. O CloudShell fornece credenciais temporárias automaticamente; a senha do console não é uma credencial de API para o Terraform.
3. Execute `terraform version`. Se não estiver instalado, siga a [instalação oficial do Terraform](https://developer.hashicorp.com/terraform/install) para Amazon Linux. Instalações fora de `$HOME` podem ser perdidas ao encerrar a sessão.
4. A configuração da IEFC, os pré-requisitos e os procedimentos para criar ou destruir o ambiente estão em [iefc/terraform/README.md](iefc/terraform/README.md). Revise os recursos e custos antes de aplicá-la.

Para uso em equipe, configure um [backend S3](https://developer.hashicorp.com/terraform/language/backend/s3) com versionamento, acesso restrito, criptografia e `use_lockfile = true`. O estado pode conter dados sensíveis; não o adicione ao Git. Não use CloudShell VPC como armazenamento persistente de estado. Para automação no GitHub, use uma role AWS com OIDC em vez de senha ou chaves permanentes.

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
