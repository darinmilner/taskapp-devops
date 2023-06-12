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
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Terraform init failed $err")
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
                -var system_environment=${cloudEnv} 
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Terraform plan failed $err")
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
            -var system_environment=${cloudEnv} 
        """
    } catch (Exception err) {
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Terraform Apply failed $err")
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
        def errorLib = evaluate readTrusted("Jenkins/Pipeline/errors.groovy")
        echo "Pipeline is exiting! $err"
        errorLib.throwError(err, "Terraform destroy failed $err")
    }
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