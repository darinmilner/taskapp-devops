//TODO: get deplpoyment start scipts from S3
def startCodeDeploy(String bucket, String awsRegion) {
    def storageLib = evaluate readTrusted("Jenkins/Pipeline/storage.groovy")
    String groupName = getCodeDeployGroup(awsRegion)
    String versionNumber = storageLib.getReleaseVersion()
    echo "Starting api deployment to $groupName"
    sh """
        aws deploy create-deployment --application-name UserServiceAPI \\
            --s3-location bucket=${bucket},key=api/${versionNumber}/user-api-${versionNumber},bundleType=zip \\
            --deployment-group-name ${groupName} --profile Default
    """
//    --deployment-config-name CodeDeployDefault.OneAtATime \\
//            --description UserAPICopeDeploy  --Profile Default
}

String getCodeDeployGroup(String awsRegion) {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    String codeDeployGroupName = "User-Service-API-DeploymentGroup-"
    switch (awsRegion) {
        case "us-east-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion)
            return codeDeployGroupName
        case "us-east-2":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion)
            return codeDeployGroupName
        case "us-west-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion)
            return codeDeployGroupName
        case "ap-southeast-1":
            codeDeployGroupName += commonLib.getRegionShortName(awsRegion)
            return codeDeployGroupName
        default:
            return new Exception("Invalid or unsupported region $awsRegion")
    }
}

//TODO: remove later

String region = "us-west-1"

String getBucketName(String region) {
    String bucketName = "core-bucket-"
    switch (region) {
        case "us-east-1":
            bucketName += getRegionShortName(region)
            return bucketName
        case "us-east-2":
            bucketName += getRegionShortName(region)
            return bucketName
        case "us-west-1":
            bucketName += getRegionShortName(region)
            return bucketName
        default:
            throw new Exception("Invalid or unsupported region $region")
    }
}

List<String> getGroupName(String region) {
    List environments = ["dev", "test", "prod"]
    List<String> groups = []
    switch (region) {
        case "us-east-1":
            for (env in environments) {
                String groupName = getBucketName("us-east-1") + "-$env"
                println groupName
                groups.add(groupName)
            }
            return groups
            break
        default:
            throw new Exception("Invalid Region")
    }
}

List<String> groupName = getGroupName("us-east-1")
println groupName

return this