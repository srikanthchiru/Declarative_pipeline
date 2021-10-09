pipeline {
    agent any

    stages {
        stage('git clone') {
            steps {
                git url: 'https://github.com/srikanthchiru/boxfuse-sample-java-war-hello.git'
            }
        }
        stage('Build') {
            steps {
                 sh """ mvn clean package """
            }
        }
         stage('Artifacts saving') {
            steps {
                 sh """ aws s3 cp /var/lib/jenkins/workspace/scripted/target/hello-*.war s3://bjym/"""
            }
        }
        stage('deploy') {
            steps {
                 sh """ scp -i /tmp/sreedev.pem /var/lib/jenkins/workspace/scripted/target/hello-*.war ec2-user@$serverip:/tmp/
                        scp -i /tmp/sreedev.pem /tmp/tomcat.sh ec2-user@$serverip:/tmp/
                        ssh -i /tmp/sreedev.pem ec2-user@$serverip "sudo bash /tmp/tomcat.sh && sudo cp /tmp/hello-*.war /var/lib/tomcat/webapps/"  """
            }
        }
    }
}

