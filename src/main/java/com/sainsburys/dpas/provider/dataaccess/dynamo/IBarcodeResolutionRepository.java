package com.sainsburys.dpas.provider.dataaccess.dynamo;

import java.util.concurrent.CompletableFuture;

import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;

public interface IBarcodeResolutionRepository {

	public CompletableFuture<EanToSainID> findById(String ean);
	public CompletableFuture<Void> save(EanToSainID eanToSainsID);
	
}
