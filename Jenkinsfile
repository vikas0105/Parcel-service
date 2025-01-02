pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Set up Java') {
            steps {
                script {
                    // Ensure Java 17 is available in your Jenkins agent setup
                    sh 'java -version'
                }
            }
        }

        stage('Cache Maven Dependencies') {
            steps {
                script {
                    def cacheDir = "${env.HOME}/.m2/repository"
                    if (fileExists(cacheDir)) {
                        echo "Maven cache found at ${cacheDir}"
                    } else {
                        echo "No Maven cache found. Dependencies will be downloaded."
                    }
                }
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Run Spring Boot App') {
            steps {
                script {
                    sh 'mvn spring-boot:run &'
                    sleep 15 // Wait for the Spring Boot app to start
                }
            }
        }

        stage('Validate App is Running') {
            steps {
                script {
                    def response = sh(script: "curl --write-out '%{http_code}' --silent --output /dev/null http://localhost:8080", returnStdout: true).trim()
                    if (response != '200') {
                        error "The app failed to start. HTTP response code: ${response}"
                    } else {
                        echo "The app is running successfully!"
                    }
                }
            }
        }

        stage('Wait for 5 Minutes') {
            steps {
                sleep time: 5, unit: 'MINUTES'
            }
        }

        stage('Stop Spring Boot App') {
            steps {
                script {
                    sh 'mvn spring-boot:stop'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed!'
        }
    }
}
