package com._247ffa.status.api;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Caffeine;

@Component
@Configuration
@EnableCaching
public class Config {
	public Config() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Bean
	public Caffeine<?, ?> caffeineConfig() {
		return Caffeine.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS);
	}

	@Bean
	public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		caffeineCacheManager.setCaffeine(caffeine);
		return caffeineCacheManager;
	}

}
