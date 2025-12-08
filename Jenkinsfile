@Library('Jenkins_library') _

pipeline {
   agent { label 'java' }
     // agent any
    tools {
        jdk 'JDK17'
        maven 'maven'
    }

   stages {

        stage('Checkout') {
            steps {
               checkout scm
            }
        }

        stage('Build') {
            steps {
                //sh 'mvn clean package -DskipTests=false'
               script {
                   // dir('hello-world-war') {
                    build 'package'
                }
            }
            }
      //  }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Run Application') {
            steps {
                  sh 'mvn spring-boot:run'
                  dir('/var/lib/jenkins/workspace/Parcel_service_feature-1/target') {
                   sh """
                     //   nohup java -jar simple-parcel-service-app-1.0-SNAPSHOT.jar > app.log 2>&1 &
                        //echo "Application started"
                   """
                }
            }
        }
    }
}
