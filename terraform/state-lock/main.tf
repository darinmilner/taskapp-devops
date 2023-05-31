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
}

resource "aws_dynamodb_table" "state-lock-table" {
  name = "state-lock-table-${local.short_region}"
}