package br.com.vote.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SqsMessageService {

    @Value("${cloud.aws.sqs.queue.active-session}")
    private String queueName;

    private static final Logger logger = LoggerFactory.getLogger(SqsMessageService.class);

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public SqsMessageService(AmazonSQSAsync amazonSQSAsync) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
    }

    public void sendMessage(String message) {
        logger.info("Envio de mensagem SQS - inicio service {}", message);
        this.queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(message).build());
        logger.info("Envio de mensagem SQS - fim service ");
    }

}
