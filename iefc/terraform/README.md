# Infraestrutura IEFC para AWS Academy

Esta configuração cria uma VPC em uma única zona, sub-redes pública e privada, internet gateway, NAT gateway e cinco instâncias EC2. A sub-rede pública contém Nginx e duas instâncias do frontend; a privada contém a API, o serviço de e-mail, Redis e RabbitMQ em uma instância, e dois bancos PostgreSQL em outra. Veja o [diagrama editável](architecture.drawio), com duas páginas: **N1 - Visão geral** (fluxo principal) e **N2 - Detalhado** (CIDRs, IPs, tipos de instância, security groups, route tables, containers e integrações com GitHub Actions, Docker Hub, S3, Secrets Manager e Systems Manager).

**Somente laboratório:** o acesso público usa HTTP sem criptografia. Não use credenciais reais nem dados pessoais. O proxy, o backend e o banco são instâncias únicas, sem alta disponibilidade. O frontend existente ainda não está integrado à API; a infraestrutura não corrige essa integração. O NAT gateway, os IPs públicos, as EC2 e os volumes podem gerar custos mesmo quando não há acesso à aplicação.

**A destruição apaga os volumes das instâncias, os dados dos bancos e os segredos `iefc/app-env` e `iefc/jwt` (sem período de recuperação).** Faça backup e teste a restauração antes de destruir ou substituir a instância. Um novo par JWT é gerado a cada criação; tokens emitidos antes deixam de valer.

## Pré-requisitos

1. Confirme no laboratório as permissões e cotas para VPC, EC2, EIP, NAT gateway, S3, Secrets Manager, Session Manager e uso do perfil de instância `LabInstanceProfile`. O perfil (role `LabRole`) precisa permitir o registro no Systems Manager e `secretsmanager:GetSecretValue`; o usuário administrador também precisa de permissão para abrir sessões. As restrições variam entre laboratórios.
2. Crie previamente, fora desta configuração, um bucket S3 na região escolhida. Habilite versionamento, criptografia e acesso restrito. Nunca destrua esse bucket junto com a infraestrutura: ele guarda o estado. A identidade usada no workflow precisa ler, gravar e listar o bucket, e criar e remover o arquivo de lock `.tflock` no prefixo `iefc/`. Estado e planos podem conter informações sensíveis.
3. Publique as três tags `*-latest` no repositório público `fluxy` do Docker Hub usando o workflow de imagens. Elas precisam ser públicas para o bootstrap automático; imagens privadas exigem autenticação Docker nas instâncias e outro processo de bootstrap. Imagens novas não são implantadas automaticamente em instâncias existentes. Alterações em user data também não são executadas novamente automaticamente; planeje a atualização ou substituição das instâncias, preservando antes os dados e as chaves.
4. Faça merge destes arquivos em `main`. Configure o ambiente GitHub `aws-academy` com revisores obrigatórios e restrição de branch para `main`, se o plano e a visibilidade do repositório oferecerem esses recursos. O workflow solicita aprovação separadamente para planejar e aplicar; revise o plano nos logs antes de aprovar a aplicação. **Se a proteção não estiver disponível ou configurada, não execute esses workflows:** a aplicação ocorrerá automaticamente após o plano. Nesse caso, execute o Terraform manualmente com revisão independente.

Em **Settings > Secrets and variables > Actions**, configure as seguintes variáveis de repositório:

| Variável | Valor |
|---|---|
| `IEFC_AWS_REGION` | Região permitida, por exemplo `us-east-1` |
| `IEFC_AVAILABILITY_ZONE` | Zona permitida na mesma região, por exemplo `us-east-1a` |
| `IEFC_AWS_ACCOUNT_ID` | ID de 12 dígitos da conta do laboratório |
| `IEFC_TF_STATE_BUCKET` | Nome do bucket S3 de estado criado separadamente |

Configure o secret `AWS_ACADEMY_CREDENTIALS` colando o bloco completo exibido em **AWS Details > AWS CLI > Show** (as linhas `aws_access_key_id`, `aws_secret_access_key` e `aws_session_token`; o cabeçalho `[default]` é opcional). O workflow separa os três valores, os mascara nos logs e os exporta como `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` e `AWS_SESSION_TOKEN`. Configure também `MATHEUS_DOCKER_HUB_USERNAME` com o namespace das imagens e o secret `IEFC_APP_ENV` com as configurações da aplicação, no formato de [app.env.example](deployment/app.env.example) (uma linha `CHAVE=valor` por variável). Não compartilhe credenciais no chat, no Git ou em arquivos Terraform. Atualize o secret a cada nova sessão do laboratório; se as credenciais expirarem antes da aprovação, cancele a execução e gere um novo plano. Limite quem pode executar workflows, acessar logs e baixar artefatos: o plano salvo fica disponível como artefato por um dia.

