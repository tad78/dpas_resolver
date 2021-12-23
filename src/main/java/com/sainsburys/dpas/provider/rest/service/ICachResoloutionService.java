package com.sainsburys.dpas.provider.rest.service;

import java.util.function.Supplier;

import reactor.core.publisher.Mono;

public interface ICachResoloutionService {
//find an EAN mapping from the cache of type CLAZZ and if it isn't there bugger off to get from
//the persistent store using the getFunction
	<T> Mono<T> findByKey(String key, Class<T> clazz, Supplier<Mono<T>> getFunction);
}
