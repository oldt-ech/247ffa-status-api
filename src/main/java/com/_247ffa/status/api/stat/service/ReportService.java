package com._247ffa.status.api.stat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

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

	public <T> List<T> removeNoise(List<T> items, BiPredicate<T, T> predicate) {
		List<T> filteredContent = new ArrayList<>();

		if (items.isEmpty()) {
			return items;
		}
			
		filteredContent.add(items.iterator().next());
		T lastAdded = filteredContent.iterator().next();
		for (T item : items) {
			if (predicate.test(lastAdded, item)) {
				filteredContent.add(item);
				lastAdded = item;
			}
		}

		filteredContent.add(items.get(items.size() - 1));
		return filteredContent;
	}
}
