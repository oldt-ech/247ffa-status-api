package com._247ffa.status.api.service;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

	@Autowired
	private CacheManager cacheManager;

	public Optional<Document> getDocument(String url) {
		Cache cache = cacheManager.getCache("docs");

		if (null == cache.get(url)) {
			try {
				cache.put(url, Jsoup.connect(url).get());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return Optional.ofNullable((cache.get(url) == null) ? null : (Document) cache.get(url).get());
	}
}
