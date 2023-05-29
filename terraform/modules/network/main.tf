resource "aws_vpc" "main" {
  cidr_block           = var.vpc-cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"
}

resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id

  cidr_block = var.cidr-block

  map_public_ip_on_launch = true
}

resource "aws_security_group" "lambda-sg" {
  name        = "lambda-sg"
  description = "lambda security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Open API port 8080"
    from_port   = var.server-port
    to_port     = var.server-port
    protocol    = var.all-protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Outbound traffic"
    from_port   = var.all-ports
    to_port     = var.all-ports
    protocol    = var.all-protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "Lambda-sg"
  }
}

resource "aws_security_group" "server-sg" {
  name        = "server-sg"
  description = "server security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Open API port 8080"
    from_port   = var.server-port
    to_port     = var.server-port
    protocol    = var.all-protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH from VPC"
    from_port   = var.ssh-port
    to_port     = var.ssh-port
    protocol    = var.tcp-protocol
    cidr_blocks = [aws_vpc.main.cidr_block]
  }

  egress {
    description = "Outbound traffic"
    from_port   = var.all-ports
    to_port     = var.all-ports
    protocol    = var.all-protocols
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "Server-sg"
  }
}

resource "aws_vpc_endpoint" "s3-endpoint" {
  service_name = "com.amazonaws.${var.aws-region}.s3"
  vpc_id       = aws_vpc.main.id
}