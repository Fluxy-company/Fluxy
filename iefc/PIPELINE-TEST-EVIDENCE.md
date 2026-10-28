# Evidências de teste local dos pipelines IEFC

Data: 27/10/2026 (15:43–16:04, horário de Brasília)
Commit base: `c0c1ee1` (branch `main`), com os workflows de `.github/workflows/` na versão atual do working tree.

## Como foi testado

Os workflows foram executados localmente com [`act`](https://github.com/nektos/act) 0.2.89, que roda os jobs do GitHub Actions em containers Docker:

| Item | Valor |
|------|-------|
| Host | Windows + WSL2 Ubuntu 26.04 |
| Docker Engine | 29.1.3 (buildx 0.30.1) |
| Imagem do runner | `catthehacker/ubuntu:act-latest` (equivalente a `ubuntu-latest`) |
| Terraform / AWS CLI (verificação externa) | 1.10.5 / 2.38.6 |
| Conta AWS | AWS Academy `379094220937`, role `voclabs`, região `us-east-1` |
| Docker Hub | `matheusdtjb/fluxy` |

Comandos usados (segredos passados por arquivo/variável de ambiente, nunca na linha de comando):

```bash
act push              -W .github/workflows/iefc-images.yml         --secret-file <arquivo>
act workflow_dispatch -W .github/workflows/iefc-terraform-up.yml   --secret-file <arquivo> -s AWS_ACADEMY_CREDENTIALS --var IEFC_...
act workflow_dispatch -W .github/workflows/iefc-terraform-down.yml --input confirmation=DESTROY (mesmos secrets/vars)
```

Variáveis usadas: `IEFC_AWS_REGION=us-east-1`, `IEFC_AWS_ACCOUNT_ID=379094220937`, `IEFC_TF_STATE_BUCKET=iefc-tfstate-379094220937`, `IEFC_AVAILABILITY_ZONE=us-east-1a`.

---

## 1. Workflow de imagens (`iefc-images.yml`) — ✅ SUCESSO

Evento simulado: `push` em `main` (inclui a etapa de publicação).

| Job | Resultado |
|-----|-----------|
| Test iefc-backend (`mvn verify`) | ✅ `Tests run: 106, Failures: 0, Errors: 0, Skipped: 0` — BUILD SUCCESS |
| Test email-service (`mvn verify`) | ✅ `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0` — BUILD SUCCESS |
| Build frontend (`npm ci` + `npm run build`) | ✅ |
| Build iefc-backend image (target `prod`) | ✅ |
| Build email-service image (target `prod`) | ✅ |
| Build iefc-institucional image (target `prod`) | ✅ |
| Publish iefc-backend image | ✅ `Login Succeeded!` + push |
| Publish email-service image | ✅ push |
| Publish iefc-institucional image | ✅ push |

Total: **9 jobs com sucesso, 0 falhas.**

### Tags publicadas (confirmadas pela API pública do Docker Hub)

| Tag em `matheusdtjb/fluxy` | Tamanho | Digest |
|----------------------------|---------|--------|
| `iefc-backend-latest` | 313,9 MB | `sha256:4107e3e37d1a57138dfdf67e47f35894c815f65c416642b2c35a9ddb7b2f71e0` |
| `iefc-backend-sha-c0c1ee1343587610046ca88dcc2179d6ea477527` | 313,9 MB | (mesmo digest) |
| `email-service-latest` | 265,6 MB | `sha256:f735706597b1d15b3c0ec505dbfbaacd38b4d39cc64ce3001b915a09c7ca08ba` |
| `email-service-sha-c0c1ee1343587610046ca88dcc2179d6ea477527` | 265,6 MB | (mesmo digest) |
| `iefc-institucional-latest` | 24,9 MB | `sha256:81787e89441145114cbb87eece461fdc85da856fc70855b3714470f46cdd854d` |
| `iefc-institucional-sha-c0c1ee1343587610046ca88dcc2179d6ea477527` | 24,9 MB | (mesmo digest) |

Trecho do log:

```text
[Publish iefc-backend image-1] #23 pushing manifest for docker.io/***/fluxy:iefc-backend-latest@sha256:4107e3e3... 2.8s done
[Publish email-service image-2] #22 pushing manifest for docker.io/***/fluxy:email-service-latest@sha256:f7357065... 3.2s done
[Publish iefc-institucional image-3] #20 pushing manifest for docker.io/***/fluxy:iefc-institucional-latest@sha256:81787e89... 2.9s done
```

### Chave privada fora da imagem

A imagem publicada `iefc-backend-latest` foi baixada e inspecionada:

```text
jar: /app/app.jar
app.key entries in jar: 0
app.pub entries in jar: 1
app.key files in filesystem: 0
```

---

## 2. Workflow de subida (`iefc-terraform-up.yml`) — ✅ SUCESSO

| Job `plan` | Job `apply` |
|------------|-------------|
| ✅ checkout | ✅ checkout |
| ✅ Load AWS Academy credentials (secret único, valores mascarados `***`) | ✅ Load AWS Academy credentials |
| ✅ Ensure AWS CLI is available | ✅ Ensure AWS CLI is available |
| ✅ setup-terraform 1.10.5 | ✅ setup-terraform 1.10.5 |
| ✅ Check account and configuration (conta = 379094220937) | ✅ Verify AWS account |
| ✅ Initialize remote state (S3) | ✅ Initialize remote state (S3) |
| ✅ Validate and create plan — `terraform test`: `1 passed, 0 failed` | ✅ download-artifact (plano revisado) |
| ✅ upload-artifact | ✅ Apply reviewed plan (2m48s) |

```text
Plan: 19 to add, 0 to change, 0 to destroy.
Apply complete! Resources: 19 added, 0 changed, 0 destroyed.
```

### Recursos criados (verificados de forma independente com AWS CLI)

| Recurso | ID | Estado / detalhe |
|---------|----|------------------|
| VPC | `vpc-0cae2c3638113e9f6` | `available`, 10.20.0.0/16 |
| Sub-rede pública | `subnet-04dfeceeff36a7db5` | 10.20.1.0/24, us-east-1a, IP público automático |
| Sub-rede privada | `subnet-06421c6b72163d6d0` | 10.20.2.0/24, us-east-1a, sem IP público |
| Internet Gateway | `igw-04d9f1fcd9f45fcfb` | `available` (anexado) |
| Elastic IP (NAT) | `eipalloc-06260c0f01577f6c0` | 54.210.35.244 |
| NAT Gateway | `nat-0dfc1e7c844a10e35` | `available`, na sub-rede pública |
| Route table pública | `rtb-0ae4dd758a272967c` | 0.0.0.0/0 → `igw-04d9f1fcd9f45fcfb` |
| Route table privada | `rtb-06f603ddb3cb8fb03` | 0.0.0.0/0 → `nat-0dfc1e7c844a10e35` |
| Associações de rota | `rtbassoc-001ea103cafab9b41`, `rtbassoc-0fa9d8633f4acd910` | pública / privada |
| SG iefc-proxy | `sg-01c45277449bd6018` | entrada 80 |
| SG iefc-frontend | `sg-0cc4a5d9a618e2bcb` | entrada 80 (somente do proxy) |
| SG iefc-backend | `sg-0f78971ed48aeb1f1` | entrada 8080 (somente do proxy) |
| SG iefc-database | `sg-0a2109496110f6ff3` | entrada 5432–5433 (somente do backend) |
| EC2 iefc-nginx-proxy | `i-07fc76dbc17d25614` | t3.micro, `running`, 10.20.1.175 / 44.201.43.47 |
| EC2 iefc-frontend-1 | `i-0c126edcd9bcad1ae` | t3.micro, `running`, 10.20.1.11 |
| EC2 iefc-frontend-2 | `i-0fb49d7566394aa7a` | t3.micro, `running`, 10.20.1.12 |
| EC2 iefc-backend | `i-00fa185bdb2b05dd2` | t3.medium, `running`, 10.20.2.11, sem IP público |
| EC2 iefc-database | `i-04d07385a0099e0bb` | t3.small, `running`, 10.20.2.10, sem IP público |

Todas as instâncias com o perfil `LabInstanceProfile`.

### Testes funcionais com a infraestrutura no ar

| Teste | Resultado |
|-------|-----------|
| `GET http://44.201.43.47/` (Nginx → frontends) | ✅ HTTP 200, `Server: nginx/1.30.6`, título `IEFC · Instituto Educacional Futuro da Ciência` |
| 6 requisições seguidas ao proxy | ✅ 6× HTTP 200 |
| Containers nos frontends (via Systems Manager, sem SSH) | ✅ `iefc-frontend matheusdtjb/fluxy:iefc-institucional-latest Up 2 minutes` + `local http 200` nas **duas** instâncias |
| Acesso direto a `http://<IP público do frontend-1>/` | ✅ bloqueado (timeout) — o SG só aceita tráfego do proxy |
| `GET /api/` pelo proxy | ⚠️ HTTP 502 — esperado: o backend só sobe após configurar `.env` e chaves JWT via Session Manager (ver `iefc/terraform/README.md`) |

---

## 3. Workflow de descida (`iefc-terraform-down.yml`, `confirmation=DESTROY`) — ✅ SUCESSO

Todos os passos dos jobs `plan` e `apply` com sucesso (mesma sequência da subida; apply em 2m13s).

```text
Plan: 0 to add, 0 to change, 19 to destroy.
Apply complete! Resources: 0 added, 0 changed, 19 destroyed.
```

### Verificação pós-destruição (AWS CLI)

| Verificação | Resultado |
|-------------|-----------|
| VPC `vpc-0cae2c3638113e9f6` | 0 encontradas ✅ |
| EC2 não terminadas na VPC | 0 ✅ |
| As 5 instâncias | `terminated` ✅ |
| NAT Gateway | `deleted` ✅ |
| Elastic IP | 0 encontrados ✅ |
| Internet Gateway | 0 encontrados ✅ |
| Security groups da VPC | 0 ✅ |
| Recursos no estado Terraform (S3) | 0 ✅ |
| Instâncias pré-existentes da conta (fora do projeto) | 2, intactas ✅ |

---

## Problemas encontrados e corrigidos durante o teste

1. **Lock file do Terraform só tinha checksum para Windows** — falharia também no GitHub (`cached package ... does not match any of the checksums`). Corrigido com `terraform providers lock -platform=linux_amd64 -platform=windows_amd64 -platform=darwin_arm64`.
2. **Runner sem `mvn` / `aws`** — o `ubuntu-latest` do GitHub já tem ambos, mas o runner local não. Adicionados os passos `Ensure Maven is available` e `Ensure AWS CLI is available`, que só instalam se a ferramenta não existir (no GitHub não fazem nada).

## Limitações do teste local

- O `act` não aplica regras de proteção de ambiente (revisores obrigatórios do ambiente `aws-academy`); a aprovação manual só pode ser validada no GitHub.
- O bucket de estado `iefc-tfstate-379094220937` (versionado, criptografado, acesso público bloqueado) foi criado previamente e **não** é removido pela descida.
- A API (`/api/`) não foi testada de ponta a ponta porque depende de segredos configurados manualmente nas instâncias privadas.
