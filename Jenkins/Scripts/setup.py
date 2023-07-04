class Setup():
    """
        Sets up the region and configuration for the s3 client functions
        TODO: finish the configuration variables
    """

    def __init__(self, region, bucket, access_key, secret_key):
        self.envfile_region = "us-east-1"
        self.region = region
        self.bucket = bucket
        self.access_key = access_key
        self.secret_key = secret_key


