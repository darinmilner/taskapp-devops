def getAPIEnvFile(String bucketName) {
    try {
        echo "Getting application file from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/application-prod.yaml src/resources/application-prod.yaml --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting!"
        errorLib.Errors.throwError(err, "Error getting file from s3 bucket $err")
    }
}

def zipAndPushAPIToS3(String bucketName) {
    try {
        echo "Pushing API code to $bucketName"
        sh """
            aws s3 sync src/ s3://${bucketName}/api  --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting!"
        errorLib.Errors.throwError(err, "Error pushing code to S3 bucket $err")
    }
}

return this