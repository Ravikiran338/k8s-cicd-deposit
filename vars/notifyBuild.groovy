def call(String buildStatus, String stageName) {
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  if ((buildStatus == 'SUCCESS') || (buildStatus == 'SUCCEEDED')) {
    color = 'GREEN'
    colorCode = '#008000'
    summary = "SUCCESS: Job ${env.JOB_NAME} with build number ${env.BUILD_NUMBER}"
     body = "The Build is Successful. Check out the log at ${env.BUILD_URL}"
  } else if (buildStatus == 'FAILED') {
    color = 'RED'
    colorCode = '#FF0000'
    summary = "FAILED: Job ${env.JOB_NAME} with build number ${env.BUILD_NUMBER} in stage ${stageName}" 
    body = "The Build is Failed in stage ${stageName}. Check out the log at ${env.BUILD_URL}"
  } else if ((buildStatus == 'UNSTABLE') || (buildStatus == "IS UNSTABLE")) {
    color = 'YELLOW'
    colorCode = '#FFFF00'
    summary = "UNSTABLE: Job ${env.JOB_NAME} with build number ${env.BUILD_NUMBER}"
    body = "The Build is Unstable. Check out the log at ${env.BUILD_URL}"
  } else if (buildStatus == 'ABORTED') {
    color = 'GREY'
    colorCode = '#808080'
    summary = "ABORTED: Job ${env.JOB_NAME} with build number ${env.BUILD_NUMBER} in stage ${stageName}"
	
    body = "The Build was Aborted at ${stageName}. Check out the log at ${env.BUILD_URL}"
  }
  // Send notifications
  slackSend (color: colorCode, message: summary)
  // Send Email notifications
  emailext (
      attachLog: true,
      subject: subject,
      body: body,
     to: "ravikiran.moola@radiant.digital"
    )
}
