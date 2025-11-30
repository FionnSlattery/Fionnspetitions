pipeline {
    agent any

    environment {
        // This is the actual name Maven produces by default:
        // target/Fionnspetitions-0.0.1-SNAPSHOT.war
        WAR_NAME       = "Fionnspetitions-0.0.1-SNAPSHOT.war"

        EC2_HOST       = "13.61.105.96"          // current EC2 IP
        EC2_USER       = "ubuntu"
        TOMCAT_WEBAPPS = "/var/lib/tomcat10/webapps"

        GIT_CRED       = "github-creds"
        SSH_CRED       = "ec2-ssh-key"
    }

    stages {

        // Checkout is handled by "Declarative: Checkout SCM" from job config.

        stage('Build & Package (FAST)') {
            steps {
                // -B = batch mode, -DskipTests = faster build
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
                // Get SSH key from Jenkins credentials
                withCredentials([sshUserPrivateKey(credentialsId: env.SSH_CRED,
                                                  keyFileVariable: 'EC2_KEY')]) {

                    // Use single-quoted Groovy string so Jenkins doesn’t try to interpolate EC2_KEY itself
                    sh '''
                    echo "Uploading WAR to EC2..."
                    scp -i "$EC2_KEY" -o StrictHostKeyChecking=no target/'"${WAR_NAME}"' '"${EC2_USER}"'@'"${EC2_HOST}"':/tmp/'"${WAR_NAME}"'

                    echo "Deploying WAR into Tomcat10..."
                    ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no '"${EC2_USER}"'@'"${EC2_HOST}"' <<EOF
                        sudo rm -rf ${TOMCAT_WEBAPPS}/Fionnspetitions*
                        sudo mv /tmp/'"${WAR_NAME}"' ${TOMCAT_WEBAPPS}/'${WAR_NAME}'
                        sudo systemctl restart tomcat10
                    EOF
                    '''
                }
            }
        }
    }
}

