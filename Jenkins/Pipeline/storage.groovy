def getAndUploadLatestEnvFileToS3(String awsRegion, String bucketName) {
    withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
        sh """
            #!/bin/bash
            cd Jenkins/Scripts/
            mkdir -p envfiles/
            file=\$(python3 main.py $ACCESSKEY $SECRETKEY $awsRegion $bucketName)
        """
    }
}

def zipAndPushAPIToS3(String bucketName) {
    String versionNumber = getReleaseVersion()
    String zipFileName = "user-api-${versionNumber}"
    try {
        echo "Pushing API code to $bucketName"
        sh """
            zip -r ${zipFileName}.zip src/
            ls -la
            aws s3 cp ${WORKSPACE}/${zipFileName}.zip s3://${bucketName}/api/${versionNumber}/  --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Error pushing code to S3 bucket $err")
    }
}

def pushTerraformPlanToS3(String bucketName, String appFolder, String changeTicket = null) {
    String versionNumber = getReleaseVersion()
    if (changeTicket == null) {
        versionNumber += "-${changeTicket}"
    }

    try {
        echo "Pushing terraform plan version number ${versionNumber} to $bucketName"
        sh """
            aws s3 sync terraform/${appFolder}/${appFolder}.tfplan s3://${bucketName}/${versionNumber}/${appFolder}/terraform-plans/${appFolder}.tfplan  --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Error pushing code to S3 bucket $err")
    }
}

String getReleaseVersion() {
    def gitCommit = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
    String versionNumber
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER
    } else {
        versionNumber = gitCommit.take(8)
    }

    println "Version number is $versionNumber"
    return versionNumber
}

def getLatestEnvFile(String bucket) {
    try {
        sh """
            aws s3 cp s3://$bucket/envfiles/application-prod.yaml src/resources/application-prod.yaml --profile Default
        """
    } catch (Exception err) {
        throw new Exception("Error copying latest envfile $err")
    }
}

return this