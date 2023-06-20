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


resource "aws_iam_policy" "dynamodb-policy" {
  name        = "backend-dynamodb-policy"
  description = "Policy for backend DyanmoDB"
  policy      = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:DescribeTable",
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:DeleteItem"
      ],
      "Resource": "${aws_dynamodb_table.state-table.arn}/*"
    }
  ]
}
EOF
}

resource "aws_iam_role" "db-role" {
  name               = "dynamo-db-role"
  assume_role_policy = <<EOF
  {
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
            "Service" : "dynamodb.amazonaws.com"
        },
        "Effect" : "Allow",
        "Sid" : ""
      }
    ]
  }
EOF
}


resource "aws_iam_role_policy_attachment" "db-policy-attachment" {
  role       = aws_iam_role.db-role.name
  policy_arn = aws_iam_policy.dynamodb-policy.arn
  depends_on = [aws_iam_policy.dynamodb-policy, aws_iam_role.db-role]
}