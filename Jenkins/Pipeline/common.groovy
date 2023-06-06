String getRegionShortName(String region) {
    return region.replace("-", "")
}

String getBucketName(String awsRegion) {
    String bucketName = "taskapi-storage-bucket-"
    switch (awsRegion) {
        case "us-west-1":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "us-east-1":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "us-east-2":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        case "ap-southeast-3":
            bucketName += getRegionShortName(awsRegion)
            return bucketName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

String getDynamoDBStateTableName(String awsRegion) {
    String tableName = "state-lock-table-"
    switch (awsRegion) {
        case "us-west-1":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "us-east-1":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "us-east-2":
            tableName += getRegionShortName(awsRegion)
            return tableName
        case "ap-southeast-3":
            tableName += getRegionShortName(awsRegion)
            return tableName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

def configureAWSProfile(String awsRegion, String awsAccessKey, String awsSecretKey) {
    echo "Configuring AWS Profile"

    try {
        sh """
            aws configure set aws_access_key_id ${awsAccessKey} --profile Default
            aws configure set aws_secret_access_key ${awsSecretKey} --profile Default
            aws configure set region ${awsRegion} --profile Default
            aws configure set output "json" --profile Default
        """
    } catch (Exception err) {
        echo "Error configuring AWS Profile $err"
        // TODO: log and throw error
    }
}

return this