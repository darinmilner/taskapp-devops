provider "aws" {
  region = var.aws-region
}

resource "aws_db_security_group" "db-sg" {
  name = "database-security-group"
}