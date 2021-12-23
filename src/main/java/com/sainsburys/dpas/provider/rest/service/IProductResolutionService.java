package com.sainsburys.dpas.provider.rest.service;

import com.sainsburys.dpas.provider.dataaccess.dto.Product;

import reactor.core.publisher.Mono;

public interface IProductResolutionService {

	public Mono<Product> getProduct(String sainId);
	public Mono<Void> save(Product product);
}
