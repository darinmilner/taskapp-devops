def runPipeline() {
    def commonLib = evaluate readTrusted("Jenkins/Pipeline/common.groovy")
    def terraformLib = evaluate readTrusted("Jenkins/Pipeline/terraform.groovy")
    def lambdasLib = evaluate readTrusted("Jenkins/Pipeline/lambdas.groovy")
    def buildAPILib = evaluate readTrusted("Jenkins/Pipeline/buildAPI.groovy")
    def storageLib = evaluate readTrusted("Jenkins/Pipeline/storage.groovy")
    def loggerLib = evaluate readTrusted("Jenkins/Pipeline/loggr.groovy")
    def accessKey
    def secretKey
    def appFolder
    def backendBucket
    def awsRegion
    def pipelineAction
    def stateTable
    def cloudEnv

    parameters {
        choice(name: "APP_FOLDER", choices: ["core-resources", "lambdas", "database", "api"], description: "Folder where pipeline actions should run")
        choice(name: "PIPELINE_ACTION", choices: ["test", "lambdas", "deploy-api", "init-and-plan", "apply", "destroy"], description: "Pipeline deploy action")
        password(name: "AWS_ACCESS_KEY", description: "AWS ACCESS KEY")
        password(name: "AWS_SECRET_KEY", description: "AWS SECRET KEY")
        choice(name: "CLOUD_ENVIRONMENT", choices: ["dev", "test", "prod"], description: "Deployment cloud environment")
        choice(name: "AWS_REGION", choices: ["us-east-1", "us-east-2", "us-west-1", "ap-southeast-3"], description: "AWS Region to deploy resources and app")
    }

    stage("Set Variables") {
        steps {
            script {
                pipelineAction = params.PIPELINE_ACTION
                awsRegion = params.AWS_REGION
                appFolder = params.APP_FOLDER
                cloudEnv = params.CLOUD_ENVIRONMENT
                accessKey = params.AWS_ACCESS_KEY
                secretKey = params.AWS_SECRET_KEY

                backendBucket = commonLib.getBucketName(awsRegion)
                stateTable = commonLib.getDynamoDBStateTableName(awsRegion)

                echo "App Folder: $appFolder"
                echo "Backend State and storage bucket: $backendBucket"
                echo "DynamoDB state table Name: $stateTable"
                echo "Current AWS Region: $awsRegion"
                echo "Cloud Environment $cloudEnv"

                def log = load "logging.groovy"
                log.logInfo("Pipeline running")
                loggerLib.echoBanner("Pipeline running.")
            }
        }
    }

    stage("Update Node") {
        steps {
            script {
                sh """
                      apt-get update
                   """
                try {
                    sh """
                         terraform --version
                         aws --version
                       """
                } catch (err) {
                    echo "$err installing aws cli or Terraform in Docker"
                }
            }
        }
    }

    stage("Run API Tests") {
        steps {
            script {
                buildAPILib.runAPITests()
            }
        }
    }

    stage("Configure AWS Environment") {
        when {
            expression {
                return pipelineAction != "test"
            }
        }

        steps {
            script {
                commonLib.configureAWSProfile(awsRegion, accessKey, secretKey)
            }
        }
    }

    stage("Init and Plan") {
        when {
            expression {
                return pipelineAction != "test" || pipelineAction != "deploy-api"
            }
        }

        steps {
            script {
                terraformLib.terraformInit(backendBucket, appFolder, awsRegion, cloudEnv, stateTable, accessKey, secretKey)
                terraformLib.planTerraform(backendBucket, appFolder, awsRegion, cloudEnv)
            }
        }
    }

    stage("Build API Environment") {
        when {
            expression {
                return pipelineAction == "deploy-api" || pipelineAction == "build-and-deploy-api"
            }
        }

        steps {
            script {
                commonLib.buildKotlinEnvironment()
                storageLib.getAPIEnvFile(backendBucket)
            }
        }
    }
}
