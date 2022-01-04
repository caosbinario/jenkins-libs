def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('docker build') {
                steps {
                    script {
                        sh "docker build -f 02-primer-pipeline/Dockerfile -t caosbinario/homer_page:1.0.0-${BUILD_ID} 02-primer-pipeline"
                    }
                }
            }
            stage('docker push') {
                steps {
                    script {
                        sh "docker push caosbinario/homer_page:1.0.0-${BUILD_ID}"
                    }
                }
            }
        }
    }
}