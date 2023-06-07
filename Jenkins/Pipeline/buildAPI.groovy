def buildKotlinEnvironment() {
    echo "Building Kotlin"

    sh """
        java --version
    """
}

def runAPITests() {
    echo "Running API tests"

    try {
        sh "gradle clean test --info"
        sh "pwd"
        sh "cat build/reports/tests/index.html"
    } catch (Exception err) {
        echo "Error running test $err"
        //TODO: throw err
    }
}

return this