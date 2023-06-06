def terraformInit(backendBucket, appFolder, cloudEnv, awsRegion, stateTable, accessKey, secretKey) {
    String folder = getTerraformAppFolder(appFolder)

    echo "Initializing Terraform in folder $folder"

    try {
        sh """
            cd ${folder}
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

def terraformPlan(String appFolder, String cloudEnv, String awsRegion) {
    String folder = getTerraformAppFolder(appFolder)

    echo "Running terraform plan in folder $folder"
    try {
        sh """
            cd ${folder}
            terraform fmt
            terraform validate -no-color
            terraform plan
                -out ${appFolder}.tfplan
                -var aws_region=${awsRegion} \\
                -var env=${cloudEnv} 
        """
    } catch (Exception err) {
        returnError(err, "Terraform plan failed")
        throw err
    }
}

def terraformApply(String appFolder, String awsRegion, String cloudEnv) {
    String folder = getTerraformAppFolder(appFolder)
    sh """
        cd ${folder}
        terraform apply
            "${appFolder}.tfplan"
            -var aws_region=${awsRegion} \\
            -var env=${cloudEnv} 
    """
}

def returnError(err, message) {
    echo "$message Error: $err"
    echo err.getMessage()
}

String getTerraformAppFolder(String appFolder) {
    if (appFolder == "database") {
        appFolder = "terraform/database"
    } else if (appFolder == "lambdas") {
        appFolder = "terraform/lambdas"
    } else if (appFolder == "core-resources") {
        appFolder = "terraform/core-resources"
    }

    return appFolder
}

return this