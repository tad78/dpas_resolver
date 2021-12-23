package com.sainsburys.dpas.provider.rest.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.sainsburys.dpas.provider.dataaccess.dto.Product;
import com.sainsburys.dpas.provider.dataaccess.dynamo.ProductResolutionRepository;

import reactor.core.publisher.Mono;

@Service
public class ProductResolutionService implements IProductResolutionService {

	private final ProductResolutionRepository productRepository;
	
	public ProductResolutionService(final ProductResolutionRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Mono<Product> getProduct(String sainId) {
		CompletableFuture<Product> product = 
				productRepository.findById(sainId)
				.whenComplete((prod, ex) ->{
					if (prod == null) {
						throw new IllegalArgumentException("Invalid Ean");
					}
				})
				.exceptionally(ex -> new Product(null, null));
		return Mono.fromFuture(product);
	}
	
	public Mono<Void> save(Product product) {
		return Mono.fromFuture(productRepository.save(product));
	}

}
