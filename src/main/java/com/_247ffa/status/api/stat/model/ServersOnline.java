package com._247ffa.status.api.stat.model;

public class ServersOnline {
	private int serversOnline;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getServersOnline() {
		return serversOnline;
	}

	public void setServersOnline(float serversOnline) {
		this.serversOnline = (int)serversOnline;
	}
}
