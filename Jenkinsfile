 pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
		sh 'mkdir -p libs'
                sh 'cp ../../libraries/*.jar ./libs'
            
                sh 'gradle clean build curseforge236541' 
                archiveArtifacts artifacts: '**output/*.jar', fingerprint: true 
            }
        }
    }
}
