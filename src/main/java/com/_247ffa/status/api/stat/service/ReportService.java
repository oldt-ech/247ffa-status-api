package com._247ffa.status.api.stat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com._247ffa.status.api.stat.model.Report;

@Service
public class ReportService {
	
	@Autowired
	private CacheManager cacheManager;

	public Report<?> getReport(String report, ReportCallback callback) {
		Cache cache = cacheManager.getCache("reports");

		if (null == cache.get(report)) {
			cache.put(report, callback.getReport());
		}

		return (Report<?>) cache.get(report).get();
	}
}
