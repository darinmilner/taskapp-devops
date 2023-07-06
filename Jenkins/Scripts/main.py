import sys

from get_file import *
from setup import Setup

access_key = sys.argv[1]
secret_key = sys.argv[2]
upload_region = sys.argv[3]
upload_bucket = sys.argv[4]

s3setup = Setup(upload_region, upload_bucket, access_key, secret_key)
s3latestfile = get_latest_envfile(s3setup)

if upload_region != s3setup.envfile_region:
    upload_envfile_to_regional_bucket(s3setup, s3latestfile)

print(s3latestfile)
