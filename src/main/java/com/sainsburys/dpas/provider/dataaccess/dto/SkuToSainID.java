package com.sainsburys.dpas.provider.dataaccess.dto;

import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBNamed;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class SkuToSainID {
	private String sku;	
	private ChannelType channel;
	private String sainId;
	public SkuToSainID() {
		
	}
	
	public SkuToSainID(
			String sku,
			ChannelType channel,
			String sainId) {
		this.sku = sku;
		this.channel = channel;
		this.sainId = sainId;
	}
	
	@DynamoDbPartitionKey
	@DynamoDbAttribute("sku")
	public String getSku() {
		return sku;
	}

	@DynamoDbAttribute("sku")
	public void setSku(String sku) {
		this.sku = sku;
	}
	@DynamoDbSortKey
	@DynamoDBNamed("channel")
	public String getChannel() {
		return channel.getChannelDescriptor();
	}
	
	@DynamoDBNamed("channel")
	public void setChannel(String channelType) {
		this.channel = ChannelType.valueByIdentifier(channelType);
	}

	@DynamoDbAttribute("sainId")
	public String getSainId() {
		return sainId;
	}
	@DynamoDbAttribute("sainId")
	public void setSainId(String sainId) {
		this.sainId = sainId;
	}
	
	public static String generateCacheKey(String sku, ChannelType channel) {
		return String.join(":", sku,channel.getChannelDescriptor());
	}
	
	public String toJson() {
		return new JSONObject()
				.put("sku", sku)
				.put("channel", channel)
				.put("sainId", sainId)
				.toString();
	}
}
