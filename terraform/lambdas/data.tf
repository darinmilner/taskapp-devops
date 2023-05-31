data "aws_s3_bucket" "storage-bucket" {
  bucket = "taskapi-storage-bucket-${local.short-region}"
}