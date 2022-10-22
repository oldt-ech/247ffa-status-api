package com._247ffa.status.api.stat.function;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.Application;
import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.ServersOnlineInput;
import com._247ffa.status.api.stat.service.ReportService;

@Component("v1StatsServerOnline")
public class ServerOnline implements Function<ServersOnlineInput, Report<?>> {

	@Autowired
	protected StatDAO statDAO;

	@Autowired
	protected ReportService reportService;

	@Override
	public Report<?> apply(ServersOnlineInput input) {
		return reportService.getReport("v1StatsServerOnline" + input.getId() + input.getFrom(), () -> {
			List<com._247ffa.status.api.stat.model.ServerOnlineInfo> items = statDAO.getServerOnline(input.getId(),
					input.getFrom());

			items = reportService.removeNoise(items,
					(previous, current) -> previous.getServersOnline() != current.getServersOnline()
							|| (current.getTime() - previous.getTime() > Application.MILLISECONDS_IN_HOUR));

			return new Report<com._247ffa.status.api.stat.model.ServerOnlineInfo>(
					"Servers online (max count of seven minute intervals) for " + input.getId() + ". Stats since "
							+ input.getFrom() + ". Requests cached for two minutes per server ID (not 'from' offset)",
					items);
		});
	}

}