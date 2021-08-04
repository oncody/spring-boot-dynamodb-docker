package com.example;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class Config {
  private final int PORT = 8000;
  private final Region REGION = Region.US_WEST_1;
  private final String DOMAIN = "http://localhost:" + PORT;
  private final String ACCESS_KEY = "access";
  private final String SECRET_KEY = "secret";

  @Bean
  DynamoDbEnhancedClient amazonDynamoDBEnhancedClient() {
    ClientOverrideConfiguration.Builder overrideConfig = ClientOverrideConfiguration.builder();
    URI uri = URI.create(DOMAIN);
    AwsCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
    AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

    DynamoDbClient client = DynamoDbClient.builder().overrideConfiguration(overrideConfig.build()).endpointOverride(uri)
        .credentialsProvider(credentialsProvider).region(REGION).build();

    return DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
  }
}