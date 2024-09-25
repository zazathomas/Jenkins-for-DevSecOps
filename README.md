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

### Retrieve initial admin password
`docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword`

### Access jenkins server
Navigate to `https://localhost:8080/` on your preferred browser.

Afterwards setup your first admin user.


## Configure docker agent as a runner
### Forward traffic from jenkins to docker sock
The socat-proxy service in the compose.yaml file aids to proxy connections from jenkins master container to the docker host.

### Get IP of socat container
docker inspect <socat-proxy container_id> | grep IPAddress

### Add docker runner via Jenkins UI
Navigate to Dashboard > Manage Jenkins > Clouds > New Cloud(Install Docker plugin first) > add `tcp://{socat_ip}:2375` to Docker Host URI & test connection > Save

### Add docker agent template
Navigate to Dashboard > Manage Jenkins > Clouds > Choose newly created agent > edit docker agent template > specify label, node name, docker image i.e. `jenkins/agent:jdk17`, home directory i.e. `/home/jenkins` & instance capacity to 2.
Repeat the above for other agents as required.

### DevSecOps Image
I built a DevSecOps docker image that contains a couple tools used for different scan types. The
dockerfile can be located at `custom-agents/Dockerfile`. Feel free to use and modify as required. PRs are welcome for adding more tools to the file.

### Miscellaneous
`*/5 * * * *` => cron expression to trigger every 5 mins.
`RUN useradd -ms /bin/bash newuser` => Add user to docker file in RHEL
To run docker commands in cloud runner, modify template to allow privileged mode run and add `type=bind,source=/var/run/docker.sock,destination=/var/run/docker.sock` to the Mounts.
