import boto3
from botocore.exceptions import ClientError


def get_ec2_instances(aws_region):
    ec2 = boto3.resource("ec2", aws_region)

    try:
        for instance in ec2.instances.all():
            print(f"Id {instance.id}\nPlatform {instance.platform}\n Type {instance.type}")
    except ClientError as e:
        raise Exception(f"client error to get ec2 instances {e.__str__()}")
    except Exception as e:
        raise Exception(f"unexpected error to get ec2 instances {e.__str__()}")


get_ec2_instances("us-east-1")
