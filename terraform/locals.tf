locals {
  zone1         = "${var.aws-region}a"
  zone2         = "${var.aws-region}b"
  zone3         = "${var.aws-region}c"
  server-port   = 8080
  db-port       = 5432
  all-ports     = 0
  ssh-port      = 22
  https-port    = 443
  http-port     = 80
  tcp-protocol  = "tcp"
  all-protocols = "-1"
  open_cidr     = "0.0.0.0/0"

  common-tags = {
    Env       = var.system-environment
    ManagedBy = "Terraform"
    Project   = var.project
  }
}