package com.sainsburys.dpas.provider.dataaccess.dynamo;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.sainsburys.dpas.provider.dataaccess.dto.Product;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class ProductResolutionRepository {

	private final DynamoDbEnhancedAsyncClient enhancedDynamoDbAsyncClient;
	private final DynamoDbAsyncTable<Product> sainIdTOProductTable;
	
	public ProductResolutionRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
		this.enhancedDynamoDbAsyncClient = dynamoDbEnhancedAsyncClient;
		this.sainIdTOProductTable = enhancedDynamoDbAsyncClient.table("prodBySainID", TableSchema.fromBean(Product.class));
	}
	public CompletableFuture<Product> findById(String sainId) {
		return sainIdTOProductTable.getItem(getKey(sainId));
	}

	public CompletableFuture<Void> save(Product product) {
		return sainIdTOProductTable.putItem(product);
	}
	
	private Key getKey(String sainId) {
		return Key.builder().partitionValue(sainId).build();
	}

}
