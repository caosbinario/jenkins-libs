def build(Map params){
    sh "docker build -f ${params.DockerfilePath} -t ${params.DockerImage} ${params.DockerContext}"
}

def push(Map params){
    sh "docker push ${params.DockerImage}"
}