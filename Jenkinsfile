pipeline {
    agent any


    environment {
        RELEASE_VERSION = "1.${BUILD_NUMBER}"
        APP_NAME = "java-maven-webapp"
        AWS_REGION = "ap-south-2"
        ECR_REGISTRY = "199264265839.dkr.ecr.ap-south-2.amazonaws.com"
        ECR_REPO = "${ECR_REGISTRY}/${APP_NAME}"
        ECS_CLUSTER = "java-maven-cluster"
        ECS_SERVICE = "java-maven-service"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Building code') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Code Quality Scan') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh 'mvn sonar:sonar'
                timeout(time: 3, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // stage('Quality Gate') {
        //     steps {
        //         timeout(time: 3, unit: 'MINUTES') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }

        stage('Docker Build'){
            steps{
                script{
                    sh """
                    docker build -t ${APP_NAME}:${BUILD_NUMBER} .
                    docker tag ${APP_NAME}:${BUILD_NUMBER} ${ECR_REPO}:${BUILD_NUMBER}
                    docker tag ${APP_NAME}:${BUILD_NUMBER} ${ECR_REPO}:latest
                    """
                }
            }
        }

        stage('Image upload to ECR'){
            steps{
                script{
                    sh """
                    docker push ${ECR_REPO}:${BUILD_NUMBER}
                    docker push ${ECR_REPO}:latest
                    """
                }
            }
        }

            stage('Deploying on ECS'){
                steps{
                    script{
                        sh """
                        aws ecs update-service \
                            --cluster ${ECS_CLUSTER} \
                            --service ${ECS_SERVICE} \
                            --force-new-deployment \
                            --region ${AWS_REGION}
                        docker rmi ${APP_NAME}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REPO}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REPO}:latest || true
                        """
                    }
                }
            }

        // stage('Cleanup Local Images') {
        //     steps {
        //         script {
        //             sh """
        //             docker rmi ${APP_NAME}:${BUILD_NUMBER} || true
        //             docker rmi ${ECR_REPO}:${BUILD_NUMBER} || true
        //             docker rmi ${ECR_REPO}:latest || true
        //             """
        //         }
        //     }
        // }



    }

        post {
        success {
            echo "Deployed to ECS: ${ECS_CLUSTER}/${ECS_SERVICE}"
        }
        failure {
            echo "Pipeline failed at some stage"
        }
    }
}
