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
                                        DockerImage: pipelineParams.dockerImage + pipelineParams.dockerVersion + pipelineParams.dockerDevTag,
                                        DockerContext: pipelineParams.dockerContext,
                                        DockerArgs: pipelineParams.dockerArgs)

                        // create git tag
                        gitLib.tag(Tag: pipelineParams.dockerVersion)

                        // push docker image
                        dockerLib.push(DockerImage: pipelineParams.dockerImage + pipelineParams.dockerVersion + pipelineParams.dockerDevTag)
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
                    branch 'main' 
                }
                steps {
                    script {
                        // promoter docker image from release to main
                        dockerLib.promoter(DockerImage: pipelineParams.dockerImage + pipelineParams.dockerQATag,
                                           DockerNewImage: pipelineParams.dockerNewImage + pipelineParams.dockerPRDTag )
                    }
                }
            }
        }
    }
}