package com.sainsburys.dpas.provider.dataaccess.dynamo;

import java.net.URI;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.util.StringUtils;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
@EnableDynamoDBRepositories
public class DynamoDbConfig {
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    
    
    
    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
    	return DynamoDbAsyncClient.builder()
    			.credentialsProvider(ProfileCredentialsProvider.create("default"))
    			.endpointOverride(URI.create(amazonDynamoDBEndpoint))
    			.build();
    }
    
    
    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() {
    	return DynamoDbEnhancedAsyncClient.builder()
    			.dynamoDbClient(dynamoDbAsyncClient())
    			.build();
    }
    
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
    	
    	
        AmazonDynamoDBClientBuilder amazonDynamoDBBuilder =  AmazonDynamoDBClient.builder();
        
        if (!StringUtils.isNullOrEmpty(amazonDynamoDBEndpoint)) {
        	amazonDynamoDBBuilder.setEndpointConfiguration(new EndpointConfiguration(amazonDynamoDBEndpoint,null));
        }
        amazonDynamoDBBuilder.setCredentials(
        		new AWSCredentialsProvider() {
					
					@Override
					public void refresh() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public AWSCredentials getCredentials() {
						return new BasicAWSCredentials(
						          amazonAWSAccessKey, amazonAWSSecretKey);
					}
				});
        return amazonDynamoDBBuilder.build();
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
          amazonAWSAccessKey, amazonAWSSecretKey);
    }
}
