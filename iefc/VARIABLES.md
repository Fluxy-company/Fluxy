# Inventário de variáveis e segredos — IEFC

Levantamento de todas as variáveis necessárias para executar a IEFC (pipelines, Terraform e aplicação) e de onde cada uma fica guardada.

**Situação atual:** as variáveis da aplicação são informadas no secret do GitHub `IEFC_APP_ENV`. O workflow de subida as grava no **AWS Secrets Manager** (`iefc/app-env`), gera o par JWT (`iefc/jwt`), e as EC2 privadas leem os dois segredos no boot. Detalhes na seção 6.

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
- **Destino**: **SM** = AWS Secrets Manager (criado pelo Terraform) · **GH** = GitHub · **TF** = calculado pelo Terraform · **Fixo** = já definido no código/Compose; não precisa coletar

---

## 1. Resumo — o que você precisa reunir

| # | Item | Tipo | Quem usa | Onde fica |
|---|------|------|----------|-----------|
| 1 | Usuários e senhas dos bancos (`IEFC_DATABASE_*`, `EMAIL_DATABASE_*`) | 🔒 | bancos, iefc-api, email-service | GH `IEFC_APP_ENV` → SM `iefc/app-env` |
| 2 | Usuário e senha do RabbitMQ (`RABBITMQ_USER`, `RABBITMQ_PASS`) | 🔒 | rabbitmq, iefc-api, email-service | GH `IEFC_APP_ENV` → SM `iefc/app-env` |
| 3 | Usuário e senha SMTP do Mailtrap (`MAILTRAP_USERNAME`, `MAILTRAP_PASSWORD`) | 🔒 | email-service | GH `IEFC_APP_ENV` → SM `iefc/app-env` |
| 4 | E-mail e senha do administrador inicial (`APP_ADMIN_EMAIL`, `APP_ADMIN_SENHA`) | 🔒 | iefc-api | GH `IEFC_APP_ENV` → SM `iefc/app-env` |
| 5 | (Opcional) usuário padrão, `EMAIL_SERVICE_API_KEY` e e-mails de remetente/destinatário | 🔒/⚙️ | iefc-api, email-service | GH `IEFC_APP_ENV` → SM `iefc/app-env` |
| 6 | Par de chaves RSA do JWT | 🔒 | iefc-api | TF gera → SM `iefc/jwt` |
| 7 | `DOCKER_NAMESPACE` e `FRONTEND_BASE_URL` | ⚙️ | Compose, iefc-api | TF calcula → SM `iefc/app-env` |
| 8 | Token do Docker Hub (novo, após revogar o exposto) | 🔒 | GitHub Actions | GH secret |
| 9 | Credenciais do AWS Academy (a cada sessão) | 🔒 | GitHub Actions | GH environment secret |

Os itens 1–5 são coletados uma vez e colados juntos em `IEFC_APP_ENV` (modelo: `iefc/terraform/deployment/app.env.example`). Os itens 6–7 são automáticos. Os itens 8–9 são do **pipeline** e ficam no GitHub: são as credenciais usadas para chegar até o AWS.

---

## 2. GitHub Actions (permanecem no GitHub)

### Secrets

| Nome | Tipo | Obrig. | Usado em | Observação |
|------|------|--------|----------|------------|
| `MATHEUS_DOCKER_HUB_USERNAME` | ⚙️ | ✅ | `iefc-images.yml` (login e tags), `iefc-terraform.yml` (`TF_VAR_docker_namespace`) | Valor: `matheusdtjb`. Não é sensível; poderia virar *variable*. |
| `MATHEUS_DOCKER_HUB_PERSONAL_ACESS_TOKEN` | 🔒 | ✅ | `iefc-images.yml` (push) | Grafia `ACESS` é intencional. Permissão *Read & Write* no repo `fluxy`. **O token atual foi exposto no chat: revogar e gerar outro.** |
| `AWS_ACADEMY_CREDENTIALS` | 🔒 | ✅ | `iefc-terraform.yml` (plan e apply) | Bloco completo de *AWS Details → AWS CLI → Show*. Guardar como secret do **ambiente** `aws-academy`. Expira a cada sessão do lab (~4 h). |
| `IEFC_APP_ENV` | 🔒 | ✅ | `iefc-terraform.yml` (`TF_VAR_app_env`) | Bloco `CHAVE=valor`, uma variável por linha (seções 4.1–4.4). Modelo: `iefc/terraform/deployment/app.env.example`. Também é exigido pelo workflow de descida. |

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

