provider "aws" {
  region = var.aws-region
}

resource "aws_db_security_group" "db-sg" {
  name        = "database-security-group"
  description = "Allow access to RDS Postgres DB"
  vpc_id      = data.aws_vpc.main.id

  ingress {
    protocol    = "tcp"
    from_port   = var.db-port
    to_port     = var.db-port
    cidr_blocks = ["0.0.0.0/0"]
  }
}