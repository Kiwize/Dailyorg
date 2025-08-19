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

        stage('Prepare .env') {
            steps {
                withCredentials([file(credentialsId: 'dailyorg-env', variable: 'DOTENV_FILE')]) {
                    sh """
                        echo "Copie du fichier .env depuis les credentials Jenkins"
                        cp $DOTENV_FILE .env
                    """
                }
            }
        }

        stage('Build & Launch Docker Compose') {
            steps {
                sh 'docker-compose --env-file .env down'

                dir('eclipse-workspace/dailyorg_java') {
                    sh 'mvn clean'
                    sh 'mvn install'
                }

                sh 'docker-compose --env-file .env up --build -d'
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
                      -Dsonar.sources=eclipse-workspace/dailyorg_java/src \
                      -Dsonar.java.binaries=eclipse-workspace/dailyorg_java/target/classes
                    """
                }
            }
        }

        stage('Backend Tests') {
            steps {
                dir('eclipse-workspace/dailyorg_java') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
            script {
                sh 'docker-compose --env-file .env down'
            }
        }
    }
}
