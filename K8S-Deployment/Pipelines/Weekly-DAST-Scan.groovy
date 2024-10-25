podTemplate(
containers: [
  containerTemplate(image: 'jenkins/inbound-agent:jdk21', name: 'jnlp', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'projectdiscovery/nuclei:v3.3.5', name: 'nuclei', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'python:3.12-slim', name: 'python3-12', alwaysPullImage: true)
]
){
    node(POD_LABEL) {
        try {
            stage('Git Clone') {
                echo 'Simulating Git Clone'
            }

            stage("Dynamic Application Security Testing (DAST)"){
                container('nuclei'){
                    echo 'Running Nuclei scan...'
                    sh """
                    #!/bin/env bash
                    nuclei --version
                    nuclei -list endpoints.txt -json-export nuclei_results.json
                    """
                }
            }

            stage("Upload Scan Results"){
                container('python3-12'){
                    echo 'Uploading Nuclei scan results...'
                    sh """
                    #!/bin/env bash
                    nuclei -list endpoints.txt -json-export nuclei_results.json
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


