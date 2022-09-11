package com._247ffa.status.api.status.model;

import java.util.Optional;

public class Server {
	private String id;
	private String name;
	private Optional<Session> session;

	public Optional<Session> getSession() {
		return session;
	}

	public Server setSession(Optional<Session> session) {
		this.session = session;
		return this;
	}

	public String getId() {
		return id;
	}

	public Server setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Server setName(String name) {
		this.name = name;
		return this;
	}
}
