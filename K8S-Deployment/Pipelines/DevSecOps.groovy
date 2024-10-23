pipeline {
    agent {
        kubernetes {
            yaml """
spec:
  containers:
    - name: jnlp
      image: inbound-agent
    - name: trufflehog
      image: trufflesecurity/trufflehog:3.82.12
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
    - name: semgrep
      image: chainguard/semgrep:latest
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
    - name: nuclei
      image: projectdiscovery/nuclei:v3.3.5
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
    - name: checkov
      image: bridgecrew/checkov:3.2.269
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
    - name: cosign
      image: bitnami/cosign:2.4.1
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
    - name: trivy
      image: aquasec/trivy:0.56.2
      imagePullPolicy: Always
      command:
      - sleep
      args:
      - infinity
"""
        }
    }

    stages {
        stage('Git Clone') {
            steps {
                container('trufflehog') {
                    echo 'Simulating Git Clone'
                }
            }
        }

        stage('Static Application Security Testing (SAST)'){
            steps{
                container('semgrep'){
                    echo 'Running Semgrep scan'
                    // Placeholder for semgrep commands
                    sh 'semgrep --version'
                }
            }
        }


        stage('Secret Scanning') {
            steps {
                container('trufflehog') {
                    echo 'Running Trufflehog Scan...'
                    sh 'trufflehog --version' // Checking version
                    // Scan Repository for secrets
                    sh 'trufflehog git file://./ --no-update --no-verification --json > trufflehog_results.json'
                }
            }
        }

        stage("Infrastructure as Code(IaC) Scanning"){
            steps{
                container('checkov'){
                    echo 'Running Checkov Scan...'
                    sh 'checkov --version' // Checking version
                    // Scan Repository for IaC misconfigurations
                    sh 'checkov -d ./ --skip-download -o json --output-file-path ./'
                }
            }
        }

        stage("Container Scanning"){
            steps{
                container('trivy'){
                    echo 'Running Trivy scan...'
                    sh 'trivy --version' // Checking version
                    // Scan image for vulnerabilities
                    sh 'trivy image --skip-db-update --skip-java-db-update --skip-check-update --scanners vuln --severity HIGH,CRITICAL --ignore-unfixed --format json --output trivy_results.json test-image'
                }
            }
        }

        stage("Dynamic Application Security Testing (DAST)"){
            steps{
                container('nuclei'){
                    echo 'Running Nuclei scan...'
                    sh 'nuclei --version' // Checking version
                    // Scan Endpoints for vulnerabilities
                    sh 'nuclei -disable-update-check -target https://example.com -json-export nuclei_results.json'
                }
            }
        }

    }

    post {
        always {
            echo 'Pipeline finished!'
        }
    }
}
