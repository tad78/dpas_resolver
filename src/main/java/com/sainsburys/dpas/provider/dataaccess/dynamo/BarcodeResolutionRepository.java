package com.sainsburys.dpas.provider.dataaccess.dynamo;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class BarcodeResolutionRepository implements IBarcodeResolutionRepository {

	private final DynamoDbEnhancedAsyncClient enhancedDynamoDbAsyncClient;
	private final DynamoDbAsyncTable<EanToSainID> eanToSainsIDTable;
	
	public BarcodeResolutionRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
		this.enhancedDynamoDbAsyncClient = dynamoDbEnhancedAsyncClient;
		this.eanToSainsIDTable = enhancedDynamoDbAsyncClient.table(EanToSainID.class.getSimpleName(), TableSchema.fromBean(EanToSainID.class));
	}
	public CompletableFuture<EanToSainID> findById(String ean) {
		return eanToSainsIDTable.getItem(getKey(ean));
	}

	public CompletableFuture<Void> save(EanToSainID eanToSainsID) {
		return eanToSainsIDTable.putItem(eanToSainsID);
	}
	
	private Key getKey(String ean) {
		return Key.builder().partitionValue(ean).build();
	}
}
