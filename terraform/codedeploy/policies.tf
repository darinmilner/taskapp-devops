# create a service role for codedeploy
resource "aws_iam_role" "codedeploy-service" {
  name = "codedeploy-service-role-${local.short_region}"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "codedeploy.amazonaws.com"
        ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

# attach AWS managed policy called AWSCodeDeployRole
# required for deployments which are to an EC2 compute platform
resource "aws_iam_role_policy_attachment" "codedeploy-service" {
  role       = aws_iam_role.codedeploy-service.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole"
}
