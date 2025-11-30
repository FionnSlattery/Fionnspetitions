pipeline {
    agent any

    environment {
        WAR_NAME        = "Fionnspetitions.war"
        EC2_HOST        = "13.53.174.137"       // <-- your EC2 updated IP
        EC2_USER        = "ubuntu"

        // Tomcat 10 deployment directory
        TOMCAT_WEBAPPS  = "/var/lib/tomcat10/webapps"

        // Jenkins credential IDs
        GIT_CRED        = "github-creds"
        SSH_CRED        = "ec2-ssh-key"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/FionnSlattery/Fionnspetitions.git',
                    credentialsId: env.GIT_CRED
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package WAR') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: "target/${WAR_NAME}", fingerprint: true
            }
        }

        stage('Deploy Approval') {
            steps {
                input message: 'Deploy WAR to EC2 Tomcat10?', ok: 'Deploy'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sshagent(credentials: [env.SSH_CRED]) {
                    sh """
                    echo 'Copying WAR to EC2...'
                    scp -o StrictHostKeyChecking=no target/${WAR_NAME} ${EC2_USER}@${EC2_HOST}:/tmp/${WAR_NAME}

                    echo 'Deploying WAR on Tomcat10...'
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                        sudo rm -rf ${TOMCAT_WEBAPPS}/Fionnspetitions*
                        sudo mv /tmp/${WAR_NAME} ${TOMCAT_WEBAPPS}/${WAR_NAME}
                        sudo systemctl restart tomcat10
                    '
                    """
                }
            }
        }
    }
}
