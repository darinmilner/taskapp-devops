import boto3
import json
import logging
import os
from datetime import datetime
from http import client

logger = logging.getLogger()
logger.setLevel(logging.INFO)

SENDER = os.getenv("SENDER")

client = boto3.client("ses")


def index_handler(event, context):
    logger.info(f"Incoming request is: {event}")

    data = json.loads(event["body"])
    message: str = data["message"]
    recipient_email: str = data["email"]
    username: str = data["name"]

    if sender_email is None:
        raise Exception("No email provided.")

    body_html = f"""<html>
        <head></head>
        <body>
          <h2>New Task Added</h2>
          <br/>
          <p>{message}</p> 
          <h4>Sent on {datetime.now()}</h4>
        </body>
        </html>
                    """
    email_message = {
        "Body": {
            "Html": {
                "Charset": "utf-8",
                "Data": body_html,
            },
        },
        "Subject": {
            "Charset": "utf-8",
            "Data": "New Task added",
        },
    }
    ses_response = client.send_email(
        Destination={
            "ToAddresses": [recipient_email],
        },
        Message=email_message,
        Source=SENDER,
    )

    logger.info(f"Recipient's email: {recipient_email}.")
    logger.info(f"Recipient's name: {username}")
    logger.info(f"SES Response: ${ses_response}")

    body = {
        "message": "Email sent successfully!",
        "success": True,
    }

    return {
        "headers": {
            "Content-Type": "application/json",
            "Access-Control-Allow-Methods": "*",
            "Access-Control-Allow-Origin": "*",
        },
        "statusCode": 200,
        "body": json.dumps(body),
    }
