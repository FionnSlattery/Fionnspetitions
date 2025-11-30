pipeline {
    agent any

    environment {
        WAR_NAME       = "Fionnspetitions.war"
        EC2_HOST       = "13.61.105.96"          // current EC2 IP
        EC2_USER       = "ubuntu"
        TOMCAT_WEBAPPS = "/var/lib/tomcat10/webapps"

        GIT_CRED       = "github-creds"
        SSH_CRED       = "ec2-ssh-key"
    }

    stages {

        // Jenkins does "Declarative: Checkout SCM" automatically using job config.

        stage('Build & Package (FAST)') {
            steps {
                // -B = batch mode, -DskipTests = no tests (faster)
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
                // Use Jenkins SSH credentials to get a key file we can use with ssh/scp
                withCredentials([sshUserPrivateKey(credentialsId: env.SSH_CRED,
                                                  keyFileVariable: 'EC2_KEY')]) {

                    sh """
                    echo 'Uploading WAR to EC2...'
                    scp -i "$EC2_KEY" -o StrictHostKeyChecking=no target/${WAR_NAME} ${EC2_USER}@${EC2_HOST}:/tmp/${WAR_NAME}

                    echo 'Deploying WAR into Tomcat10...'
                    ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
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

