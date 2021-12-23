package com.sainsburys.dpas.provider.rest.service;

import com.sainsburys.dpas.provider.dataaccess.dto.SkuToSainID;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;

import reactor.core.publisher.Mono;

public interface ISkuResolutionService {

	public Mono<SkuToSainID> getSainsId(String sku, ChannelType channelType);
	public Mono<Void> save(SkuToSainID skuToSainId);
}
