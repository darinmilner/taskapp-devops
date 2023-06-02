def installAWSCLI() {
    echo "Installing AWS CLI"
    sh """
        curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
       # unzip awscliv2.zip
        unzip -f awscliv2.zip
        ./aws/install 
        which aws
        aws --version
    """
}

def buildTerraformEnvironment() {
    echo "Installing Terraform"

    try {
        sh """            
            # Download terraform for linux
            wget --progress=dot:mega https://releases.hashicorp.com/terraform/${env.TERRAFORM_VERSION}/terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip
            # Unzip
            unzip -f terraform_${env.TERRAFORM_VERSION}_linux_amd64.zip 
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

return this