locals {
  zone1         = data.aws_availability_zones.az-zones.names[0]
  zone2         = data.aws_availability_zones.az-zones.names[1]
  zone3         = data.aws_availability_zones.az-zones.names[2]
  azs           = slice(data.aws_availability_zones.az-zones.names, 0, 3 )
  server-port   = 8080
  db-port       = 5432
  all-ports     = 0
  ssh-port      = 22
  https-port    = 443
  http-port     = 80
  tcp-protocol  = "tcp"
  all-protocols = "-1"
  open_cidr     = "0.0.0.0/0"
  region        = replace(var.aws-region, "-", "" )

  common-tags = {
    Env       = var.system-environment
    ManagedBy = "Terraform"
    Project   = var.project
  }
}