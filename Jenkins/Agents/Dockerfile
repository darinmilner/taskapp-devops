FROM gradle:8.1.1-jdk17-alpine

RUN apk upgrade && apk update && apk add --no-cache python3 py-pip git zip terraform

RUN apk add --no-cache aws-cli

RUN echo which aws

RUN pip install --upgrade pip

RUN pip install boto3
