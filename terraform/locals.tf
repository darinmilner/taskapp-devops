locals {
  zone1         = "${var.aws-region}a"
  server-port   = 8080
  db-port       = 5432
  all-ports     = 0
  ssh-port      = 22
  https-port    = 443
  http-port     = 80
  tcp-protocol  = "tcp"
  all-protocols = "-1"

  common_tags = {
    "Project-Name" : "Task API"
  }
}