def getAPIEnvFile(String bucketName) {
    try {
        echo "Getting application file from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/application-prod.yaml src/resources/application-prod.yaml --profile Default
        """
    } catch (Exception err) {
        echo "Error getting file from s3 bucket $err"
        throw err
    }
}

return this