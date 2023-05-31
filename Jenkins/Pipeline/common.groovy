def getBucketName(awsRegion) {
    String bucketName = null
    switch (awsRegion) {
        case "us-west-1":
            bucketName == "taskapp-bucket-uswest1"
            return bucketName
        case "us-east-1":
            bucketName == "taskapp-bucket-useast1"
            return bucketName
        default:
            throw new Exception("Invalid or unsupported region $awsRegion")
    }
}

def buildLambdaEnvironment() {
    echo "Building Lambda Environment"
}

def buildPythonEnvironment() {
    echo "Installing Python environment"
    sh """
        apt update
        apt install python3-pip -y
        python3 --version
        pip3 install -r requirements.txt
    """
}

def buildKotlinEnvironment() {
    echo "Building Kotlin"
    sh """
    java --version
    """
}

return this