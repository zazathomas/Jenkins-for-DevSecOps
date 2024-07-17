# Getting started
Getting started with jenkins for DevSecOps
Inspiration: https://github.com/devopsjourney1/jenkins-101

## Setup
### Installation
Build the Jenkins BlueOcean Docker Image (or pull and use the one I built).


```
# To simplify this workflow, I created a docker-compose file which you can use to deploy jenkins easily
# pull image
docker pull zazathomas/jenkins-blueocean:2.468
# Create the folders which jenkins will use to store/retrieve data & cert files
mkdir -p jenkins_data/ jenkins-docker-certs/
# change file permissions to allow jenkins write to the folders
sudo chmod 777 jenkins-docker-certs jenkins_data
# Create jenkins network
docker network create jenkins
# Run jenkins
docker compose up -d
```
