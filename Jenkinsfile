pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'  // Adjust if needed
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
                    // Verify Java 17 is available
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
                // Use Maven to build the project
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Spring Boot App') {
            steps {
                // Run the Spring Boot app in the background
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
