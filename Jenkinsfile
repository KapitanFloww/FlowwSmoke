pipeline {
    agent any
    tools {
        maven 'Maven 3.8.6'
        jdk 'jdk17'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo "JAVA_HOME = ${JAVA_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage ('Archive Artifacts') {
            steps {
                archiveArtifacts allowEmptyArchive: true, artifacts: 'target/FlowwSmoke-*.jar'
            }
        }
    }
}