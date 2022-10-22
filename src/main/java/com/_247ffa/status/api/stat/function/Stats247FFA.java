package com._247ffa.status.api.stat.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.model.Input;
import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.service.ReportService;

@Component("v1Stats247FFA")
public class Stats247FFA implements Function<Input, Report<?>> {

	@Autowired
	protected StatDAO statDAO;

	@Autowired
	protected ReportService reportService;

	@Override
	public Report<?> apply(Input input) {
		return reportService.getReport("v1Stats247FFA", () -> {
			return new Report<>(
					"General stats for 247ffa.com hosted QE servers. Stats since Wed Sep 7 2022."
							+ " Host player is excluded from totals",
					new ArrayList<>(Arrays.asList(statDAO.getStats247FFA())));
		});
	}
}