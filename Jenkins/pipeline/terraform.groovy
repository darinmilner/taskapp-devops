def terraformInit(backendBucket, appFolder, awsRegion, accessKey, secretKey) {
    echo "Initializing Terraform"
    if (appFolder == "secret-manager") {
        appFolder = "terraform/secret-manager"
    }

    try {
        sh """
            cd ${appFolder}
            terraform init -backend-config="bucket=${backendBucket}" \\
            -backend-config="key=${appFolder}/terraform.tfstate" \\
            -backend-config="region=${awsRegion}" \\
            -backend-config="access_key=${accessKey}" \\
            -backend-config="secret_key=${secretKey}"
        """
    } catch (Exception err) {
        echo "Terraform init failed $err"
        echo err.getMessage()
        echo err.getStackTrace()
        throw err
    }
}

return this