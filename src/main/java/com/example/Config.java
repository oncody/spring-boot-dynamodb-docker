package com.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.example.repo")
public class Config {
  private final int PORT = 8080;
  private final String DOMAIN = "http://localhost:" + PORT;
  private final String ACCESS_KEY = "access";
  private final String SECRET_KEY = "secret";

  @Bean
  public AmazonDynamoDB amazonDynamoDB() throws Exception {
    System.setProperty("sqlite4java.library.path", "native-libs");
    DynamoDBProxyServer server = ServerRunner
        .createServerFromCommandLineArgs(new String[] { "-inMemory", "-port", "" + PORT });
    server.start();

    AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DOMAIN, null)).build();

    return amazonDynamoDB;
  }

  @Bean
  @Primary
  public DynamoDBMapper dynamoDBMapper() throws Exception {
    return new DynamoDBMapper(amazonDynamoDB());
  }
}