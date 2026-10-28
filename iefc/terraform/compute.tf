variable "docker_namespace" {
  description = "Docker Hub namespace containing the public IEFC images."
  type        = string

  validation {
    condition     = can(regex("^[a-z0-9][a-z0-9_.-]*$", var.docker_namespace))
    error_message = "docker_namespace must be a lowercase Docker Hub namespace."
  }
}

variable "instance_profile" {
  description = "Existing instance profile with Systems Manager access."
  type        = string
  default     = "LabInstanceProfile"
}

variable "web_instance_type" {
  type    = string
  default = "t3.micro"
}

variable "backend_instance_type" {
  type    = string
  default = "t3.medium"
}

variable "database_instance_type" {
  type    = string
  default = "t3.small"
}

data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023*-kernel-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_security_group" "proxy" {
  name        = "iefc-proxy"
  description = "Public HTTP entry point"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "frontend" {
  name        = "iefc-frontend"
  description = "Frontend access from the proxy only"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.proxy.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "backend" {
  name        = "iefc-backend"
  description = "API access from the proxy only"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.proxy.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "database" {
  name        = "iefc-database"
  description = "PostgreSQL access from the backend only"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port       = 5432
    to_port         = 5433
    protocol        = "tcp"
    security_groups = [aws_security_group.backend.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "frontend" {
  for_each = toset(["1", "2"])

  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.web_instance_type
  subnet_id                   = aws_subnet.public.id
  private_ip                  = "10.20.1.${10 + tonumber(each.key)}"
  associate_public_ip_address = true
  vpc_security_group_ids      = [aws_security_group.frontend.id]
  iam_instance_profile        = var.instance_profile
  user_data                   = templatefile("${path.module}/templates/frontend.sh.tftpl", { docker_namespace = var.docker_namespace })

  metadata_options {
    http_tokens = "required"
  }

  root_block_device {
    encrypted   = true
    volume_type = "gp3"
    volume_size = 12
  }

  depends_on = [aws_route_table_association.public]
  tags       = { Name = "iefc-frontend-${each.key}" }
}

resource "aws_instance" "backend" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.backend_instance_type
  subnet_id                   = aws_subnet.private.id
  private_ip                  = "10.20.2.11"
  associate_public_ip_address = false
  vpc_security_group_ids      = [aws_security_group.backend.id]
  iam_instance_profile        = var.instance_profile
  user_data = templatefile("${path.module}/templates/docker-host.sh.tftpl", {
    compose_base64 = filebase64("${path.module}/deployment/backend.yml")
    loader_base64  = filebase64("${path.module}/templates/load-secrets.py")
    aws_region     = var.aws_region
    app_secret     = aws_secretsmanager_secret.app_env.name
    jwt_secret     = aws_secretsmanager_secret.jwt.name
    env_keys       = "*"
  })

  metadata_options {
    http_tokens = "required"
  }

  root_block_device {
    encrypted   = true
    volume_type = "gp3"
    volume_size = 20
  }

  depends_on = [aws_route_table_association.private]
  tags       = { Name = "iefc-backend" }
}

resource "aws_instance" "database" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.database_instance_type
  subnet_id                   = aws_subnet.private.id
  private_ip                  = "10.20.2.10"
  associate_public_ip_address = false
  vpc_security_group_ids      = [aws_security_group.database.id]
  iam_instance_profile        = var.instance_profile
  user_data = templatefile("${path.module}/templates/docker-host.sh.tftpl", {
    compose_base64 = filebase64("${path.module}/deployment/database.yml")
    loader_base64  = filebase64("${path.module}/templates/load-secrets.py")
    aws_region     = var.aws_region
    app_secret     = aws_secretsmanager_secret.app_env.name
    jwt_secret     = "-"
    env_keys       = join(",", local.database_env_keys)
  })

  metadata_options {
    http_tokens = "required"
  }

  root_block_device {
    encrypted   = true
    volume_type = "gp3"
    volume_size = 30
  }

  depends_on = [aws_route_table_association.private]
  tags       = { Name = "iefc-database" }
}

resource "aws_instance" "proxy" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.web_instance_type
  subnet_id                   = aws_subnet.public.id
  associate_public_ip_address = true
  vpc_security_group_ids      = [aws_security_group.proxy.id]
  iam_instance_profile        = var.instance_profile
  user_data = templatefile("${path.module}/templates/proxy.sh.tftpl", {
    frontend_ips = [for instance in aws_instance.frontend : instance.private_ip]
    backend_ip   = aws_instance.backend.private_ip
  })

  metadata_options {
    http_tokens = "required"
  }

  root_block_device {
    encrypted   = true
    volume_type = "gp3"
    volume_size = 12
  }

  depends_on = [aws_route_table_association.public]
  tags       = { Name = "iefc-nginx-proxy" }
}

output "application_url" {
  value = "http://${aws_instance.proxy.public_ip}"
}

output "instance_ids" {
  value = {
    proxy     = aws_instance.proxy.id
    frontends = { for key, instance in aws_instance.frontend : key => instance.id }
    backend   = aws_instance.backend.id
    database  = aws_instance.database.id
  }
}