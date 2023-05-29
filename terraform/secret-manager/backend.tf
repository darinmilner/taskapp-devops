terraform {
  backend "s3" {
    bucket = "storage-bucket"
    region = "us-east-1"
    key    = "secret-manager/terraform.tfstate"
  }
}