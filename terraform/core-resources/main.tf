provider "aws" {
  region = var.aws-region
}

module "network" {
  source        = "./modules/network"
  aws-region    = var.aws-region
  all-ports     = local.all-ports
  all-protocols = local.all-protocols
  server-port   = local.server-port
  ssh-port      = local.ssh-port
  tcp-protocol  = local.tcp-protocol
  http-port     = local.http-port
  https-port    = local.https-port
  zone1         = local.azs[0]
  zone2         = local.azs[1]
  open-cidr     = local.open_cidr
  common-tags   = local.common-tags
}

module "server" {
  source     = "./modules/server"
  aws-region = var.aws-region
  server-sg  = module.network.server-sg
  zone1      = local.azs[0]
  zone2      = local.azs[1]
  env        = var.system-environment
  project    = var.project
}