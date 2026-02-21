pipeline {
    agent any

     parameters {
        string(name: 'NEXUS_IP',
               defaultValue: '',
               description: 'Private IP of Nexus server')
         string(name: 'TOMCAT_IP',
               defaultValue: '',
               description: 'Private IP of Tomcat server')
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
                timeout(time: 1, unit: 'MINUTES') {
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
                        mvn deploy \
                        --settings $MAVEN_SETTINGS \
                        -Dnexus.release.url=${NEXUS_BASE_URL}/repository/maven-releases/ \
                        -Dnexus.snapshot.url=${NEXUS_BASE_URL}/repository/maven-snapshots/ \
                        -DskipTests
                        """
                    }
                }
            }
        }

        stage('Deploy to Tomcat') {
    steps {
        sshagent(['tomcat-ssh-key']) {
            withCredentials([usernamePassword(
                credentialsId: 'nexus-creds',
                usernameVariable: 'NEXUS_USER',
                passwordVariable: 'NEXUS_PASS'
            )]) {

                sh """
                ssh ubuntu@${params.TOMCAT_IP} '
                curl -u ${NEXUS_USER}:${NEXUS_PASS} \
                http://${params.NEXUS_IP}:8081/repository/maven-releases/com/daniel/test/java_maven_webApp/1.0/java_maven_webApp-1.0.war \
                -o /opt/tomcat/webapps/java_maven_webApp.war
                '
                """
            }
        }
    }
}
    }
}
