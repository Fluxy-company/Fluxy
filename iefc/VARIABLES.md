# Inventário de variáveis e segredos — IEFC

Levantamento de todas as variáveis necessárias para executar a IEFC (pipelines, Terraform e aplicação), com a proposta de onde guardar cada uma no AWS (Secrets Manager ou Systems Manager Parameter Store).

Fontes analisadas:

- `.github/workflows/iefc-images.yml`, `iefc-terraform.yml`, `iefc-terraform-up.yml`, `iefc-terraform-down.yml`
- `iefc/terraform/*.tf` e `iefc/terraform/deployment/*.yml`, `*.env.example`
- `iefc/docker-compose.yml` e `iefc/backend/.env.example` (ambiente local)
- `iefc/backend/iefc-backend/src/main/resources/application.properties` e `application.yml`
- `iefc/backend/email-service/src/main/resources/application.properties`
- `@Value(...)` no código Java das duas APIs
- `iefc/iefc-institucional` (frontend)

Legenda:

- **Tipo**: 🔒 segredo (senha, token, chave) · ⚙️ configuração (não sensível)
- **Obrigatória**: ✅ a aplicação falha sem ela · ➖ opcional (há valor padrão)
- **Destino sugerido**: **SM** = AWS Secrets Manager · **PS** = SSM Parameter Store (`String` ou `SecureString`) · **GH** = GitHub (fica fora do AWS) · **Fixo** = já definido no código/Compose; não precisa coletar

---

## 1. Resumo — o que você precisa reunir

| # | Item a coletar | Tipo | Quem usa | Destino sugerido |
|---|----------------|------|----------|------------------|
| 1 | Senha do banco IEFC (`IEFC_DATABASE_PASSWORD`) | 🔒 | banco + iefc-api | SM `iefc/database` |
| 2 | Senha do banco de e-mail (`EMAIL_DATABASE_PASSWORD`) | 🔒 | banco + email-service | SM `iefc/database` |
| 3 | Usuário e senha do RabbitMQ (`RABBITMQ_USER`, `RABBITMQ_PASS`) | 🔒 | rabbitmq, iefc-api, email-service | SM `iefc/rabbitmq` |
| 4 | Usuário e senha SMTP do Mailtrap (`MAILTRAP_USERNAME`, `MAILTRAP_PASSWORD`) | 🔒 | email-service | SM `iefc/mail` |
| 5 | E-mail e senha do administrador inicial (`APP_ADMIN_EMAIL`, `APP_ADMIN_SENHA`) | 🔒 | iefc-api | SM `iefc/admin` |
| 6 | Par de chaves RSA do JWT (privada e pública, PEM) | 🔒 | iefc-api | SM `iefc/jwt` |
| 7 | (Opcional) usuário padrão (`APP_USUARIO_PADRAO_EMAIL`, `APP_USUARIO_PADRAO_SENHA`) | 🔒 | iefc-api | SM `iefc/admin` |
| 8 | URL pública do sistema (`FRONTEND_BASE_URL`) | ⚙️ | iefc-api (links em e-mails) | PS `/iefc/app/FRONTEND_BASE_URL` |
| 9 | E-mails de remetente/destinatário (opcionais) | ⚙️ | iefc-api, email-service | PS `/iefc/app/...` |
| 10 | Token do Docker Hub (novo, após revogar o exposto) | 🔒 | GitHub Actions | GH secret |
| 11 | Credenciais do AWS Academy (a cada sessão) | 🔒 | GitHub Actions | GH environment secret |

Os itens 1–9 são da **aplicação** e vão para o AWS. Os itens 10–11 são do **pipeline** e precisam ficar no GitHub: são justamente as credenciais usadas para chegar até o AWS.

---

## 2. GitHub Actions (permanecem no GitHub)

### Secrets

| Nome | Tipo | Obrig. | Usado em | Observação |
|------|------|--------|----------|------------|
| `MATHEUS_DOCKER_HUB_USERNAME` | ⚙️ | ✅ | `iefc-images.yml` (login e tags), `iefc-terraform.yml` (`TF_VAR_docker_namespace`) | Valor: `matheusdtjb`. Não é sensível; poderia virar *variable*. |
| `MATHEUS_DOCKER_HUB_PERSONAL_ACESS_TOKEN` | 🔒 | ✅ | `iefc-images.yml` (push) | Grafia `ACESS` é intencional. Permissão *Read & Write* no repo `fluxy`. **O token atual foi exposto no chat: revogar e gerar outro.** |
| `AWS_ACADEMY_CREDENTIALS` | 🔒 | ✅ | `iefc-terraform.yml` (plan e apply) | Bloco completo de *AWS Details → AWS CLI → Show*. Guardar como secret do **ambiente** `aws-academy`. Expira a cada sessão do lab (~4 h). |

