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
            terraform init -no-color \\
            -backend-config="bucket=${backendBucket}" \\
            -backend-config="key=${appFolder}/${cloudEnv}/terraform.tfstate" \\
            -backend-config="region=${awsRegion}" \\
            -backend-config="access_key=${accessKey}" \\
            -backend-config="secret_key=${secretKey}" \\
            -backend-config="dynamodb_table=${stateTable}"
        """
    } catch (err) {
        returnError(err, "Terraform init failed")
        throw err
    }
}

def terraformPlan(appFolder, cloudEnv, awsRegion) {
    if (appFolder == "database") {
        appFolder = "terraform/database"
    } else if (appFolder == "lambdas") {
        appFolder = "terraform/lambdas"
    }

    echo "Running terraform plan in folder $appFolder"
    try {
        sh """
            cd ${appFolder}
            terraform fmt
            terraform validate -no-color
            terraform plan -var aws_region=${awsRegion} \\
                -var env=${cloudEnv} 
        """
    } catch (Exception err) {
        returnError(err, "Terraform plan failed")
        throw err
    }
}

def returnError(err, message) {
    echo "$message Error: $err"
    echo err.getMessage()
}

return this