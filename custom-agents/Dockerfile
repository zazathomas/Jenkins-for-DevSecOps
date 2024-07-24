FROM redhat/ubi9:9.4

USER root

#########################################################################
# This Dockfile file is used to build a ubi9 based Docker Image         #
# containing a couple security tools used for SAST, Secret scanning     #
# , IaC scanning, Image scanning, SBOM generation & Artifact signing.   #
#                                                                       #
# All the tools are updated to the latest available versions each time  #
# an image is built from this dockerfile.                               #
#########################################################################


# Install Java for Jenkins support
RUN dnf update -y && dnf install java-21-openjdk -y

# Install Trivy for Image scanning
RUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin

# Install Trufflehog for Secret Scanning
RUN curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sh -s -- -b /usr/local/bin

# Install Python 12
RUN dnf install python3.12 -y && dnf install python3.12-pip -y

# Install semgrep & bandit for SAST scanning
RUN pip3.12 install semgrep && pip3.12 install bandit

# Install Syft for SBOM generation
RUN curl -sSfL https://raw.githubusercontent.com/anchore/syft/main/install.sh | sh -s -- -b /usr/local/bin

# Install Checkov for IaC scanning
RUN pip3.12 install checkov

# Install Cosign for Artifact Signing
RUN curl -O -L "https://github.com/sigstore/cosign/releases/latest/download/cosign-linux-amd64" \
&& mv cosign-linux-amd64 /usr/local/bin/cosign && chmod +x /usr/local/bin/cosign

# Install Docker CLI
COPY --from=docker:dind /usr/local/bin/docker /usr/local/bin/

# Create Jenkins User
RUN useradd -ms /bin/bash jenkins
