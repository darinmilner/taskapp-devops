import boto3
import logging
from botocore.exceptions import ClientError

USEAST1_BUCKET_NAME = "taskapi-storage-bucket-useast1"


def get_latest_envfile(s3setup):
    session = boto3.Session(
        aws_access_key_id=s3setup.access_key,
        aws_secret_access_key=s3setup.secret_key,
    )
    s3_resource = session.resource('s3', region_name=s3setup.envfile_region)
    latest_file = None
    try:
        files = list(s3_resource.Bucket(USEAST1_BUCKET_NAME).objects.filter(Prefix=s3setup.folder))
        files.sort(key=lambda x: x.last_modified)
        latest_file = files[-1].key
        download_file(s3setup, latest_file)
    except ClientError as e:
        logging.error(e)
    return latest_file


def download_file(s3setup, file_name):
    session = boto3.Session(
        aws_access_key_id=s3setup.access_key,
        aws_secret_access_key=s3setup.secret_key,
        region_name=s3setup.envfile_region
    )
    source_s3 = session.client('s3')
    downloaded_file = "application-prod.yaml"
    # file_name is file to download  downloaded file is name after downloading
    source_s3.download_file(USEAST1_BUCKET_NAME, file_name, downloaded_file)


def upload_envfile_to_regional_bucket(setup, file):  # object=None):
    session = boto3.Session(
        aws_access_key_id=setup.access_key,
        aws_secret_access_key=setup.secret_key,
        region_name=setup.region
    )

    file_parts = file.split("/")
    # latest_envfile = f"{file_parts[0]}/application-prod.yaml"
    latest_envfile = f"envfiles/application-prod.yaml"
    print(f"Upload file object path {latest_envfile}\n")
    s3_client = session.client("s3")
    # if object is None:
    #     object = f"{setup.envfile_region}{file}"
    file = "application-prod.yaml"
    try:
        s3_client.upload_file(file, setup.bucket, latest_envfile)
    except ClientError as e:
        logging.error(e)
