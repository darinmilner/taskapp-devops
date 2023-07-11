def buildPythonEnvironment() {
    echo "Installing Python environment"
    sh """
        apt update
        apt install python3-pip -y
        python3 --version
        pip3 install -r requirements.txt
    """
}

def buildLambdaEnvironment() {
    echo "Building Lambda Environment"
}

def invokeGetAPICodeLambda(String region, String functionName, String bucket, String latestFolder) {
    try {
        sh """
        aws configure set region $region
        aws lambda invoke --function-name $functionName  \\
            --payload '{
                "uploadRegion" : $region,
                "uploadBucket" : $bucket,
                "latestFolder" : $latestFolder
            }' format.json  --profile Default
        """
    } catch (Exception err) {
        echo "An error occured invoking lambda $functionName"
        throw err
    }

}

return this