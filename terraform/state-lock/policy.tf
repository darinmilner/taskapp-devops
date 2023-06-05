resource "aws_s3_bucket_policy" "bucket-policy" {
  bucket = aws_s3_bucket.storage-bucket.bucket

  policy = jsonencode({
    Version   = "2012-10-17",
    Statement = [
      {
        Sid       = "VisualEditor0"
        Effect    = "Allow"
        Action    = "s3*"
        Principal = "*"
        Resource  = [
          aws_s3_bucket.storage-bucket.arn,
          "${aws_s3_bucket.storage-bucket.arn}/*"
        ]
      }
    ]
  })
}