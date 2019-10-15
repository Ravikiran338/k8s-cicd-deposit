@Library('pipeline-shared-library') _
pipeline {
	agent any
	tools { 
        maven 'Maven'  
          }
	environment {
     DOCKERHUB_USERNAME = "ravi338"
     APP_NAME = "msa-banking-aws"
	 SERVICE_NAME = "deposit"
     
     REPOSITORY_TAG="${DOCKERHUB_USERNAME}/${APP_NAME}-${SERVICE_NAME}:${BUILD_ID}"
	 }
	stages {
	         stage ('scm checkout') {
			       steps {
				   script{
                env.STAGE = "scm checkout"
                         }
				    cleanWs()
					checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub-Ravi', url: 'https://github.com/Ravikiran338/k8s-cicd-deposit.git']]])
	                      }
                    }						  
			 stage ('Build and Push the Docker Image') {
			       steps {
				   script{
                env.STAGE = "Build and Push the Docker Image"
                         }
				           sh label: '', script: '''cd ${WORKSPACE}
                                                    mvn clean install
			                                     '''
					     }
					}					
			 stage ('Deployment using Kubernetes') {
			      steps {
				  script{
                env.STAGE = "Deployment using Kubernetes"
                        }
				     sh label: '', script: '''
                          cd ${WORKSPACE}					 
					      docker build -t ${REPOSITORY_TAG} .
					      docker login
                          docker push ${REPOSITORY_TAG}
					      export KUBECONFIG=~/.kube/kube-config-eks	
					      export PATH=$HOME/bin:$PATH
					      echo `kubectl get svc`
					      echo `kubectl get nodes`
					      envsubst < ${WORKSPACE}/${SERVICE_NAME}.yaml | kubectl apply -f -
					'''										   
				}
			}
         stage ('testing scm checkout') {
                      steps {
                script{
                env.STAGE = "testing scm checkout"
                }
                       cleanWs()
                       checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub-Ravi', url: 'https://github.com/Ravikiran338/k8s-deposit-testscripts.git']]])
                             }
                       }

		   stage ('test automation execution') {
                   steps {
			   script{
			   env.STAGE = "test automation execution"
						  try{
                           sh label: '', script: '''
						                            cd ${WORKSPACE}
						                            chmod +x drivers/*
                                                    mvn clean install
                    
                                                 '''
												 currentBuild.result = 'SUCCESS'
												                      echo "Service Deployed Successfully"
							}
				                                 catch(Exception e){
					                             currentBuild.result = 'FAILURE'

					                             sh label: '', script: '''
					                                                   echo "Rolling back Deployment "
		                                                               export KUBECONFIG=~/.kube/kube-config-eks
		                                                               export PATH=$HOME/bin:$PATH
		                                                               kubectl rollout undo deployment.apps/depositservice
                                                                       '''
				                                                   }
				        echo "${currentBuild.result}"
								 }
						 }
				 }
		 }
post
   {
     success
       {
         slackSend color: "good", message: "Hi , Microservices Build SUCCESS with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild("SUCCESS", env.STAGE)
       }
     failure
       {
         slackSend color: "danger", message: "Hi Microservices Build FAILED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('FAILED', env.STAGE)
       }
     aborted
       {
         slackSend color: "danger", message: "Hi , Microservices Build ABORTED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('ABORTED', env.STAGE)
       }
     unstable
       {
         slackSend color: "warning", message: "Hi , Microservices Build FAILED with your changes - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)", channel: "k8s-slack-notify"
         notifyBuild('UNSTABLE', env.STAGE)
       }
   }
 }
