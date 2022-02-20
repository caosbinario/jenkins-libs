def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('Build') {
                when {
                    branch 'develop' 
                }
                steps {
                    script {
                        // build docker image
                        dockerLib.build(DockerfilePath: pipelineParams.dockerfilePath,
                                        DockerImage: pipelineParams.dockerImage + pipelineParams.dockerDevTag,
                                        DockerContext: pipelineParams.dockerContext,
                                        DockerArgs: pipelineParams.dockerArgs)

                        // push docker image
                        dockerLib.push(DockerImage: pipelineParams.dockerImage + pipelineParams.dockerDevTag)
                    }
                }
            }
            stage('Promoter for QA') {
                when {
                    branch 'release' 
                }
                steps {
                    script {
                        // promoter docker image from develop to release
                        dockerLib.promoter(DockerImage: pipelineParams.dockerImage + pipelineParams.dockerDevTag,
                                           DockerNewImage: pipelineParams.dockerImage + pipelineParams.dockerQATag )
                    }
                }
            }
            stage('Promoter for PROD') {
                when {
                    branch 'master' 
                }
                steps {
                    script {
                        // promoter docker image from release to master
                        dockerLib.promoter(DockerImage: pipelineParams.dockerImage + pipelineParams.dockerQATag,
                                           DockerNewImage: pipelineParams.dockerNewImage + pipelineParams.dockerPRDTag )
                    }
                }
            }
        }
    }
}