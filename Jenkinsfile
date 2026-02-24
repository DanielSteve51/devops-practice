pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_IP',
               defaultValue: '',
               description: 'Private IP of Nexus server')
    }

    environment {
        NEXUS_BASE_URL = "http://${params.NEXUS_IP}:8081"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-creds',
                    usernameVariable: 'NEXUS_USERNAME',
                    passwordVariable: 'NEXUS_PASSWORD'
                )]) {

                    configFileProvider([configFile(
                        fileId: 'maven-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {

                        sh """
                        mvn clean deploy \
                        --settings \$MAVEN_SETTINGS \
                        -Drevision=1.5 \
                        -Dnexus.release.url=${NEXUS_BASE_URL}/repository/maven-releases/ \
                        -Dnexus.snapshot.url=${NEXUS_BASE_URL}/repository/maven-snapshots/ \
                        -DskipTests
                        """
                    }
                }
            }
        }
    }
}
