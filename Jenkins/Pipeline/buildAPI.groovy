def buildKotlinEnvironment() {
    echo "Building Kotlin"
    sh """
    java --version
    """
}

def runAPITests() {
    echo "Running API tests"
}

return this