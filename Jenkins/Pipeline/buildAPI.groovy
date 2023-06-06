def buildKotlinEnvironment() {
    echo "Building Kotlin"

    sh """
        java --version
    """
}

def runAPITests() {
    echo "Running API tests"

    script {
        sh "gradle clean test"
    }
}

return this