Todas são preenchidas pelo workflow a partir dos secrets e variables acima. Só `app_env` é segredo (marcada como `sensitive`).

| Variável Terraform | Origem | Obrig. | Padrão |
|--------------------|--------|--------|--------|
| `aws_region` | `IEFC_AWS_REGION` | ➖ | `us-east-1` |
| `aws_account_id` | `IEFC_AWS_ACCOUNT_ID` | ✅ | — |
| `availability_zone` | `IEFC_AVAILABILITY_ZONE` | ✅ | — |
| `docker_namespace` | `MATHEUS_DOCKER_HUB_USERNAME` | ✅ | — |
| `app_env` | `IEFC_APP_ENV` | ✅ | — (valida as 12 chaves obrigatórias) |
| `instance_profile` | padrão | ➖ | `LabInstanceProfile` |
| `web_instance_type` | padrão | ➖ | `t3.micro` |
| `backend_instance_type` | padrão | ➖ | `t3.medium` |
| `database_instance_type` | padrão | ➖ | `t3.small` |
| backend S3: `bucket`, `key`, `region` | `IEFC_TF_STATE_BUCKET`, fixo `iefc/terraform.tfstate`, `IEFC_AWS_REGION` | ✅ | — |

---

## 4. Aplicação na AWS (instâncias EC2)

Esses valores vêm do segredo `iefc/app-env` e são gravados automaticamente em `/opt/iefc/.env` no boot por `iefc-secrets.service`. O host `iefc-database` recebe só as seis variáveis de banco; o `iefc-backend` recebe todas, além das chaves JWT de `iefc/jwt` em `/opt/iefc/secrets/`.

### 4.1 Host `iefc-database` (`deployment/database.yml`)

| Variável | Tipo | Obrig. | Valor sugerido | Destino |
|----------|------|--------|----------------|---------|
| `IEFC_DATABASE_NAME` | ⚙️ | ✅ | `iefc_db` | SM `iefc/app-env` |
| `IEFC_DATABASE_USERNAME` | ⚙️ | ✅ | `iefc_user` | SM `iefc/app-env` |
| `IEFC_DATABASE_PASSWORD` | 🔒 | ✅ | gerar (ex.: 32 caracteres alfanuméricos) | SM `iefc/app-env` |
| `EMAIL_DATABASE_NAME` | ⚙️ | ✅ | `email_db` | SM `iefc/app-env` |
| `EMAIL_DATABASE_USERNAME` | ⚙️ | ✅ | `email_user` | SM `iefc/app-env` |
| `EMAIL_DATABASE_PASSWORD` | 🔒 | ✅ | gerar | SM `iefc/app-env` |

> Os mesmos seis valores precisam ser **idênticos** no host do backend.

### 4.2 Host `iefc-backend` — container `iefc-api` (`deployment/backend.yml`)

