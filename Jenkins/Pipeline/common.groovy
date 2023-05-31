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