pipeline {
    agent any

    environment {
        SONARQUBE = 'SonarQube-local'
        SCANNER_HOME = tool 'SonarQubeScanner'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'dev', url: 'https://github.com/Kiwize/Dailyorg.git'
            }
        }

        stage('Build & Launch Docker Compose') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }

        stage('Wait Services Ready') {
            steps {
                echo 'Attente des services…'
                sleep time: 20, unit: 'SECONDS'
            }
        }

        stage('SonarQube Analysis (Java Backend)') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    sh """
                      ${SCANNER_HOME}/bin/sonar-scanner \
                      -Dsonar.projectKey=Dailyorg-backend \
                      -Dsonar.sources=eclipse-workspace/dailyorg-java/src \
                      -Dsonar.java.binaries=eclipse-workspace/dailyorg-java/target/classes
                    """
                }
            }
        }

        stage('Backend Tests') {
            steps {
                dir('eclipse-workspace/dailyorg-java') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
            script {
                sh 'docker-compose down'
            }
        }
    }
}
