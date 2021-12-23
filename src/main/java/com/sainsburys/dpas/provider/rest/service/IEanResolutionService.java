package com.sainsburys.dpas.provider.rest.service;

import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;

import reactor.core.publisher.Mono;

public interface IEanResolutionService {

	public Mono<EanToSainID> getSainsId(String ean);
	
	public Mono<Void> save(EanToSainID eanToSainsId);
}
