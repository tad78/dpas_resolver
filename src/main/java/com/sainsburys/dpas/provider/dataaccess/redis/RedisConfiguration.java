package com.sainsburys.dpas.provider.dataaccess.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Bean
	public ReactiveRedisOperations<String, Object> redisOperations(ReactiveRedisConnectionFactory redisConnectionFactory) {
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializationContext<String, Object> context =
        RedisSerializationContext.<String, Object>newSerializationContext(new StringRedisSerializer())
                .value(serializer).build();
        return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
	}
}
