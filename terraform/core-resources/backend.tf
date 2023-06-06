terraform {
  backend "s3" {
    bucket         = "state-storage-bucket"
    key            = "terraformstate/taskapi/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "state-lock-table-useast1"
  }
}