### Variables (Settings → Secrets and variables → Actions → Variables)

| Nome | Tipo | Obrig. | Valor validado no teste |
|------|------|--------|-------------------------|
| `IEFC_AWS_REGION` | ⚙️ | ✅ | `us-east-1` |
| `IEFC_AWS_ACCOUNT_ID` | ⚙️ | ✅ | `379094220937` |
| `IEFC_TF_STATE_BUCKET` | ⚙️ | ✅ | `iefc-tfstate-379094220937` (já criado, versionado e criptografado) |
| `IEFC_AVAILABILITY_ZONE` | ⚙️ | ✅ | `us-east-1a` |

### Ambiente

| Item | Observação |
|------|------------|
| Environment `aws-academy` | Configurar *required reviewers* e restrição à branch `main`. Sem isso, o apply/destroy roda sem aprovação. |

---

## 3. Terraform (`iefc/terraform`)

Todas são preenchidas pelo workflow a partir das variables acima; nenhuma é segredo.

| Variável Terraform | Origem | Obrig. | Padrão |
|--------------------|--------|--------|--------|
| `aws_region` | `IEFC_AWS_REGION` | ➖ | `us-east-1` |
| `aws_account_id` | `IEFC_AWS_ACCOUNT_ID` | ✅ | — |
| `availability_zone` | `IEFC_AVAILABILITY_ZONE` | ✅ | — |
| `docker_namespace` | `MATHEUS_DOCKER_HUB_USERNAME` | ✅ | — |
| `instance_profile` | padrão | ➖ | `LabInstanceProfile` |
| `web_instance_type` | padrão | ➖ | `t3.micro` |
| `backend_instance_type` | padrão | ➖ | `t3.medium` |
| `database_instance_type` | padrão | ➖ | `t3.small` |
| backend S3: `bucket`, `key`, `region` | `IEFC_TF_STATE_BUCKET`, fixo `iefc/terraform.tfstate`, `IEFC_AWS_REGION` | ✅ | — |

---

## 4. Aplicação na AWS (instâncias EC2)

Hoje esses valores são digitados manualmente em `/opt/iefc/.env` e `/opt/iefc/secrets/*.pem`, via Session Manager (ver `iefc/terraform/README.md`). São eles que devem ir para o cofre.

### 4.1 Host `iefc-database` (`deployment/database.yml`)

| Variável | Tipo | Obrig. | Valor sugerido | Destino |
|----------|------|--------|----------------|---------|
| `IEFC_DATABASE_NAME` | ⚙️ | ✅ | `iefc_db` | PS `/iefc/database/IEFC_DATABASE_NAME` |
| `IEFC_DATABASE_USERNAME` | ⚙️ | ✅ | `iefc_user` | SM `iefc/database` (junto da senha) |
| `IEFC_DATABASE_PASSWORD` | 🔒 | ✅ | gerar (ex.: 32 caracteres alfanuméricos) | SM `iefc/database` |
| `EMAIL_DATABASE_NAME` | ⚙️ | ✅ | `email_db` | PS `/iefc/database/EMAIL_DATABASE_NAME` |
| `EMAIL_DATABASE_USERNAME` | ⚙️ | ✅ | `email_user` | SM `iefc/database` |
| `EMAIL_DATABASE_PASSWORD` | 🔒 | ✅ | gerar | SM `iefc/database` |

> Os mesmos seis valores precisam ser **idênticos** no host do backend.

### 4.2 Host `iefc-backend` — container `iefc-api` (`deployment/backend.yml`)

