def terraformInit(backendBucket, appFolder, cloudEnv, awsRegion, stateTable, accessKey, secretKey) {
    if (appFolder == "database") {
        appFolder = "terraform/database"
    } else if (appFolder == "lambdas") {
        appFolder = "terraform/lambdas"
    } else if (appFolder == "core-resources") {
        appFolder = "terraform"
    }

    echo "Initializing Terraform in folder $appFolder"

    try {
        sh """
            cd ${appFolder}
            terraform init -backend-config="bucket=${backendBucket}" \\
            -backend-config="key=${appFolder}/${cloudEnv}/terraform.tfstate" \\
            -backend-config="region=${awsRegion}" \\
            -backend-config="access_key=${accessKey}" \\
            -backend-config="secret_key=${secretKey}" \\
            -backend-config="dynamodb_table=${stateTable}"
        """
    } catch (err) {
        returnError(err)
        throw err
    }
}

def planTerraform(backendBucket, appFolder, cloudEnv, awsRegion) {
    if (appFolder == "database") {
        appFolder = "terraform/database"
    } else if (appFolder == "lambdas") {
        appFolder = "terraform/lambdas"
    }

    echo "Running terraform plan in folder $appFolder"

    sh """
        terraform plan
    """
}

def returnError(err) {
    echo "Terraform init failed $err"
    echo err.getMessage()
}

return this