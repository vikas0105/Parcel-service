pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven (Docker)') {
            steps {
                script {
                    // Run Maven inside a Docker container
                    sh '''
                    docker run --rm -v $(pwd):/mnt -w /mnt maven:3.8.1-jdk-17 mvn clean install -DskipTests
                    '''
                }
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
