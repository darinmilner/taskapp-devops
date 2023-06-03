FROM jenkins/jenkins:lts-jdk11

ARG TERRAFORM_VERSION=1.4.6

USER root

RUN apt-get update && apt-get install -y lsb-release

RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
    https://download.docker.com/linux/debian/gpg

RUN echo "deb [arch=$(dpkg --print-architecture) \
    signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
    https://download.docker.com/linux/debian \
    $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

RUN apt-get update && apt-get install -y docker-ce-cli

################################
# Install AWS CLI
################################
#
#RUN apt-get install -y python3-pip
#
#RUN pip3 install --upgrade pip
#RUN python3 -V
#RUN pip --version
#
#RUN pip install awscli --upgrade --user
#
## add aws cli location to path
#ENV PATH=~/.local/bin:$PATH
#
## Adds local templates directory and contents in /usr/local/terrafrom-templates
##ADD templates /usr/local/bin/templates
#
#RUN mkdir ~/.aws && touch ~/.aws/credentials

################################
# Install Terraform
################################
RUN \
    # Update
  	apt-get update -y && \
  	# Install dependencies
  	apt-get install unzip wget -y

# Download terraform for linux
RUN wget https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip
# Unzip
RUN unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip
# Move to local bin
RUN mv terraform /usr/local/bin/
# Check that it's installed
RUN terraform --version

RUN rm -f terraform_${TERRAFORM_VERSION}_linux_amd64.zip

USER jenkins

RUN jenkins-plugin-cli --plugins "blueocean docker-workflow git"