pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_IP',
               defaultValue: '',
               description: 'Private IP of Nexus server')

        string(name: 'TOMCAT_IP',
               defaultValue: '',
               description: 'Private IP of Tomcat server')

        string(name: 'RELEASE_VERSION',
               defaultValue: '1.0',
               description: 'Version to deploy (e.g. 1.0 or 1.1)')
    }

    environment {
        NEXUS_BASE_URL = "http://${params.NEXUS_IP}:8081"
        RELEASE_VERSION = 1.0.${BUILD_NUMBER}
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
                        mvn versions:set -DnewVersion=${RELEASE_VERSION}
                        mvn versions:commit
                        mvn deploy \
                        --settings \$MAVEN_SETTINGS \
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
