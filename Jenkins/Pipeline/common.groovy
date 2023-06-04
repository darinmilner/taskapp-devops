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

def getAppFolder(pipelineAction) {
    def appFolder
    switch (pipelineAction) {
        case "lambdas" || "test-lambdas":
            appFolder = "terraform/lambdas"
            return appFolder
        case "database":
            appFolder = "terraform/database"
            return appFolder
        case "deploy-api" || "test-api":
            appFolder = "src"
            return appFolder
        case "core-resources":
            appFolder = "terraform"
            return appFolder
        default:
            throw new Exception("Invalid or unsupported pipeline action $pipelineAction")
    }
}

def configureAWSProfile(awsRegion, awsAccessKey, awsSecretKey) {
    echo "Configuring AWS Profile"

    try {
        sh """
            aws configure set aws_access_key_id ${awsAccessKey} --profile Default
            aws configure set aws_secret_access_key ${awsSecretKey} --profile Default
            aws configure set region ${awsRegion} --profile Default
            aws configure set output "json" --profile Default
        """
    } catch (err) {
        echo "Error configuring AWS Profile"
        // TODO: log and throw error
    }
}

return this