| Variável | Propriedade Spring | Tipo | Obrig. | Padrão / valor | Destino |
|----------|--------------------|------|--------|----------------|---------|
| `DOCKER_NAMESPACE` | — (Compose) | ⚙️ | ✅ | `matheusdtjb` | PS `/iefc/app/DOCKER_NAMESPACE` |
| `IEFC_DATABASE_NAME` | `spring.datasource.url` | ⚙️ | ✅ | `iefc_db` | (compartilhado, 4.1) |
| `IEFC_DATABASE_USERNAME` | `spring.datasource.username` | ⚙️ | ✅ | `iefc_user` | (compartilhado, 4.1) |
| `IEFC_DATABASE_PASSWORD` | `spring.datasource.password` | 🔒 | ✅ | — | (compartilhado, 4.1) |
| `RABBITMQ_USER` | `spring.rabbitmq.username` | 🔒 | ✅ | — | SM `iefc/rabbitmq` |
| `RABBITMQ_PASS` | `spring.rabbitmq.password` | 🔒 | ✅ | — | SM `iefc/rabbitmq` |
| `APP_ADMIN_EMAIL` | `app.admin.email` | 🔒 | ✅ | — | SM `iefc/admin` |
| `APP_ADMIN_SENHA` | `app.admin.senha` | 🔒 | ✅ | — (a API **não sobe** sem ela: `IllegalStateException` em `AdminUserConfig`) | SM `iefc/admin` |
| `APP_USUARIO_PADRAO_EMAIL` | `app.usuario-padrao.email` | 🔒 | ➖ | vazio (pula a criação) | SM `iefc/admin` |
| `APP_USUARIO_PADRAO_SENHA` | `app.usuario-padrao.senha` | 🔒 | ➖ | vazio | SM `iefc/admin` |
| `FRONTEND_BASE_URL` | `app.frontend.base-url` | ⚙️ | ➖ | `http://localhost:3000` → usar `http://<IP do proxy>` | PS `/iefc/app/FRONTEND_BASE_URL` |
| `RELATORIO_EMAIL_DESTINATARIO` | `app.relatorio.email.destinatario` | ⚙️ | ➖ | `admin@iefc.org.br` | PS `/iefc/app/...` |
| `EMAIL_SERVICE_REMETENTE` | `app.email-service.remetente` | ⚙️ | ➖ | `no-reply@iefc.org.br` | PS `/iefc/app/...` |
| `ADMIN_NOTIFICACAO_EMAIL` | `app.email-service.admin-destinatario` | ⚙️ | ➖ | `admin@iefc.org.br` | PS `/iefc/app/...` |
| `JWT_PRIVATE_KEY` | `jwt.private.key` | 🔒 | ✅ | `file:/run/secrets/jwt-private.pem` (arquivo montado) | conteúdo PEM em SM `iefc/jwt` |
| `JWT_PUBLIC_KEY` | `jwt.public.key` | ⚙️ | ✅ | `file:/run/secrets/jwt-public.pem` | conteúdo PEM em SM `iefc/jwt` |
| `IEFC_DATABASE_HOST` | `spring.datasource.url` | ⚙️ | — | **Fixo** `10.20.2.10` no Compose | — |
| `IEFC_DATABASE_PORT` | `spring.datasource.url` | ⚙️ | — | **Fixo** `5432` | — |
| `RABBITMQ_HOST` | `spring.rabbitmq.host` | ⚙️ | — | **Fixo** `rabbitmq` | — |
| `REDIS_HOST` | `spring.data.redis.host` | ⚙️ | — | **Fixo** `redis` | — |
| `REDIS_PORT` | `spring.data.redis.port` | ⚙️ | ➖ | `6379` | — |

### 4.3 Host `iefc-backend` — container `email-service`

| Variável | Propriedade Spring | Tipo | Obrig. | Padrão / valor | Destino |
|----------|--------------------|------|--------|----------------|---------|
| `EMAIL_DATABASE_NAME` | `spring.datasource.url` | ⚙️ | ✅ | `email_db` | (compartilhado, 4.1) |
| `EMAIL_DATABASE_USERNAME` | `spring.datasource.username` | ⚙️ | ✅ | `email_user` | (compartilhado, 4.1) |
| `EMAIL_DATABASE_PASSWORD` | `spring.datasource.password` | 🔒 | ✅ | — | (compartilhado, 4.1) |
| `RABBITMQ_USER` / `RABBITMQ_PASS` | `spring.rabbitmq.*` | 🔒 | ✅ | — | (compartilhado, 4.2) |
| `MAILTRAP_HOST` | `spring.mail.host` | ⚙️ | ➖ | `sandbox.smtp.mailtrap.io` | PS `/iefc/mail/MAILTRAP_HOST` |
| `MAILTRAP_PORT` | `spring.mail.port` | ⚙️ | ➖ | `2525` | PS `/iefc/mail/MAILTRAP_PORT` |
| `MAILTRAP_USERNAME` | `spring.mail.username` | 🔒 | ✅ | — | SM `iefc/mail` |
| `MAILTRAP_PASSWORD` | `spring.mail.password` | 🔒 | ✅ | — | SM `iefc/mail` |
| `EMAIL_REMETENTE_PADRAO` | `app.email.remetente-padrao` | ⚙️ | ➖ | `no-reply@iefc.org.br` | PS `/iefc/app/...` |
| `EMAIL_MAX_TENTATIVAS` | `app.email.max-tentativas` | ⚙️ | ➖ | `5` | PS `/iefc/app/...` |
| `SPRING_DATASOURCE_URL` | `spring.datasource.url` | ⚙️ | — | **Fixo** `jdbc:postgresql://10.20.2.10:5433/${EMAIL_DATABASE_NAME}` | — |
| `RABBITMQ_HOST` | `spring.rabbitmq.host` | ⚙️ | — | **Fixo** `rabbitmq` | — |

