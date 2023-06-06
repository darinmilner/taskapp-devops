variable "cidr-block" {
  type    = string
  default = "10.0.0.0/24"
}

variable "private-cidrs" {
  type    = list(string)
  default = ["10.0.10.0/24", "10.0.11.0/24"]
}

variable "vpc-cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "aws-region" {
  type = string
}

variable "server-port" {
  type = string
}

variable "all-ports" {
  type = string
}

variable "ssh-port" {
  type = string
}

variable "http-port" {
  type = string
}

variable "https-port" {
  type = string
}

variable "all-protocols" {
  type = string
}

variable "tcp-protocol" {
  type = string
}

variable "open-cidr" {
  type = string
}

variable "zone1" {
  type = string
}

variable "zone2" {
  type = string
}

variable "common-tags" {
  type = map(string)
}