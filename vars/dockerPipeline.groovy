def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('docker build') {
                steps {
                    script {
                        dockerLib.build(DockerfilePath: pipelineParams.dockerfilePath,
                                        DockerImage: pipelineParams.dockerImage,
                                        DockerContext: pipelineParams.dockerContext)
                    }
                }
            }
            stage('docker push') {
                steps {
                    script {
                        dockerLib.push(DockerImage: pipelineParams.dockerImage)
                    }
                }
            }
        }
    }
}