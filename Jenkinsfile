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

        // Jenkins will already do "Declarative: Checkout SCM" automatically
        // using the repo + credentials configured in the job.

        stage('Build & Package (FAST)') {
            steps {
                // -B = batch mode (no prompts)
                // -DskipTests = skip tests to keep build quick
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

