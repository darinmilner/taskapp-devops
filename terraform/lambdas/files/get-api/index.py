import boto3
import logging
from botocore.exceptions import ClientError

# TODO import setup class

logger = logging.getLogger()
logger.setLevel(logging.INFO)

access_key = os.environ.get("AWS_ACCESS_KEY")
secret_key = os.environ.get("AWS_SECRET_KEY")

USEAST1_BUCKET = "taskapi-storage-bucket-useast1"


def lambda_handler(event, context):
    logger.info(f"Uploading API file from useast1 bucket {event}")

    upload_bucket = event["uploadBucket"]
    upload_region = event["uploadRegion"]
    latest_file_folder = event["latestFolder"]

    s3setup = Setup(upload_region, upload_bucket, access_key, secret_key, latest_file_folder)
    s3latestfile = get_latest_apifile(s3setup)

    if upload_region != s3setup.envfile_region:
        upload_apifile_to_regional_bucket(s3setup, s3latestfile)


def get_latest_apifile(s3setup):
    session = boto3.Session(
        aws_access_key_id=s3setup.access_key,
        aws_secret_access_key=s3setup.secret_key,
    )
    s3_resource = session.resource('s3', region_name=s3setup.apifile_region)
    latest_file = None
    try:
        files = list(s3_resource.Bucket(USEAST1_BUCKET).objects.filter(Prefix=s3setup.folder))
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
        region_name=s3setup.apifile_region
    )
    source_s3 = session.client('s3')
    downloaded_file = "latest-api.zip"
    logger.info(downloaded_file)
    # file_name is file to download  downloaded file is name after downloading
    source_s3.download_file(USEAST1_BUCKET, file_name, downloaded_file)


def upload_apifile_to_regional_bucket(setup, file):  # object=None):
    session = boto3.Session(
        aws_access_key_id=setup.access_key,
        aws_secret_access_key=setup.secret_key,
        region_name=setup.region
    )

    latest_apifile = f"api/latest-api.zip"
    logger.info(f"Upload file object path {latest_apifile}\n")
    s3_client = session.client("s3")

    file = "latest-api.zip"
    try:
        s3_client.upload_file(file, setup.bucket, latest_apifile)
    except ClientError as e:
        logging.error(e)
