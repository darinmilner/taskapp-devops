locals {
  zone1         = "${var.aws-region}a"
  server-port   = 8080
  db-port       = 5432
  all-ports     = 0
  ssh-ports     = 443
  tcp-protocol  = "tcp"
  all-protocols = "-1"
}