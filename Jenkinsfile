pipeline {
    agent any

    environment {
        WAR_NAME        = "Fionnspetitions.war"
        EC2_HOST        = "16.171.129.52"       // Your current EC2 Public IP
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
                    url: 'git@github.com:FionnSlattery/Fionnspetitions.git',
                    credentialsId: env.GIT_CRED
            }
        }

        stage('Build & Package (FAST)') {
            steps {
                // -B        = batch (non-interactive)
                // -DskipTests = do NOT run tests (faster)
                // clean package = produces WAR in target/
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Deploy Approval') {
            steps {
                input message: 'Deploy to EC2 Tomcat10?', ok: 'Deploy'
            }
        }

        stage('Deploy to Tomcat10') {
            steps {
                sshagent(credentials: [env.SSH_CRED]) {
                    sh """
                    echo 'Uploading WAR to EC2...'
                    scp -o StrictHostKeyChecking=no target/${WAR_NAME} ${EC2_USER}@${EC2_HOST}:/tmp/${WAR_NAME}

                    echo 'Deploying WAR into Tomcat10...'
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

