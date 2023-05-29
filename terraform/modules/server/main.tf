resource "aws_instance" "server" {
  ami                    = var.amis[var.aws-region]
  instance_type          = "t2.micro"
  key_name               = "terraform"
  vpc_security_group_ids = [var.server-sg]
  availability_zone      = var.zone1

  tags = {
    Name    = "API-Server"
    Project = "Task-API"
  }
}