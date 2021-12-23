package com.sainsburys.dpas.provider.dataaccess.dynamo;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.sainsburys.dpas.provider.dataaccess.dto.SkuToSainID;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class SkuResolutionRepository {

	
	private final DynamoDbEnhancedAsyncClient enhancedDynamoDbAsyncClient;
	private final DynamoDbAsyncTable<SkuToSainID> skuToSainsIdTable;
	
	public SkuResolutionRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
		this.enhancedDynamoDbAsyncClient = dynamoDbEnhancedAsyncClient;
		this.skuToSainsIdTable = 
				enhancedDynamoDbAsyncClient.table("skuToSainID", TableSchema.fromBean(SkuToSainID.class));
	}

	public CompletableFuture<SkuToSainID> findById(String sku, ChannelType channelType) {
		return skuToSainsIdTable.getItem(getKey(sku, channelType));
	}

	public CompletableFuture<Void> save(SkuToSainID sukToSainsId) {
		return skuToSainsIdTable.putItem(sukToSainsId);
	}
	
	private Key getKey(String sku, ChannelType channelType) {
		return Key.builder().partitionValue(sku).sortValue(channelType.getChannelDescriptor()).build();
	}

	
}
