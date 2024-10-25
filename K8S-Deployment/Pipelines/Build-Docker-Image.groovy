podTemplate(
containers: [
  containerTemplate(image: 'jenkins/inbound-agent:jdk21', name: 'jnlp', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'bitnami/kaniko:1.23.2', name: 'kaniko', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'chainguard/crane', name: 'crane', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'bitnami/cosign:2.4.1', name: 'cosign', alwaysPullImage: true)
]
){
    node(POD_LABEL) {
        try {
            stage('Git Clone') {
                echo 'Simulating Git Clone'
            }

            stage("Build Docker Image"){
                container('kaniko'){
                    echo 'Building Docker Image...'
                    sh """
                    #!/bin/env bash
                    # Add details here
                    """
                }
            }

            stage("Push Image to Registry"){
                container('crane'){
                    echo 'Pushing Image to registry...'
                    sh """
                    #!/bin/env bash
                    # Add details here
                    """
                }
            }

            stage("Sign Image"){
                container('cosign'){
                    echo 'Signing Image...'
                    sh """
                    #!/bin/env bash
                    # Add details here
                    """
                }
            }
        }

        catch (Exception e) {
        // Handle failure case (similar to `post { failure { } }` in declarative)
        echo "Pipeline failed: ${e.getMessage()}"
        currentBuild.result = 'FAILURE'
        }

        finally {
        // This will always run, whether success or failure (like `post { always { } }`)
        echo 'Pipeline finished!'
    }
}
}


