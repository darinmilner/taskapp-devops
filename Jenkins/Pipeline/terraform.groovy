def buildTerraformEnvironment() {
    echo "Installing Terraform"

    try {
        sh """            
            # Download terraform for linux
            wget --progress=dot:mega https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip
            # Unzip
            unzip -o terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip 
            # Move to local bin
            mv terraform /usr/local/bin/ 
            # Make it executable
            chmod +x /usr/local/bin/terraform 
            # Check that it's installed
            terraform --version
        """
    } catch (err) {
        returnError(err)
        throw err
    }
}

def buildTerraformEnvironmentOld() {
    echo "Installing Terraform"

    try {
        sh """
            curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip \\
            && curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_SHA256SUMS \\
            && curl https://keybase.io/hashicorp/pgp_keys.asc | gpg --import \\
            && curl -Os https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_SHA256SUMS...
            ls -la
            mkdir -p ~/bin/terraform
            mv terraformfolder ~/bin/terraform
            chmod +X 775 ~/bin/terraform
            cd ~/bin/terraform
            unzip terraform
            export PATH=/usr/local/bin/terraform:$PATH
            export PATH=$PATH:/usr/local/terraform
            terraform --version
        """
    } catch (err) {
        returnError(err)
        throw err
    }
}

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
        echo "terraform plan"
    """
}

def returnError(err) {
    echo "Terraform init failed $err"
    echo err.getMessage()
}

return this