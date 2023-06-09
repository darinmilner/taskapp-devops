resource "aws_kms_key" "task-db-key" {
  description             = "KMS for encrypted db"
  deletion_window_in_days = 10
  is_enabled              = true
}