| Variável | Propriedade Spring | Tipo | Obrig. | Padrão / valor | Destino |
|----------|--------------------|------|--------|----------------|---------|
| `DOCKER_NAMESPACE` | — (Compose) | ⚙️ | ✅ | `matheusdtjb` | TF → SM `iefc/app-env` |
| `IEFC_DATABASE_NAME` | `spring.datasource.url` | ⚙️ | ✅ | `iefc_db` | (compartilhado, 4.1) |
| `IEFC_DATABASE_USERNAME` | `spring.datasource.username` | ⚙️ | ✅ | `iefc_user` | (compartilhado, 4.1) |
| `IEFC_DATABASE_PASSWORD` | `spring.datasource.password` | 🔒 | ✅ | — | (compartilhado, 4.1) |
| `RABBITMQ_USER` | `spring.rabbitmq.username` | 🔒 | ✅ | — | SM `iefc/app-env` |
| `RABBITMQ_PASS` | `spring.rabbitmq.password` | 🔒 | ✅ | — | SM `iefc/app-env` |
| `APP_ADMIN_EMAIL` | `app.admin.email` | 🔒 | ✅ | — | SM `iefc/app-env` |
| `APP_ADMIN_SENHA` | `app.admin.senha` | 🔒 | ✅ | — (a API **não sobe** sem ela: `IllegalStateException` em `AdminUserConfig`) | SM `iefc/app-env` |
| `APP_USUARIO_PADRAO_EMAIL` | `app.usuario-padrao.email` | 🔒 | ➖ | vazio (pula a criação) | SM `iefc/app-env` |
| `APP_USUARIO_PADRAO_SENHA` | `app.usuario-padrao.senha` | 🔒 | ➖ | vazio | SM `iefc/app-env` |
| `FRONTEND_BASE_URL` | `app.frontend.base-url` | ⚙️ | ➖ | `http://<IP público do proxy>` (calculado; pode ser sobrescrito em `IEFC_APP_ENV`) | TF → SM `iefc/app-env` |
| `RELATORIO_EMAIL_DESTINATARIO` | `app.relatorio.email.destinatario` | ⚙️ | ➖ | `admin@iefc.org.br` | SM `iefc/app-env` |
| `EMAIL_SERVICE_REMETENTE` | `app.email-service.remetente` | ⚙️ | ➖ | `no-reply@iefc.org.br` | SM `iefc/app-env` |
| `ADMIN_NOTIFICACAO_EMAIL` | `app.email-service.admin-destinatario` | ⚙️ | ➖ | `admin@iefc.org.br` | SM `iefc/app-env` |
| `JWT_PRIVATE_KEY` | `jwt.private.key` | 🔒 | ✅ | `file:/run/secrets/jwt-private.pem` (arquivo montado) | TF gera → SM `iefc/jwt` (`private_key_pem`) |
| `JWT_PUBLIC_KEY` | `jwt.public.key` | ⚙️ | ✅ | `file:/run/secrets/jwt-public.pem` | TF gera → SM `iefc/jwt` (`public_key_pem`) |
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
| `MAILTRAP_HOST` | `spring.mail.host` | ⚙️ | ➖ | `sandbox.smtp.mailtrap.io` | SM `iefc/app-env` |
| `MAILTRAP_PORT` | `spring.mail.port` | ⚙️ | ➖ | `2525` | SM `iefc/app-env` |
| `MAILTRAP_USERNAME` | `spring.mail.username` | 🔒 | ✅ | — | SM `iefc/app-env` |
| `MAILTRAP_PASSWORD` | `spring.mail.password` | 🔒 | ✅ | — | SM `iefc/app-env` |
| `EMAIL_REMETENTE_PADRAO` | `app.email.remetente-padrao` | ⚙️ | ➖ | `no-reply@iefc.org.br` | SM `iefc/app-env` |
| `EMAIL_MAX_TENTATIVAS` | `app.email.max-tentativas` | ⚙️ | ➖ | `5` | SM `iefc/app-env` |
| `EMAIL_SERVICE_API_KEY` | — | 🔒 | ➖ | não lido pelo código atual; armazenado para uso futuro | SM `iefc/app-env` |
| `EMAIL_SERVICE_BASE_URL` | — | ⚙️ | ➖ | `http://email-service:8080`; não lido pelo código atual | SM `iefc/app-env` |
| `SPRING_DATASOURCE_URL` | `spring.datasource.url` | ⚙️ | — | **Fixo** `jdbc:postgresql://10.20.2.10:5433/${EMAIL_DATABASE_NAME}` | — |
| `RABBITMQ_HOST` | `spring.rabbitmq.host` | ⚙️ | — | **Fixo** `rabbitmq` | — |

### 4.4 Host `iefc-backend` — container `rabbitmq`

| Variável | Tipo | Obrig. | Destino |
|----------|------|--------|---------|
| `RABBITMQ_DEFAULT_USER` ← `RABBITMQ_USER` | 🔒 | ✅ | (compartilhado, 4.2) |
| `RABBITMQ_DEFAULT_PASS` ← `RABBITMQ_PASS` | 🔒 | ✅ | (compartilhado, 4.2) |

### 4.5 Frontend (`iefc-institucional`), Nginx e Redis

- **Frontend:** nenhuma variável em uso. É um site estático; não lê `import.meta.env` nem chama a API. `VITE_API_URL` só teria efeito **no build da imagem** (o Vite embute o valor no JavaScript), não em tempo de execução; por isso não é lido do Secrets Manager. Na AWS, o valor adequado seria o caminho relativo `/api/v1`, já roteado pelo Nginx.
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

## 6. Organização no AWS (implementada)

