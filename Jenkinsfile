pipeline {
  agent { label 'master' }
  stages {
    stage('Source') { // Get code
      steps {
        // get code from our Git repository
        git 'https://github.com/saikrishna2653/crud_examples.git'
      }
    }
    stage('Compile') { // Compile and do unit testing
      tools {
        maven 'maven3'
      }
      steps {
        // run Gradle to execute compile and unit testing
        sh 'mvn clean install package'
      }
    }
    stage('Deploy to Server'){
      steps {
      sshPublisher(publishers: [sshPublisherDesc(configName: 'tools-server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''docker stop department_employee || true; 
		docker rm -f department_employee || true; 
		docker image rm -f department_employee || true;
		 cd /opt/docker; 
		docker build -t department_employee .
		docker run -d --name department_employee -p 8090:8080 department_employee''', 
		execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '//opt//docker', remoteDirectorySDF: false, removePrefix: '/target', sourceFiles: '**/*.war')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
      }
   }
  }
}
