import boto3
import logging
import sys
from botocore.exceptions import ClientError
from setup import Setup

USEAST1_BUCKET_NAME = "taskapi-storage-useast1"
def get_latest_envfile(access_key, secret_key):
    # TODO: add to Setup class to make code dynamic
    session = boto3.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
    )
    s3_resource = session.resource('s3', region_name='us-east-1')
    latest_file = None
    try:
        # TODO: add BucketName to Setup class
        files = list(s3_resource.Bucket(USEAST1_BUCKET_NAME).objects.filter(Prefix="envfiles/"))
        files.sort(key=lambda x: x.last_modified)
        latest_file = files[-1].key
        download_file(access_key, secret_key, latest_file)
    except ClientError as e:
        logging.error(e)
    return latest_file

def download_file(access_key, secret_key, file_name):
    session = boto3.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
        region_name='us-east-1'
    )
    source_s3 = session.client('s3')
    source_s3.download_file(USEAST1_BUCKET_NAME, file_name, file_name)


def upload_envfile_to_regional_bucket(setup, file, object=None):
    session = boto3.Session(
        aws_access_key_id=setup.access_key,
        aws_secret_access_key=setup.secret_key,
        region_name=setup.region
    )
    s3_client = session.client("s3")
    if object is None:
        object = f"envfiles/{file}"
    try:
        s3_client.upload_file(file, setup.bucket, file)
    except ClientError as e:
        logging.error(e)


access_key = sys.argv[1]
secret_key = sys.argv[2]
upload_region = sys.argv[3]
upload_bucket = sys.argv[4]

s3Setup = Setup(upload_region, upload_bucket, access_key, secret_key)
latestfile = get_latest_envfile(access_key, secret_key)
#upload_envfile_to_regional_bucket(access_key, secret_key, latestfile, upload_bucket, upload_region)
upload_envfile_to_regional_bucket(s3Setup, latestfile)

