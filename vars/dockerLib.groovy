def build(Map params){
    sh "docker build -f ${params.DockerfilePath} -t ${params.DockerImage} ${params.DockerArgs} ${params.DockerContext}"
}

def push(Map params){
    sh "docker push ${params.DockerImage}"
}