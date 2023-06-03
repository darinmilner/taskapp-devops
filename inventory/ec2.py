import boto3

aws_region = "us-east-1"

ec2 = boto3.resource("ec2", aws_region)

for instance in ec2.instances.all():
    print(f"instance id : {instance.id}\nplatform {instance.platform}\ntype {instance.type}")
