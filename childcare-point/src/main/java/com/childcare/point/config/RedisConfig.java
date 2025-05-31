package com.childcare.point.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

	@Bean
	LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
		return builder -> {
			if (builder.build().isUseSsl()) {
				builder.useSsl().disablePeerVerification();
			}
		};
	}
	
	public static StatefulRedisConnection<String, String> connect() {
	    RedisURI redisURI = RedisURI.create(System.getenv("REDIS_URL"));
	    redisURI.setVerifyPeer(false);

	    RedisClient redisClient = RedisClient.create(redisURI);
	    return redisClient.connect();
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