### 4.4 Host `iefc-backend` — container `rabbitmq`

| Variável | Tipo | Obrig. | Destino |
|----------|------|--------|---------|
| `RABBITMQ_DEFAULT_USER` ← `RABBITMQ_USER` | 🔒 | ✅ | (compartilhado, 4.2) |
| `RABBITMQ_DEFAULT_PASS` ← `RABBITMQ_PASS` | 🔒 | ✅ | (compartilhado, 4.2) |

### 4.5 Frontend (`iefc-institucional`), Nginx e Redis

- **Frontend:** nenhuma variável. É um site estático; não lê `import.meta.env` nem chama a API.
- **Nginx:** IPs dos frontends e do backend são gerados pelo Terraform no user data.
- **Redis:** sem autenticação; acessível só pela rede interna do Docker no host do backend.

---

## 5. Ambiente local (`iefc/docker-compose.yml` + `iefc/backend/.env.example`)

Usa as mesmas variáveis das seções 4.1–4.4, mais algumas exclusivas do ambiente local. Não precisam ir para o cofre.

| Variável | Uso | Padrão |
|----------|-----|--------|
| `IEFC_APP_PORT` | porta publicada da API | `8080` |
| `EMAIL_APP_PORT` | porta publicada do email-service | `8081` |
| `IEFC_WEB_PORT` | porta publicada do frontend (dev) | `3000` |
| `IEFC_DATABASE_HOST` / `EMAIL_DATABASE_HOST` | nome do serviço no Compose | `iefc-db` / `email-db` |
| `REDIS_HOST` / `RABBITMQ_HOST` | nome do serviço no Compose | `redis` / `rabbitmq` |

---

## 6. Proposta de organização no AWS

Agrupar os segredos por responsabilidade facilita a rotação e o controle de acesso. Os segredos ficam em **Secrets Manager** (JSON); as configurações não sensíveis, em **Parameter Store** (`String`, gratuito).

| Secret (Secrets Manager) | Chaves JSON | Lido por |
|--------------------------|-------------|----------|
| `iefc/database` | `IEFC_DATABASE_USERNAME`, `IEFC_DATABASE_PASSWORD`, `EMAIL_DATABASE_USERNAME`, `EMAIL_DATABASE_PASSWORD` | database, backend |
| `iefc/rabbitmq` | `RABBITMQ_USER`, `RABBITMQ_PASS` | backend |
| `iefc/mail` | `MAILTRAP_USERNAME`, `MAILTRAP_PASSWORD` | backend |
| `iefc/admin` | `APP_ADMIN_EMAIL`, `APP_ADMIN_SENHA`, `APP_USUARIO_PADRAO_EMAIL`, `APP_USUARIO_PADRAO_SENHA` | backend |
| `iefc/jwt` | `JWT_PRIVATE_KEY_PEM`, `JWT_PUBLIC_KEY_PEM` | backend |

| Parameter Store (`String`) | Valor sugerido |
|----------------------------|----------------|
| `/iefc/app/DOCKER_NAMESPACE` | `matheusdtjb` |
| `/iefc/app/FRONTEND_BASE_URL` | `http://<IP público do proxy>` (muda a cada subida) |
| `/iefc/app/RELATORIO_EMAIL_DESTINATARIO` | `admin@iefc.org.br` |
| `/iefc/app/EMAIL_SERVICE_REMETENTE` | `no-reply@iefc.org.br` |
| `/iefc/app/ADMIN_NOTIFICACAO_EMAIL` | `admin@iefc.org.br` |
| `/iefc/app/EMAIL_REMETENTE_PADRAO` | `no-reply@iefc.org.br` |
| `/iefc/app/EMAIL_MAX_TENTATIVAS` | `5` |
| `/iefc/database/IEFC_DATABASE_NAME` | `iefc_db` |
| `/iefc/database/EMAIL_DATABASE_NAME` | `email_db` |
| `/iefc/mail/MAILTRAP_HOST` | `sandbox.smtp.mailtrap.io` |
| `/iefc/mail/MAILTRAP_PORT` | `2525` |

