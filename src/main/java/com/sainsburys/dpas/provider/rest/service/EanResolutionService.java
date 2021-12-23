package com.sainsburys.dpas.provider.rest.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;
import com.sainsburys.dpas.provider.dataaccess.dynamo.IBarcodeResolutionRepository;

import reactor.core.publisher.Mono;

@Service
public class EanResolutionService implements IEanResolutionService{

	@Autowired
	private IBarcodeResolutionRepository barcodeRepository;
	
	public Mono<EanToSainID> getSainsId(String ean) {
		CompletableFuture<EanToSainID> eanToSainsId = 
				barcodeRepository.findById(ean)
				.whenComplete((eanToSains, ex) ->{
					if (eanToSains == null || eanToSains.getSainId() == null) {
						System.out.println("*****oh my it's null******");
						throw new IllegalArgumentException("Invalid Ean");
					} 
				})
				//you can't return null in a Mono (really you want Mono.empty(), but there's
				//no obvious way to get there from a completableFuture
				.exceptionally(ex -> {return new EanToSainID(null, null);})
				;//new EanToSainID(null, null);});
		return Mono.fromFuture(eanToSainsId);
	}
	
	public Mono<Void> save(EanToSainID eanToSainsId) {
		return Mono.fromFuture(barcodeRepository.save(eanToSainsId));
	}

}
