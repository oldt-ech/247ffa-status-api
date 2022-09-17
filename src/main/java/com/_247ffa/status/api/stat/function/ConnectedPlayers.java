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

@Component("v1statsplayers")
public class ConnectedPlayers implements Function<StatFilter, Report<?>> {

	@Autowired
	protected StatDAO statDAO;

	@Autowired
	protected ReportService reportService;

	@Override
	public Report<?> apply(StatFilter filter) {
		return reportService.getReport("v1statsplayers", () -> {
			List<com._247ffa.status.api.stat.model.ConnectedPlayers> items = statDAO.getConnectedPlayers();

			items = reportService.removeNoise(items,
					(previous, current) -> (previous.getConnectedPlayers() != current.getConnectedPlayers())
							|| (previous.getTime() - current.getTime() > Application.MILLISECONDS_IN_HOUR));

			return new Report<com._247ffa.status.api.stat.model.ConnectedPlayers>(
					"Player connection history for 247ffa.com hosted QE servers. "
							+ "Stats for last three days. Host player is excluded from totals",
					items);
		});
	}

}