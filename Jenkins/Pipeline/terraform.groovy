def terraformInit(String backendBucket, String appFolder, String cloudEnv, String awsRegion, String stateTable) {
    String folder = getTerraformAppFolder(appFolder)

    echo "Initializing Terraform in folder $folder"

    try {
        sh """
            cd ${folder}
            terraform init -no-color \\
            -backend-config="bucket=${backendBucket}" \\
            -backend-config="key=${appFolder}/${cloudEnv}/terraform.tfstate" \\
            -backend-config="region=${awsRegion}" \\
            -backend-config="profile=Default" \\
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
            export AWS_PROFILE=Default
            terraform plan -no-color \\
                -out ${appFolder}.tfplan \\
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
    try {
        sh """
        cd ${folder}
        export AWS_PROFILE=Default
        terraform apply --auto-approve \\
            "${appFolder}.tfplan"      \\
            -var aws_region=${awsRegion} \\
            -var env=${cloudEnv} 
        """
    } catch (Exception err) {
        returnError(err, "terraform apply failed. Please check your Terraform code.")
        throw err
    }
}


def terraformDestroy(String appFolder, String awsRegion, String cloudEnv) {
    String folder = getTerraformAppFolder(appFolder)
    try {
        sh """
        cd ${folder}
        export AWS_PROFILE=default
        terraform destroy --auto-approve \\
            -var aws_region=${awsRegion} \\
            -var env=${cloudEnv} 
        """
    } catch (Exception err) {
        returnError(err, "terraform apply failed. Please check your Terraform code.")
        throw err
    }
}

def returnError(err, message) {
    echo "$message Error: $err"
    echo err.getMessage()
}

String getTerraformAppFolder(String appFolder) {
    switch (appFolder) {
        case "database":
            appFolder = "terraform/database"
            break
        case "lambdas":
            appFolder = "terraform/lambdas"
            break
        case "core-resources":
            appFolder = "terraform/core-resources"
            break
        case "api":
            appFolder = "src"
            break
        default:
            throw new Exception("Invalid or unsupported app folder $appFolder")
    }

    return appFolder
}

return this