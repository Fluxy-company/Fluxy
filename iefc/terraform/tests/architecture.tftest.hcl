mock_provider "aws" {
  mock_data "aws_ami" {
    defaults = {
      id = "ami-0123456789abcdef0"
    }
  }
}

variables {
  aws_account_id    = "123456789012"
  availability_zone = "us-east-1a"
  docker_namespace  = "example"
}

run "network_and_compute" {
  command = plan

  assert {
    condition     = aws_subnet.public.cidr_block == "10.20.1.0/24" && aws_subnet.private.cidr_block == "10.20.2.0/24"
    error_message = "Public and private subnet CIDRs must match the tier addresses."
  }

  assert {
    condition     = length(aws_instance.frontend) == 2 && aws_instance.backend.associate_public_ip_address == false && aws_instance.database.associate_public_ip_address == false
    error_message = "Two public-tier frontends and private backend/database hosts are required."
  }

  assert {
    condition     = alltrue(concat([for instance in aws_instance.frontend : instance.iam_instance_profile == "LabInstanceProfile"], [aws_instance.proxy.iam_instance_profile == "LabInstanceProfile", aws_instance.backend.iam_instance_profile == "LabInstanceProfile", aws_instance.database.iam_instance_profile == "LabInstanceProfile"]))
    error_message = "Every instance must use the existing management profile."
  }

  assert {
    condition     = aws_instance.frontend["1"].private_ip == "10.20.1.11" && aws_instance.frontend["2"].private_ip == "10.20.1.12" && aws_instance.backend.private_ip == "10.20.2.11" && aws_instance.database.private_ip == "10.20.2.10"
    error_message = "Instance addresses must match the proxy and container configuration."
  }

  assert {
    condition     = length(aws_security_group.database.ingress) == 1 && one(aws_security_group.database.ingress).from_port == 5432 && one(aws_security_group.database.ingress).to_port == 5433
    error_message = "The database tier must expose only its PostgreSQL ports."
  }

  assert {
    condition = alltrue([
      for upstream in ["10.20.1.11:80", "10.20.1.12:80", "10.20.2.11:8080"] :
      strcontains(templatefile("${path.module}/templates/proxy.sh.tftpl", { frontend_ips = ["10.20.1.11", "10.20.1.12"], backend_ip = "10.20.2.11" }), upstream)
    ])
    error_message = "The proxy configuration must route to both frontend instances and the backend."
  }
}