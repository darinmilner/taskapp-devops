FROM gradle:8.1.1-jdk17-alpine

ARG TERRAFORM_VERSION=1.5.0

RUN apk add curl

# Download terraform for linux
RUN wget https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip
# Unzip
RUN unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip
# Move to local bin
RUN mv terraform /usr/local/bin/
# Check that it's installed
RUN echo terraform --version

RUN rm -f terraform_${TERRAFORM_VERSION}_linux_amd64.zip

RUN apk add --no-cache aws-cli

RUN echo which aws
