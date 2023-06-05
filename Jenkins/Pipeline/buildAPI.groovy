def buildKotlinEnvironment() {
    echo "Building Kotlin"
    sh """
        java --version
    """
}

def installGradle() {
    echo "Installing gradle"

    sh """
        wget -c https://services.gradle.org/distributions/gradle-${env.GRADLE_VERSION}-bin.zip -P /tmp
        ls /tmp
        unzip -d /opt/gradle /tmp/gradle-${env.GRADLE_VERSION}-bin.zip
        ls /opt/gradle
        export GRADLE_HOME="/opt/gradle/gradle-${env.GRADLE_VERSION}"
        export PATH="\${GRADLE_HOME}/bin:\${PATH}"
        gradle --version
       
    """
}

def runAPITests() {
    echo "Running API tests"
    sh "gradle test"
}

return this