package com.sainsburys.dpas.provider.rest.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.sainsburys.dpas.provider.dataaccess.dto.SkuToSainID;
import com.sainsburys.dpas.provider.dataaccess.dynamo.SkuResolutionRepository;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;

import reactor.core.publisher.Mono;

@Service
public class SkuResolutionService implements ISkuResolutionService {

	private SkuResolutionRepository skuResolutionRepository;
	
	public SkuResolutionService(SkuResolutionRepository skuResolutionRepository) {
		this.skuResolutionRepository = skuResolutionRepository;
	}
	
	public Mono<SkuToSainID> getSainsId(String sku, ChannelType channelType) {
		CompletableFuture<SkuToSainID> skuToSainId = 
				skuResolutionRepository.findById(sku, channelType)
				.whenComplete((skuToSain, ex) ->{
					if (skuToSain == null) {
						throw new IllegalArgumentException("Invalid Ean");
					}
				})
				.exceptionally(ex -> new SkuToSainID(null, null, null));
		return Mono.fromFuture(skuToSainId);
	}
	
	public Mono<Void> save(SkuToSainID skuToSainId) {
		return Mono.fromFuture(skuResolutionRepository.save(skuToSainId));
	}
}
