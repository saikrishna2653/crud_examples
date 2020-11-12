pipeline {
  agent { label 'master' }
   environment {
     HOST_NAME="35.170.73.113"
     DOCKER_HOST="100.26.99.122"
     USER_ID="dockeradmin"
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
   stage('Build and Push Docker images to Hub'){
	steps {  
      sshagent(['deploy_user']) {
         sh '''		 		
	 ssh $USER_ID@$DOCKER_HOST docker image rm -f department_employee || true;
	 ssh $USER_ID@$DOCKER_HOST docker build -t department_employee /opt/docker;  
         ssh $USER_ID@$DOCKER_HOST docker tag department_employee saikrishna2653/department_employee;  
         ssh $USER_ID@$DOCKER_HOST docker push saikrishna2653/department_employee; 	
         '''
         }	  
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
	 su dockeradmin -c 'ssh $USER_ID@$HOST_NAME "kubectl delete -f /opt/kubernetes_Deloy/crud-app-service.yml; kubectl delete -f /opt/kubernetes_Deloy/crud-app-deploy.yml;"' 
         su dockeradmin -c 'ssh $USER_ID@$HOST_NAME "kubectl apply -f /opt/kubernetes_Deloy/crud-app-deploy.yml; kubectl apply -f /opt/kubernetes_Deloy/crud-app-service.yml"'
        '''		
      }
    }
  }

  }
}
