//def getAPIEnvFileFromUSEast1Bucket(String awsRegion, String bucketFilePath) {
//    try {
//        echo "Getting application file $bucketFilePath from us-east-1 s3 bucket"
//        sh """
//            aws configure set region us-east-1 --profile Default
//            aws s3 cp s3://taskapi-storage-bucket-useast1/$bucketFilePath \\
//            src/resources/application-prod.yaml --profile Default
//        """
//    } catch (Exception err) {
//        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
//        echo "Pipeline is exiting $err!"
//        errorLib.throwError(err, "Error getting file from us-east-1 s3 bucket $err")
//    }
//
//    if (awsRegion != "us-east-1") {
//        String region = awsRegion.replace("-", "")
//        copyEnvFileToRegionalS3Bucket("taskapi-storage-bucket-${region}", awsRegion, bucketFilePath)
//    }
//}


String getLatestEnvFileName(String awsRegion) {
    String latestEnvFileName
    withCredentials([usernamePassword(credentialsId: "amazon", usernameVariable: "ACCESSKEY", passwordVariable: "SECRETKEY")]) {
        latestEnvFileName = sh(script: """
            #!/bin/bash
            cd Jenkins/Scripts/
            envFile=\$(python3 get_file.py $ACCESSKEY $SECRETKEY)
            aws configure set region us-east-1 --profile Default
            aws s3 cp s3://taskapi-storage-bucket-useast1/\$envFile \\
            src/resources/application-prod.yaml --profile Default
        """, returnStdout: true)

        echo "enfile location $latestEnvFileName"

        if (awsRegion != "us-east-1") {
            String region = awsRegion.replace("-", "")
            copyEnvFileToRegionalS3Bucket("taskapi-storage-bucket-${region}", awsRegion, latestEnvFileName)
        }
    }

    return latestEnvFileName
}

def getAPIEnvFile(String bucketName, String filePath) {
    if (filePath == null || filePath == "") {
        codedeployLib = evaluate readTrusted("Jenkins/Pipeline/codedeploy.groovy")
        filePath = codedeployLib.getLatestEnvFileName()
    }
    try {
        echo "Getting application file $filePath from s3 bucket $bucketName"
        sh """
            aws s3 cp s3://${bucketName}/$filePath src/resources/application-prod.yaml --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting $err!"
        errorLib.throwError(err, "Error getting file from s3 bucket $err")
    }
}

def copyEnvFileToRegionalS3Bucket(String bucketName, String awsRegion, String filePath) {
    try {
        echo "Pushing API code to $bucketName"
        sh """
            #!/bin/bash
            echo ${filePath}
            echo ${awsRegion}
            aws configure set region ${awsRegion} --profile Default
            aws s3 cp src/resources/application-prod.yaml s3://${bucketName}/${filePath}  --profile Default
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Error pushing code to S3 bucket $err")
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

return this