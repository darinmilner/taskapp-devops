def getBucketName(awsRegion) {
    String bucketName
    switch (awsRegion) {
        case "us-west-1":
            bucketName = "taskapi-storage-bucket-uswest1"
            return bucketName
        case "us-east-1":
            bucketName = "taskapi-storage-bucket-useast1"
            return bucketName
        case "us-east-2":
            bucketName = "taskapi-storage-bucket-useast2"
            return bucketName
        case "ap-southeast-3":
            bucketName = "taskapi-storage-bucket-apsoutheast3"
            return bucketName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

def getDynamoDBStateTableName(awsRegion) {
    String tableName
    switch (awsRegion) {
        case "us-west-1":
            tableName = "state-lock-table-uswest1"
            return tableName
        case "us-east-1":
            tableName = "state-lock-table-useast1"
            return tableName
        case "us-east-2":
            tableName = "state-lock-table-useast2"
            return tableName
        case "ap-southeast-3":
            tableName = "state-lock-table-apsoutheast3"
            return tableName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

def installAWSCLI() {
    echo "Installing AWS CLI"
    sh """
        curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
        unzip -o awscliv2.zip
        ./aws/install 
        which aws
    """
}

def buildKotlinEnvironment() {
    echo "Building Kotlin"
    sh """
    java --version
    
    """
}

return this