package com._247ffa.status.api.stat.function;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.StatFilter;

@Component("v1statsplayers")
public class ConnectedPlayers implements Function<StatFilter, Report<com._247ffa.status.api.stat.model.ConnectedPlayers>> {

	@Autowired
	protected StatDAO statDAO;

	@Override
	public Report<com._247ffa.status.api.stat.model.ConnectedPlayers> apply(StatFilter filter) {
		return new Report<>("Player connection history for 247ffa.com hosted QE servers. Stats for last seven days. Host player is excluded from totals",
				statDAO.getConnectedPlayers());

	}

}