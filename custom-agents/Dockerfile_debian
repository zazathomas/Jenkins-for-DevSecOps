FROM jenkins/agent:jdk17

USER root

# Copy docker cli from dind image
COPY --from=docker:dind /usr/local/bin/docker /usr/local/bin/

# Install Python, Semgrep, Checkov, Bandit
RUN apt update -y && apt-get install -y python3 python3-pip \
  && rm -rf /var/lib/apt/lists/* \
  && pip3 install semgrep checkov bandit

# Install Trivy, TruffleHog, Cosign, Syft
RUN apt update -y &&  apt-get install wget -y && wget https://github.com/aquasecurity/trivy/releases/download/v0.18.3/trivy_0.18.3_Linux-64bit.deb \
&& dpkg -i trivy_0.18.3_Linux-64bit.deb && rm trivy_0.18.3_Linux-64bit.deb \
&& curl -O -L "https://github.com/sigstore/cosign/releases/latest/download/cosign-linux-amd64" \
&& mv cosign-linux-amd64 /usr/local/bin/cosign && chmod +x /usr/local/bin/cosign \
&& curl -sSfL https://raw.githubusercontent.com/anchore/syft/main/install.sh | sh -s -- -b /usr/local/bin



