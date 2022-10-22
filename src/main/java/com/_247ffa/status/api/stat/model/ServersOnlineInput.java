package com._247ffa.status.api.stat.model;

import java.time.LocalDateTime;

public class ServersOnlineInput {
	private int id;
	private LocalDateTime from;

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
