pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'http://NEXUS_IP:8081', description: 'Nexus server URL')
        string(name: 'NEXUS_REPO', defaultValue: 'maven-snapshots', description: 'Target Nexus repository')
    }

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
                nexusVersion: 'nexus3',
                protocol: 'http',
                nexusUrl: params.NEXUS_URL.replace('http://', '').replace('https://', ''),
                repository: params.NEXUS_REPO,
                groupId: pom.groupId,
                artifactId: pom.artifactId,
                version: pom.version,
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
