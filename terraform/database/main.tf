provider "aws" {
  region = var.aws-region
}

# DB Subnets
resource "aws_db_subnet_group" "db-main" {
  name       = "main-rds-db-subnets"
  subnet_ids = [var.private-subnets[0], var.private-subnets[1]]
}

resource "aws_security_group" "db-sg" {
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

resource "aws_db_instance" "task-db" {
  identifier           = "${var.system-environment}-main-db"
  db_name              = var.db-name
  storage_encrypted    = true
  kms_key_id           = aws_kms_key.task-db-key.arn
  apply_immediately    = true
  allocated_storage    = 20    # disk space
  storage_type         = "gp2" # entry level general purpose
  engine               = "postgres"
  engine_version       = "14.3"
  instance_class       = var.db-instance-type
  db_subnet_group_name = aws_db_subnet_group.db-main.name
  /* password                = jsondecode(data.aws_secretsmanager_secret_version.secrets.secret_string)["POSTGRES_PASSWORD"]
  username                = jsondecode(data.aws_secretsmanager_secret_version.secrets.secret_string)["POSTGRES_USERNAME"] */
  password                = var.dbpassword
  username                = var.dbusername
  backup_retention_period = var.backup-days
  multi_az                = false // TODO: true for production
  skip_final_snapshot     = true //TODO:  false for production
  vpc_security_group_ids  = [aws_security_group.db-sg.id]
  maintenance_window      = "Mon:00:00-Mon:03:00"
  backup_window           = "03:00-06:00"
}
