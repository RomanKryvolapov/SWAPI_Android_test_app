pipeline {

    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk"
        ANDROID_HOME = "/opt/android-sdk"
        PATH = "${env.PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools"
    }

    tools {
        gradle 'Gradle-8'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/RomanKryvolapov/SWAPI_Android_test_app.git'
            }
        }

        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }

        stage('Build All Modules') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Assemble Android') {
            steps {
                sh './gradlew :android:assembleDebug'
            }
        }

        stage('Archive APKs and JARs') {
            steps {
                archiveArtifacts artifacts: '**/build/outputs/**/*.apk', allowEmptyArchive: true
                archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo 'Build successful'
        }
        failure {
            echo 'Build failed'
        }
    }

}
