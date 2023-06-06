def buildKotlinEnvironment() {
    echo "Building Kotlin"

    exportGradlePath()

    sh """
        java --version
    """
}

def exportGradlePath() {
    echo "Exporting Gradle Path"

    try {
        sh """
         export GRADLE_HOME="/opt/gradle/gradle-${env.GRADLE_VERSION}"
         export PATH="\${GRADLE_HOME}/bin:\${PATH}"
         gradle --version
       """
    } catch (Exception err) {
        echo "Error exporting Gradle Path $err"
        throw err
    }
}

def installGradle() {
    echo "Installing gradle"

    try {
        sh """
            wget -c https://services.gradle.org/distributions/gradle-${env.GRADLE_VERSION}-bin.zip -P /tmp
            ls /tmp
            unzip -d /opt/gradle /tmp/gradle-${env.GRADLE_VERSION}-bin.zip
            ls /opt/gradle    
        """
    } catch (Exception err) {
        echo "Error installing Gradle $err"
        throw err
    }

    exportGradlePath()
}

def runAPITests() {
    echo "Running API tests"

    exportGradlePath()

    sh "gradle test"
}

return this