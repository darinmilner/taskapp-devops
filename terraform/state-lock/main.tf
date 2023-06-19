provider "aws" {
  region = var.aws-region
  access_key = var.access-key
    secret_key = var.secret-key
}

locals {
  short_region = replace(var.aws-region, "-", "")
}

resource "aws_s3_bucket" "storage-bucket" {
  bucket = "taskapi-storage-bucket-${local.short_region}"
}

#resource "aws_s3_bucket_acl" "bucket-acl" {
#  bucket = aws_s3_bucket.storage-bucket.bucket
#  acl    = "public-read"
#}

resource "aws_dynamodb_table" "state-lock-table" {
  name         = "state-lock-table-${local.short_region}"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }
}