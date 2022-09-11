package com._247ffa.status.api.stat.model;

import java.util.ArrayList;
import java.util.List;

public class Report<T> {
	private String description;
	private List<? extends T> items;

	public Report(String description, List<? extends T> items) {
		this.description = description;
		this.items = items;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<T> getItems() {
		return new ArrayList<>(items);
	}
}
