variable "app_env" {
  description = "Application settings in dotenv format (one KEY=VALUE per line). Stored in Secrets Manager and loaded by the private hosts at boot."
  type        = string
  sensitive   = true

  validation {
    condition = length(setsubtract(
      ["IEFC_DATABASE_NAME", "IEFC_DATABASE_USERNAME", "IEFC_DATABASE_PASSWORD", "EMAIL_DATABASE_NAME", "EMAIL_DATABASE_USERNAME", "EMAIL_DATABASE_PASSWORD", "RABBITMQ_USER", "RABBITMQ_PASS", "MAILTRAP_USERNAME", "MAILTRAP_PASSWORD", "APP_ADMIN_EMAIL", "APP_ADMIN_SENHA"],
      nonsensitive([for match in regexall("(?m)^[ \\t]*([A-Za-z_][A-Za-z0-9_]*)[ \\t]*=", var.app_env) : match[0]])
    )) == 0
    error_message = "app_env must define IEFC_DATABASE_*, EMAIL_DATABASE_* (NAME, USERNAME, PASSWORD), RABBITMQ_USER, RABBITMQ_PASS, MAILTRAP_USERNAME, MAILTRAP_PASSWORD, APP_ADMIN_EMAIL and APP_ADMIN_SENHA."
  }
}

locals {
  app_env_secret_name = "iefc/app-env"
  jwt_secret_name     = "iefc/jwt"

  # Later lines win when a key is repeated; surrounding single or double quotes are removed.
  app_env_entries = { for match in regexall("(?m)^[ \\t]*([A-Za-z_][A-Za-z0-9_]*)[ \\t]*=[ \\t]*(.*?)[ \\t\\r]*$", var.app_env) : match[0] => match[1]... }
  app_env = {
    for key, values in local.app_env_entries :
    key => replace(values[length(values) - 1], "/^\"(.*)\"$|^'(.*)'$/", "$1$2")
  }

  database_env_keys = ["IEFC_DATABASE_NAME", "IEFC_DATABASE_USERNAME", "IEFC_DATABASE_PASSWORD", "EMAIL_DATABASE_NAME", "EMAIL_DATABASE_USERNAME", "EMAIL_DATABASE_PASSWORD"]
}

resource "aws_secretsmanager_secret" "app_env" {
  name                    = local.app_env_secret_name
  description             = "IEFC application settings loaded into /opt/iefc/.env on the private hosts."
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "app_env" {
  secret_id = aws_secretsmanager_secret.app_env.id
  secret_string = jsonencode(merge(
    { FRONTEND_BASE_URL = "http://${aws_instance.proxy.public_ip}" },
    local.app_env,
    { DOCKER_NAMESPACE = var.docker_namespace },
  ))
}

resource "tls_private_key" "jwt" {
  algorithm = "RSA"
  rsa_bits  = 2048
}

resource "aws_secretsmanager_secret" "jwt" {
  name                    = local.jwt_secret_name
  description             = "IEFC JWT signing key pair (PEM) mounted into the iefc-api container."
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "jwt" {
  secret_id = aws_secretsmanager_secret.jwt.id
  secret_string = jsonencode({
    private_key_pem = tls_private_key.jwt.private_key_pem_pkcs8
    public_key_pem  = tls_private_key.jwt.public_key_pem
  })
}

output "secret_names" {
  value = {
    app_env = aws_secretsmanager_secret.app_env.name
    jwt     = aws_secretsmanager_secret.jwt.name
  }
}
