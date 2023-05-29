provider "aws" {
  region = var.aws-region
}

module "network" {
  source        = "./modules/network"
  aws-region    = var.aws-region
  all-ports     = local.all-ports
  all-protocols = local.all-protocols
  server-port   = local.server-port
  ssh-port      = local.ssh-ports
  tcp-protocol  = local.tcp-protocol
}

module "server" {
  source     = "./modules/server"
  aws-region = var.aws-region
  server-sg  = module.network.server-sg
  zone1      = local.zone1
}