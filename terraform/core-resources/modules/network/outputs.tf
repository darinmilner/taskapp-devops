output "server-sg" {
  value = aws_security_group.server-sg.id
}

output "public-subnet" {
  value = aws_subnet.public.id
}