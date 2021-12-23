package com.sainsburys.dpas.provider.rest.service;

import java.util.function.Supplier;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Service
@RequiredArgsConstructor
public class CachedResolutionService implements ICachResoloutionService{

	private ReactiveRedisOperations<String, Object> redisCacheOperations;

	public CachedResolutionService(ReactiveRedisOperations<String, Object> redisCacheOperations){
		this.redisCacheOperations = redisCacheOperations;
	}
	
	@Override
	public <T> Mono<T> findByKey(String key, Class<T> clazz, Supplier<Mono<T>> getFunction) {
		return CacheMono.lookup(k -> redisCacheOperations.opsForValue().get(composeKey(key, clazz))
				.map(w -> Signal.next(clazz.cast(w))), key)
				.onCacheMissResume(getFunction)
				.andWriteWith((k, v) -> Mono.fromRunnable(() -> 
					redisCacheOperations.opsForValue().setIfAbsent(composeKey(key, clazz), v.get()).subscribe()));
	}
	
	
	private <T> String composeKey(String key, Class<T> clazz) {
		return String.join("::", clazz.getName(), key);
	}
}
