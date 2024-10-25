podTemplate(
containers: [
  containerTemplate(image: 'jenkins/inbound-agent:jdk21', name: 'jnlp', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'trufflesecurity/trufflehog:3.82.12', name: 'trufflehog', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'chainguard/semgrep:latest', name: 'semgrep', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'bridgecrew/checkov:3.2.269', name: 'checkov', alwaysPullImage: true),
  containerTemplate(args: 'infinity', command: 'sleep', image: 'aquasec/trivy:0.56.2', name: 'trivy', alwaysPullImage: true)
]
){
    node(POD_LABEL) {
        try {
            stage('Git Clone') {
                git branch: "branch-name", credentialsId: "github-creds" url: "git-url"
                sh "mkdir -p scan-results"
            }

            stage('Static Application Security Testing (SAST)'){
                container('semgrep'){
                    echo 'Running Semgrep scan'
                    sh """
                    #!/bin/env bash
                    semgrep --version
                    """
                    }
            }


            stage('Secret Scanning') {
                container('trufflehog') {
                    echo 'Running Trufflehog Scan...'
                    sh """
                    #!/bin/env bash
                    trufflehog --version
                    trufflehog git file://./ --json > scan-results/trufflehog_results.json
                    """
                }
            }

            stage("Infrastructure as Code(IaC) Scanning"){
                container('checkov'){
                    echo 'Running Checkov Scan...'
                    sh """
                    #!/bin/env bash
                    checkov --version
                    checkov -d ./ -o json --output-file-path scan-results/
                    """
                }
            }

            stage("Container Scanning"){
                container('trivy'){
                    echo 'Running Trivy scan...'
                    sh """
                    #!/bin/env bash
                    trivy --version
                    trivy image --scanners vuln --severity HIGH,CRITICAL --ignore-unfixed --format json --output scan-results/trivy_results.json test-image
                    """
                }
            }

            stage("Archive Scan Results"){
                archiveArtifacts "scan-results/*.json"
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


