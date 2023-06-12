variable "aws-region" {
  type = string
}

variable "db-port" {
  type    = number
  default = 5432
}

variable "private-subnets" {
  description = "Private subnets used for database"
}

variable "vpc-id" {
  type        = string
  description = "VPC Id"
}

variable "db-name" {
  type        = string
  description = "Database Name"
}

variable "db-instance-type" {
  description = "DB Instance type"
  default     = "db.t3.micro" // TODOS change size based on growth
}

variable "backup-days" {
  type        = number
  description = "Backdays for rds instance"
  default     = 30
}

variable "db-secret" {
  type        = string
  description = "DB Secret Name"

}

variable "dbusername" {
  type        = string
  description = "DB Username"
}

variable "dbpassword" {
  type        = string
  description = "DB Password"
}

variable "system-environment" {
  type = string
}

variable "project" {
  type        = string
  description = "Project name"
  default     = "Task-API"
}

variable "secret-name" {
  type = string
}