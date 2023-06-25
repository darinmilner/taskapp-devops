provider "aws" {
  region     = var.aws-region
  access_key = var.access-key
  secret_key = var.secret-key
  profile    = "Default"
}

locals {
  short_region = replace(var.aws-region, "-", "" )
  name-prefix  = format("%s-%s", var.project, var.cloud-environment)
}

resource "aws_codedeploy_app" "user-service" {
  name = "User-Service-API"
}

resource "aws_codedeploy_deployment_group" "user-service-deployment-group" {
  app_name              = aws_codedeploy_app.user-service.name
  deployment_group_name = "User-Service-API-DeploymentGroup-${local.short_region}-${var.cloud-environment}"
  service_role_arn      = aws_iam_role.codedeploy-service.arn

  deployment_config_name = "CodeDeployDefault.OneAtATime"

  ec2_tag_filter {
    key   = "Name"
    type  = "KEY_AND_VALUE"
    value = "${local.name-prefix}-API-Server"
  }

  # triggers a rollback on deployment failure
  auto_rollback_configuration {
    enabled = true
    events  = [
      "DEPLOYMENT_FAILURE"
    ]
  }
}