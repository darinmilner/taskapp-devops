resource "aws_iam_policy" "api-lambda-policy" {
  name        = "taskapp-api-lambda-policy"
  description = "Allows Lambda to call AWS services on your behalf."
  policy      = <<EOF
{
  "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "sns:ListTagsForResource",
                "sns:ListSubscriptionsByTopic",
                "sns:GetTopicAttributes",
                "sns:DeleteTopic",
                "sns:CreateTopic",
                "sns:ListTopics",
                "sns:Unsubscribe",
                "sns:GetSubscriptionAttributes",
                "sns:ListSubscriptions",
                "sns:AddPermission",
                "sns:Publish",
                "sns:Subscribe",
                "sns:ConfirmSubscription",
                "sns:RemovePermission"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}


resource "aws_iam_role" "api-lambda-role" {
  name               = "taskapp-api-lambda-role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda-policy-attachment" {
  role       = aws_iam_role.api-lambda-role.name
  policy_arn = aws_iam_policy.api-lambda-policy.arn
  depends_on = [aws_iam_role.api-lambda-role, aws_iam_policy.api-lambda-policy]
}
