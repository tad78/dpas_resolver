package com.sainsburys.dpas.provider.dataaccess.dto;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Product {

	private String sainId;
	private String productDescription;
	@DynamoDbPartitionKey
	@DynamoDbAttribute("sainId")
	public String getSainId() {
		return this.sainId;
	}
	
	@DynamoDbAttribute("productDescription")
	public String getProductDescription() {
		return this.productDescription;
	}
	public Product() {
	}
	
	
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	public void setSainId(String sainId) {
		this.sainId = sainId;
	}
	
	public Product(String sainId, String productDescription) {
		this.sainId = sainId;
		this.productDescription = productDescription;
	}
}