| Segredo (Secrets Manager) | Conteúdo | Criado por | Lido por |
|---------------------------|----------|-----------|----------|
| `iefc/app-env` | JSON com todas as chaves de `IEFC_APP_ENV` + `DOCKER_NAMESPACE` + `FRONTEND_BASE_URL` | `secrets.tf` (`aws_secretsmanager_secret_version.app_env`) | `iefc-database` (só `IEFC_DATABASE_*`/`EMAIL_DATABASE_*`) e `iefc-backend` (todas) |
| `iefc/jwt` | JSON `private_key_pem` (PKCS#8) e `public_key_pem` | `secrets.tf` (`tls_private_key.jwt`) | `iefc-backend` |

Fluxo:

1. GitHub secret `IEFC_APP_ENV` → `TF_VAR_app_env` → o Terraform valida as chaves obrigatórias e converte o bloco em JSON.
2. O Terraform cria os dois segredos com `recovery_window_in_days = 0`; o workflow de descida os apaga na hora, permitindo recriar com o mesmo nome.
3. No boot, `iefc-secrets.service` executa `/usr/local/bin/iefc-load-secrets` com as credenciais da `LabRole` (via `LabInstanceProfile`), grava `/opt/iefc/.env` (`600`) e os PEM (`444`, em diretório só de root), e então `iefc.service` sobe os containers.
4. Se o segredo ainda não tiver valor (o `FRONTEND_BASE_URL` depende do IP do proxy, criado depois do backend), o carregador tenta de novo a cada 10 s por até 15 min.

### Pontos de atenção para o AWS Academy

- **Custo:** o Secrets Manager cobra cerca de US$ 0,40 por segredo por mês (≈ US$ 0,80/mês para os 2), proporcional ao tempo em que existem; eles são apagados na descida.
- **Permissões:** depende de a `LabRole` ter `secretsmanager:GetSecretValue`. O Academy não permite criar roles/policies novas.
- **Estado:** os valores ficam no estado do Terraform (S3 criptografado) e no artefato do plano (1 dia). Aceitável para dados de laboratório; para produção, crie os segredos fora do Terraform e passe apenas os nomes.
- **Alteração sem recriar:** altere o segredo no console e execute `systemctl restart iefc-secrets.service && systemctl restart iefc.service` na instância. A próxima subida sobrescreve com `IEFC_APP_ENV`.

---

## 7. Observações encontradas na análise

1. **Chave privada JWT versionada:** `iefc/backend/iefc-backend/src/main/resources/app.key` está no Git (o pipeline a remove da imagem). Na AWS ela não é usada: o Terraform gera um par novo em `iefc/jwt`.
2. **`APP_USUARIO_PADRAO_*` ausentes** de `iefc/backend/.env.example`; são opcionais e já constam em `deployment/app.env.example`.
3. **`APP_ADMIN_EMAIL`/`APP_ADMIN_SENHA` ausentes** de `iefc/backend/.env.example` (ambiente local), embora a API não suba sem a senha.
4. **Porta do banco de e-mail fixa em `5432`** no `application.properties` do email-service. Na AWS isso é contornado com `SPRING_DATASOURCE_URL` (porta `5433`); não existe `EMAIL_DATABASE_PORT`.
5. **CORS aberto** (`setAllowedOriginPatterns(List.of("*"))` em `SpringSecurityConfig`): não depende de variável. Se desejar restringir, `FRONTEND_BASE_URL` seria o candidato natural.
6. **Segredos expostos durante a configuração** (token do Docker Hub e credenciais do Academy colados no chat): revogar/rotacionar. Os valores de `IEFC_APP_ENV` também foram compartilhados no chat; são apenas de teste, mas não os reutilize em outro ambiente.
7. **Variáveis sem uso no código atual:** `EMAIL_SERVICE_API_KEY`, `EMAIL_SERVICE_BASE_URL` e `VITE_API_URL` não aparecem no código desta branch. As duas primeiras são armazenadas mesmo assim; `VITE_API_URL` é de build do frontend (ver 4.5).

---

## 8. Checklist

- [ ] Preencher o secret `IEFC_APP_ENV` a partir de `iefc/terraform/deployment/app.env.example` (bancos, RabbitMQ, Mailtrap, admin e opcionais)
- [ ] Revogar o token Docker Hub exposto, gerar outro e atualizar `MATHEUS_DOCKER_HUB_PERSONAL_ACESS_TOKEN`
- [ ] Criar as variables `IEFC_AWS_REGION`, `IEFC_AWS_ACCOUNT_ID`, `IEFC_TF_STATE_BUCKET` e `IEFC_AVAILABILITY_ZONE` no GitHub
- [ ] Criar o environment `aws-academy` com revisores e o secret `AWS_ACADEMY_CREDENTIALS`
