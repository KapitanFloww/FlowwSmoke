pipeline {
    agent {
        docker {
            image 'maven.3.8.1-adoptopenjdk-17'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Checkout') {
            checkout scm
        }
        stage('Check Java') {
            sh "java -version"
        }
        stage('Build') {
            sh 'mvn -B -DskipTests clean package'
        }
        stage('Archive Artifacts') {
            archiveArtifacts artifacts: '**/target/*.jar'
        }
    }
}