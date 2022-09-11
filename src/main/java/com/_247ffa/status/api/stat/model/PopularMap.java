package com._247ffa.status.api.stat.model;

public class PopularMap {
	private int connectedPlayers;
	private String map;

	public int getConnectedPlayers() {
		return connectedPlayers;
	}

	public void setConnectedPlayers(float connectedPlayers) {
		this.connectedPlayers = (int) connectedPlayers;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
}
