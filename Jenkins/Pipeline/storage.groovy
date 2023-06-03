def getAPIEnvFile(bucketName) {
    try {
        echo "Getting .env file from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/api-env-file src/resources/
        """
    } catch (Exception err) {
        echo err
        throw err
    }
}

return this