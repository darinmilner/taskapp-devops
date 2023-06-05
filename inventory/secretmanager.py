import boto3
from botocore.exceptions import ClientError


def get_all_secrets(aws_region):
    session = boto3.session.Session()
    secrets_client = session.client("secretsmanager", aws_region)
    try:
        response = secrets_client.list_secrets()
        return response
    except ClientError as e:
        raise Exception(f"client error to get secrets {e.__str__()}")
    except Exception as e:
        raise Exception(f"unexpected error to get secrets {e.__str__()}")


secrets = get_all_secrets("us-east-1")

for secret in secrets["SecretList"]:
    print(secret["Name"])
