provider "aws" {
  region = var.aws-region
}

locals {
  short_region = replace(var.aws-region, "-", "")
}

resource "aws_s3_bucket" "storage-bucket" {
  bucket = "taskapi-storage-bucket-${var.aws-region}"
}

resource "aws_s3_bucket_acl" "bucket-acl" {
  bucket = aws_s3_bucket.storage-bucket.bucket
  acl    = "private-read"
}

resource "aws_dynamodb_table" "state-lock-table" {
  name         = "state-lock-table-${local.short_region}"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockId"

  attribute {
    name = "LockId"
    type = "S"
  }
}