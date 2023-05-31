resource "aws_vpc" "main" {
  cidr_block           = var.vpc-cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"
}

resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id

  cidr_block              = var.cidr-block
  availability_zone       = var.zone1
  map_public_ip_on_launch = true
}

resource "aws_vpc_endpoint" "s3-endpoint" {
  service_name    = "com.amazonaws.${var.aws-region}.s3"
  route_table_ids = [aws_route_table.public-route.id]  //TODO: attach to private route
  vpc_id          = aws_vpc.main.id
}

resource "aws_vpc_endpoint" "dynamodb-endpoint" {
  service_name    = "com.amazonaws.${var.aws-region}.dynamodb"
  route_table_ids = [aws_route_table.public-route.id]  //TODO: attach to private route
  vpc_id          = aws_vpc.main.id
}