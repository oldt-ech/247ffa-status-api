package com._247ffa.status.api.stat.function;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.Application;
import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.StatFilter;
import com._247ffa.status.api.stat.service.ReportService;

@Component("v1statsservers")
public class ServersOnline implements Function<StatFilter, Report<?>> {

	@Autowired
	protected StatDAO statDAO;

	@Autowired
	protected ReportService reportService;

	@Override
	public Report<?> apply(StatFilter filter) {
		return reportService.getReport("v1statsservers", () -> {
			List<com._247ffa.status.api.stat.model.ServersOnline> items = statDAO.getServersOnline();

			items = reportService.removeNoise(items,
					(previous, current) -> previous.getServersOnline() != current.getServersOnline()
							|| (previous.getTime() - current.getTime() > Application.MILLISECONDS_IN_HOUR));

			return new Report<com._247ffa.status.api.stat.model.ServersOnline>(
					"Server online status for 247ffa.com hosted QE servers. Stats for the last three days.", items);
		});
	}

}