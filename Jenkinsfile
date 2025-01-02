pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token')
        SONAR_ORG = 'vikas0105'
        SONAR_HOST_URL = 'https://sonarcloud.io'
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Set Up Java') {
            steps {
                script {
                    // Install Java 17 if not already installed
                    sh 'sudo apt update'
                    sh 'sudo apt install openjdk-17-jdk -y'
                    sh 'java -version'
                }
            }
        }

        stage('Cache Maven Dependencies') {
            steps {
                script {
                    // Cache Maven dependencies if required
                    sh 'mkdir -p ~/.m2/repository'
                }
            }
        }

        stage('Build with Maven') {
            steps {
                // Use Maven without specifying the version from Jenkins' Global Tool Configuration
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Spring Boot App') {
            steps {
                sh 'mvn spring-boot:run &'
            }
        }

        stage('Wait for App to Start') {
            steps {
                script {
                    echo 'Waiting for the app to start...'
                    sleep 15
                }
            }
        }

        stage('Validate App is Running') {
            steps {
                script {
                    def response = sh(script: 'curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:8080', returnStdout: true).trim()
                    if (response != '200') {
                        error "App is not running. HTTP response code: ${response}"
                    } else {
                        echo "The app is running successfully!"
                    }
                }
            }
        }

        stage('Wait for 5 Minutes') {
            steps {
                script {
                    echo 'Waiting for 5 minutes before stopping the app...'
                    sleep 300
                }
            }
        }

        stage('Gracefully Stop Spring Boot App') {
            steps {
                sh 'mvn spring-boot:stop'
            }
        }

        stage('SonarCloud Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                            -Dsonar.organization=${env.SONAR_ORG} \
                            -Dsonar.host.url=${env.SONAR_HOST_URL} \
                            -Dsonar.projectKey=vikas0105_Parcel-service \
                            -Dsonar.login=${env.SONAR_TOKEN}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up after the build.'
        }

        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }
    }
}
