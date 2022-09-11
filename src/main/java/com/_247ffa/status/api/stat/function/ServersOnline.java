package com._247ffa.status.api.stat.function;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.StatFilter;

@Component("v1statsservers")
public class ServersOnline implements Function<StatFilter, Report<com._247ffa.status.api.stat.model.ServersOnline>> {

	@Autowired
	protected StatDAO statDAO;

	@Override
	public Report<com._247ffa.status.api.stat.model.ServersOnline> apply(StatFilter filter) {
		return new Report<>("Server online status for 247ffa.com hosted QE servers. Stats for the last seven days.",
				statDAO.getServersOnline());
	}

}