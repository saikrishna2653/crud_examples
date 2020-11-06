pipeline {
  agent { label 'master' }
   environment {
     HOST_NAME="3.237.40.210"
     def USER_ID="dockeradmin"
   }
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
    stage('Build Docker images'){
      steps {
      sshPublisher(publishers: [sshPublisherDesc(configName: 'tools-server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''docker stop department_employee || true; 
		docker rm -f department_employee || true; 
		docker image rm -f department_employee || true;
		 cd /opt/docker; 
		docker build -t department_employee .
		docker tag department_employee saikrishna2653/department_employee; 
		docker push saikrishna2653/department_employee; 
		docker image rm -f department_employee || true;
		docker rmi department_employee saikrishna2653/department_employee;
		''', 
		execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '//opt//docker', remoteDirectorySDF: false, removePrefix: '/target', sourceFiles: '**/*.war')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
      }
   }  
    stage('Copy manifest files to server') { 
	 steps {   
      dir('kubernetes-my-appln') {
        sh ''' 	
		   
          #
          # copy files to server
          #
          chmod +x k8s-deploy.sh
	      sed -i -e 's/\r$//' k8s-deploy.sh
          su dockeradmin -c './k8s-deploy.sh "${HOST_NAME}" "${USER_ID}"'
        '''		
      }
    }
  }

	stage('Deploy in to Kubernetes pods') { 
	 steps {   
      dir('kubernetes-my-appln') {
        sh ''' 	
		   
          #
          # copy files to server
          #
         su dockeradmin -c "$USER_ID@$HOST_NAME 'kubectl apply -f /opt/kubernetes_Deloy/crud-app-deploy.yml; kubectl apply -f /opt/kubernetes_Deloy/crud-app-service.yml'"
        '''		
      }
    }
  }

  }
}
