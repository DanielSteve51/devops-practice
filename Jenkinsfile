pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    // environment {
    //     NEXUS_URL  = "http://<NEXUS_IP>:8081"
    //     NEXUS_REPO = "maven-snapshots"
    // }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Upload to Nexus') {
    steps {
        script {
            def pom = readMavenPom file: 'pom.xml'

            nexusArtifactUploader(
                nexusUrl: "${env.NEXUS_URL}",
                repository: "${env.NEXUS_REPO}",
                groupId: pom.groupId,
                artifactId: pom.artifactId,
                version: pom.version + "-${env.BUILD_NUMBER}",
                credentialsId: "nexus-creds",
                artifacts: [
                    [artifactId: pom.artifactId, classifier: "", file: "target/${pom.artifactId}.war", type: "war"]
                ]
            )
        }
    }
}

    }
}
