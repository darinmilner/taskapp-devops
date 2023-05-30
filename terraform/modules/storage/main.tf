resource "aws_s3_bucket" "storage-bucket" {
  bucket = var.bucket-name
}