**Total a cadastrar:** 5 segredos no Secrets Manager e 11 parâmetros no Parameter Store.

### Pontos de atenção para o AWS Academy

- **Custo:** Secrets Manager cobra cerca de US$ 0,40 por segredo por mês (cerca de US$ 2/mês para os 5), proporcional ao tempo de existência. Alternativa sem custo: tudo no Parameter Store, com os segredos como `SecureString`.
- **Permissões:** as EC2 usam `LabInstanceProfile` → `LabRole`. Antes de automatizar, confirme no lab que essa role consegue `secretsmanager:GetSecretValue` / `ssm:GetParameter(s)` (`aws secretsmanager list-secrets` e `aws ssm describe-parameters` numa sessão do Session Manager). O Academy não permite criar novas roles/policies.
- **Persistência:** os segredos **não** devem ser criados pelo Terraform desta pasta, porque o workflow de descida os apagaria (e o valor ficaria salvo no state). Crie-os uma vez, fora do Terraform (console ou CLI), como foi feito com o bucket de estado.
- **Próximo passo técnico (não implementado):** trocar a criação manual de `/opt/iefc/.env` e dos PEM por um script de bootstrap que leia o cofre na inicialização da instância.

---

## 7. Observações encontradas na análise

1. **Chave privada JWT versionada:** `iefc/backend/iefc-backend/src/main/resources/app.key` está no Git (o pipeline a remove da imagem). Gere um novo par para o cofre; o atual deve ser considerado comprometido.
2. **`APP_USUARIO_PADRAO_*` ausentes** de `deployment/backend.env.example` e `iefc/backend/.env.example`; são opcionais, mas não estão documentados.
3. **`APP_ADMIN_EMAIL`/`APP_ADMIN_SENHA` ausentes** de `iefc/backend/.env.example` (ambiente local), embora a API não suba sem a senha.
4. **Porta do banco de e-mail fixa em `5432`** no `application.properties` do email-service. Na AWS isso é contornado com `SPRING_DATASOURCE_URL` (porta `5433`); não existe `EMAIL_DATABASE_PORT`.
5. **CORS aberto** (`setAllowedOriginPatterns(List.of("*"))` em `SpringSecurityConfig`): não depende de variável. Se desejar restringir, `FRONTEND_BASE_URL` seria o candidato natural.
6. **Segredos expostos durante a configuração** (token do Docker Hub e credenciais do Academy colados no chat): revogar/rotacionar antes de cadastrar no cofre.

---

## 8. Checklist de coleta

- [ ] Gerar `IEFC_DATABASE_PASSWORD`
- [ ] Gerar `EMAIL_DATABASE_PASSWORD`
- [ ] Definir `RABBITMQ_USER` e gerar `RABBITMQ_PASS`
- [ ] Obter `MAILTRAP_USERNAME` / `MAILTRAP_PASSWORD` (Mailtrap → Email Testing → Inbox → SMTP Settings)
- [ ] Definir `APP_ADMIN_EMAIL` e `APP_ADMIN_SENHA`
- [ ] (Opcional) Definir `APP_USUARIO_PADRAO_EMAIL` / `APP_USUARIO_PADRAO_SENHA`
- [ ] Gerar novo par RSA 2048 do JWT (`openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048`)
- [ ] Revogar o token Docker Hub exposto, gerar outro e atualizar `MATHEUS_DOCKER_HUB_PERSONAL_ACESS_TOKEN`
- [ ] Criar as variables `IEFC_AWS_REGION`, `IEFC_AWS_ACCOUNT_ID`, `IEFC_TF_STATE_BUCKET` e `IEFC_AVAILABILITY_ZONE` no GitHub
- [ ] Criar o environment `aws-academy` com revisores e o secret `AWS_ACADEMY_CREDENTIALS`
- [ ] Verificar se a `LabRole` lê Secrets Manager / Parameter Store
