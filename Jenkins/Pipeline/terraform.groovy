def buildTerraformEnvironment() {
    echo "Installing Terraform"

    try {
        sh """
            curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip \\
            && curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_SHA256SUMS \\
            && curl https://keybase.io/hashicorp/pgp_keys.asc | gpg --import \\
            && curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_SHA256SUMS...
            export PATH=/usr/local/bin:$PATH
            export PATH=$PATH:/usr/local/terraform
            terraform --version
        """
    } catch (err) {
        returnError(err)
        throw err
    }

}

def terraformInit(backendBucket, appFolder, awsRegion, stateTable, accessKey, secretKey) {
    if (appFolder == "secret-manager") {
        appFolder = "terraform/secret-manager"
    } else if (appFolder == "lambdas") {
        appFolder = "terraform/lambdas"
    }

    echo "Initializing Terraform in folder $appFolder"

    try {
        sh """
            cd ${appFolder}
            terraform init -backend-config="bucket=${backendBucket}" \\
            -backend-config="key=${appFolder}/terraform.tfstate" \\
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

def returnError(err) {
    echo "Terraform init failed $err"
    echo err.getMessage()
}

return this