FROM gradle:8.1.1-jdk17-alpine

ARG TERRAFORM_VERSION=1.4.6

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

RUN curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip"

RUN unzip awscli-bundle.zip

RUN ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

RUN echo which aws

