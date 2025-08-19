pipeline {
    agent any

    environment {
        SONARQUBE = 'SonarQube-local'
        SCANNER_HOME = tool 'SonarQubeScanner'

        JWT_SECRET= credentials('jwt-secret')
        DB_USERNAME= credentials('db-username')
        DB_PASSWORD= credentials('db-password')
        DB_NAME= credentials('db-name')
        DB_URL= credentials('db-url')
        ALLOWED_ORIGINS= credentials('allowed-origins')
        REDIS_HOST= credentials('redis-host')
        REDIS_PASSWORD= credentials('redis-password')
        REDIS_PORT= credentials('redis-port')
        APP_API_URL= credentials('app-api-url')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'dev', url: 'https://github.com/Kiwize/Dailyorg.git'
            }
        }

        stage('Prepare .env') {
            steps {
                sh """
                cat > .env <<EOF
                JWT_SECRET=${JWT_SECRET}
                DB_USERNAME=${DB_USERNAME}
                DB_PASSWORD=${DB_PASSWORD}
                DB_NAME=${DB_NAME}
                DB_URL=${DB_URL}
                ALLOWED_ORIGINS=${ALLOWED_ORIGINS}
                REDIS_HOST=${REDIS_HOST}
                REDIS_PASSWORD=${REDIS_PASSWORD}
                REDIS_PORT=${REDIS_PORT}
                APP_API_URL=${APP_API_URL}
                EOF
                """
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