## Criar a infraestrutura

Execute **Actions > IEFC infrastructure up** na branch `main`. Aprove o job de planejamento, confira conta, recursos, custos e alterações nos logs e, somente então, aprove o job de aplicação. O segundo job aplica o mesmo plano salvo; alterações no estado entre plano e aplicação causam falha e exigem novo plano. Os outputs incluem `application_url` e `instance_ids`.

O bootstrap das instâncias leva alguns minutos. Se uma instância não aparecer como gerenciada no Systems Manager, verifique perfil, permissões, conectividade, status do agente e `/var/log/cloud-init-output.log` pelo acesso permitido pelo laboratório.

## Segredos e configuração dos serviços privados

A configuração é automática:

1. O Terraform lê `IEFC_APP_ENV`, valida as chaves obrigatórias e grava tudo como JSON no segredo `iefc/app-env` do AWS Secrets Manager, acrescentando `DOCKER_NAMESPACE` e `FRONTEND_BASE_URL` (`http://<IP público do proxy>`).
2. O Terraform gera um par RSA 2048 para o JWT e o grava em `iefc/jwt`.
3. No boot, o serviço `iefc-secrets.service` de cada instância privada executa `/usr/local/bin/iefc-load-secrets`, que lê os segredos com as credenciais da `LabRole`:
   - **database:** grava em `/opt/iefc/.env` somente as seis variáveis `IEFC_DATABASE_*` e `EMAIL_DATABASE_*`;
   - **backend:** grava todas as variáveis em `/opt/iefc/.env` e as chaves JWT em `/opt/iefc/secrets/`.
4. Em seguida, `iefc.service` sobe os containers com Docker Compose. Se o segredo ainda não existir, o carregador tenta novamente a cada 10 segundos por até 15 minutos.

Nenhum valor secreto vai para user data, logs ou Git. Os valores ficam no estado do Terraform (bucket S3 criptografado) e no artefato do plano, que expira em um dia.

Para alterar uma configuração sem recriar o ambiente, atualize o segredo no Secrets Manager (console ou `aws secretsmanager put-secret-value`) e, como root na instância, execute `systemctl restart iefc-secrets.service && systemctl restart iefc.service`. A próxima execução do workflow de subida sobrescreve o segredo com o conteúdo de `IEFC_APP_ENV`.

Para diagnóstico, consulte `journalctl -u iefc-secrets.service`, `journalctl -u iefc.service` e os logs dos containers sem expor credenciais.

## Destruir

Depois de verificar backups e custos, execute **Actions > IEFC infrastructure down** na branch `main` e informe `DESTROY`. Revise o plano de destruição e aprove apenas se concordar com a perda dos volumes e dados. O bucket de estado permanece fora do gerenciamento desta configuração. Não altere o bucket, a chave de estado ou a conta entre as operações; ambos os workflows usam `iefc/terraform.tfstate` e um lock S3 compartilhado.

## Alternativa manual

Se não houver aprovação protegida disponível no GitHub, use uma sessão autenticada no CloudShell e revisão independente. Defina e exporte `STATE_BUCKET`, `AWS_REGION`, `TF_VAR_aws_account_id`, `TF_VAR_availability_zone`, `TF_VAR_docker_namespace` e `TF_VAR_app_env` (por exemplo, `export TF_VAR_app_env="$(cat app.env)"` a partir de um arquivo local fora do Git) no shell com os mesmos valores usados pelo projeto; `TF_VAR_aws_region` deve ser igual a `AWS_REGION` e também deve ser exportada. Não inclua credenciais nesses comandos. No diretório `iefc/terraform`, execute:

```bash
aws sts get-caller-identity
terraform init -lockfile=readonly -backend-config="bucket=$STATE_BUCKET" -backend-config="key=iefc/terraform.tfstate" -backend-config="region=$AWS_REGION" -backend-config="use_lockfile=true" -backend-config="encrypt=true"
terraform plan -out=iefc.tfplan
terraform show iefc.tfplan
```

Confirme a conta e peça a outra pessoa que revise o plano e os custos. Somente após aprovação, execute `terraform apply iefc.tfplan`. Para destruir, gere um novo plano com `terraform plan -destroy -out=iefc.tfplan`, revise a perda dos dados e aplique esse plano. O arquivo de plano contém informações potencialmente sensíveis e deve ser removido de forma apropriada após o uso.

## Validação local

Com Terraform 1.10.5, execute `terraform init -backend=false`, `terraform fmt -check -recursive`, `terraform validate` e `terraform test` neste diretório. O teste usa um provider simulado, não cria recursos e não verifica permissões, bootstrap ou conectividade AWS. A aplicação real ainda precisa ser validada no laboratório, inclusive os containers, Session Manager e proxy.