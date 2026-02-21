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

        stage('Deploy to Tomcat') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    ),
                    usernamePassword(
                        credentialsId: 'tomcatManager-creds',
                        usernameVariable: 'TOMCAT_USER',
                        passwordVariable: 'TOMCAT_PASS'
                    )
                ]) {

                    sh '''
                    echo "Downloading WAR from Nexus..."

                    curl -f -u $NEXUS_USER:$NEXUS_PASS \
                    "$NEXUS_BASE_URL/repository/maven-releases/com/daniel/test/java_maven_webApp/$RELEASE_VERSION/java_maven_webApp-$RELEASE_VERSION.war" \
                    -o app.war

                    echo "Deploying to Tomcat..."

                    curl -f -u $TOMCAT_USER:$TOMCAT_PASS \
                    -T app.war \
                    "http://$TOMCAT_IP:8080/manager/text/deploy?path=/java_maven_webApp&update=true"

                    echo "Deployment completed."
                    '''
                }
            }
        }
    }
}
