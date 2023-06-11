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
        // sh "cat build/reports/tests/test/index.html"
    } catch (Exception err) {
        echo "Error running test $err"
        throw err
    }
}

return this