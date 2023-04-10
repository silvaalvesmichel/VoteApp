#!/bin/bash

echo "####### STARTING #######"

hostname=http://localhost:4566

echo "####### CREATING SQS #######"

aws sqs create-queue --endpoint-url $hostname --queue-name active-session --profile localstack --region sa-east-1

echo "####### LISTING SQS #######"

aws sqs list-queues --endpoint-url $hostname --profile localstack --region sa-east-1