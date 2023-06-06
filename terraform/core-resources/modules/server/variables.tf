variable "aws-region" {
  type = string
}

variable "zone1" {
  type = string
}

variable "zone2" {
  type = string
}

variable "amis" {
  type    = map
  default = {
    us-east-1 = "ami-0947d2ba12ee1ff75"
    us-east-2 = "ami-03657b56516ab7912"
  }
}

variable "server-sg" {
  type = string
}

variable "project" {
  type = string
}

variable "env" {
  type = string
}