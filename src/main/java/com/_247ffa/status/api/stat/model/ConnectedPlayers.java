package com._247ffa.status.api.stat.model;

public class ConnectedPlayers {
	private int connectedPlayers;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getConnectedPlayers() {
		return connectedPlayers;
	}

	public void setConnectedPlayers(float connectedPlayers) {
		this.connectedPlayers = (int) connectedPlayers;
	}

}
