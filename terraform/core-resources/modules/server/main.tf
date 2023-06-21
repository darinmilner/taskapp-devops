locals {
  name-prefix = format("%s-%s", var.project, var.system-environment)
}

resource "aws_instance" "server" {
  ami                    = var.amis[var.aws-region]
  instance_type          = "t2.micro"
  key_name               = "terraform"
  vpc_security_group_ids = [var.server-sg]
  availability_zone      = var.zone1
  subnet_id              = var.subnet-id
  iam_instance_profile   = aws_iam_instance_profile.ec2-instance-profile.arn

  user_data = file("${path.module}/install-codedeploy-agent.sh")

  tags = {
    Name    = "${local.name-prefix}-API-Server"
    Project = var.project
  }
}

resource "aws_iam_policy" "ec2-policy" {
  name        = "EC2Policy"
  path        = "/"
  description = "Policy to grant permission to EC2 instances"
  policy      = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Effect" : "Allow",
        "Action" : [
          "ec2:*",
          "autoscaling:Describe*",
          "cloudwatch:*",
          "logs:*",
          "sns:*",
          "iam:GetPolicy",
          "iam:GetPolicyVersion",
          "iam:GetRole"
        ],
        "Resource" : "*"
      },
      {
        "Effect" : "Allow",
        "Action" : "iam:CreateServiceLinkedRole",
        "Resource" : "arn:aws:iam::*:role/aws-service-role/events.amazonaws.com/AWSServiceRoleForCloudWatchEvents*",
        "Condition" : {
          "StringLike" : {
            "iam:AWSServiceName" : "events.amazonaws.com"
          }
        }
      }
    ]
  })
}

# Role for EC2
resource "aws_iam_role" "ec2-role" {
  name               = "EC2Role"
  assume_role_policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ec2.amazonaws.com"
        },
        "Effect" : "Allow"
      }
    ]
  })
}

# Attach policy to role
resource "aws_iam_role_policy_attachment" "ec2-role-policy-attachment" {
  role       = aws_iam_role.ec2-role.name
  policy_arn = aws_iam_policy.ec2-policy.arn
}

# Attach role to instance
resource "aws_iam_instance_profile" "ec2-instance-profile" {
  name = "EC2InstanceProfile"
  role = aws_iam_role.ec2-role.name
}

#creates cloudwatch loggroup and subscription stream
resource "aws_cloudwatch_log_group" "webserverlogs" {
  name              = format("%s-webserverlogs", local.name-prefix)
  retention_in_days = 7
}

resource "aws_cloudwatch_log_stream" "webserverstream" {
  name           = "webserver"
  log_group_name = aws_cloudwatch_log_group.webserverlogs.name
}