import boto3
import json
import logging
import sys
from botocore.config import Config
from botocore.exceptions import ClientError

USEAST1_BUCKET_NAME = "taskapi-storage-useast1"
def get_latest_envfile(access_key, secret_key):
    # TODO: add to Setup class to make code dynamic
    session = boto3.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
    )
    s3_resource = session.resource('s3', region_name='us-east-1')

    # TODO: find bug and download file from useast1 bucket by calling download_file function below
    try:
        # TODO: add BucketName to Setup class
        files = list(s3_resource.Bucket(USEAST1_BUCKET_NAME).objects.filter(Prefix="envfiles/"))
        files.sort(key=lambda x: x.last_modified)
        latest_file = files[-1].key
        print(latest_file)
        download_file(access_key, secret_key, latest_file)
        return latest_file
    except ClientError as e:
        logging.error(e)


def download_file(access_key, secret_key, file_name):
    session = boto3.Session(
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
        region_name='us-east-1'
    )
    #s3_resource = session.resource('s3', region_name='us-east-1')
    source_s3 = session.client('s3')
    source_s3.download_file(USEAST1_BUCKET_NAME, 'src\resources\application-prod.yml', file_name)

    # TODO use boto3 s3 Object and download_file functions to download the latest file


def upload_envfile_to_regional_bucket(file, bucket, region, object=None):
    s3_client = boto3.client("s3")
    config = Config(
        region_name=region
    )
    if object is None:
        object = f"envfiles/{file}"
    try:
        s3_client.upload_file(file, bucket, object, Config=config)
    except ClientError as e:
        logging.error(e)
 # TODO: use boto3 client to upload the env file to the bucket using upload_file method


access_key = sys.argv[1]
secret_key = sys.argv[2]
upload_region = sys.argv[3]
upload_bucket = sys.argv[4]

latestfile = get_latest_envfile(access_key, secret_key)
upload_envfile_to_regional_bucket(latestfile, upload_bucket, upload_region)