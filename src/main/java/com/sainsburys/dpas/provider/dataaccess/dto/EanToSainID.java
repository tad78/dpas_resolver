package com.sainsburys.dpas.provider.dataaccess.dto;

import org.json.JSONObject;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class EanToSainID {

	private String ean;
	private String sainId;

	@DynamoDbPartitionKey
	@DynamoDbAttribute("ean")
	public String getEan() {
		return this.ean;
	}
	
	@DynamoDbAttribute("sainId")
	public String getSainId() {
		return this.sainId;
	}
	public EanToSainID() {
	}
	
	
	public void setEan(String ean) {
		this.ean = ean;
	}
	
	public void setSainId(String sainId) {
		this.sainId = sainId;
	}
	
	public EanToSainID(String ean, String sainId) {
		this.ean = ean;
		this.sainId = sainId;
	}
	
	public String toJson() {
		return new JSONObject()
				.put("ean", ean)
				.put("sainId", sainId)
				.toString();
	}

}
