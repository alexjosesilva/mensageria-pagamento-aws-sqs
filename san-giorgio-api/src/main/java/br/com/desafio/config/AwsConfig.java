package br.com.desafio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("${aws.region}")  // Lê a região a partir do arquivo de propriedades
    private String awsRegion;

    @Bean
    public SqsClient sqsClient() {
        System.out.println("AWS Region: " + awsRegion);
        if (awsRegion == null || awsRegion.isEmpty()) {
            throw new IllegalStateException(">>> AWS region must not be null or empty");
        }

        return SqsClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))  // Apontando para o LocalStack
                .build();
    }
}

