package br.com.vote.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
@Profile("local")
public class SqsConfigurationLocal {

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("user", "password");
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://localhost.localstack.cloud:4566", "sa-east-1"))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync){
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    protected MappingJackson2MessageConverter messageConverter(ObjectMapper objectMapper){
        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }


}
