pipeline{
    agent{
        label "devsecops-box"
    }
    stages{
        stage("Verify Branch"){
            steps{
                echo "$GIT_BRANCH"
            }
        }
        stage("Verify Installed Binaries"){
            steps{
                sh(script: 'docker version')
                sh(script: 'semgrep --version')
                sh(script: 'checkov --version')
                sh(script: 'syft --version')
                sh(script: 'trivy --version')
                sh(script: 'trufflehog --version')
                sh(script: 'cosign version')
                sh(script: 'bandit --version')
            }
        }
}
}
