def terraformInit(backendBucket, appFolder, awsRegion, accessKey, secretKey) {
    echo "Initializing Terraform"
    if (appFolder == "secret-manager") {
        appFolder = "terraform/secret-manager"
    }

    try {
        sh """
            cd ${appFolder}
            terraform init
        """
    } catch (Exception err) {
        echo "Terraform init failed $err"
        echo err.getMessage()
        echo err.getStackTrace()
        throw err
    }
}

return this