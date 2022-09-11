package com._247ffa.status.api.stat.function;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.stat.dao.StatDAO;
import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.StatFilter;

@Component("v1statsmaps")
public class PopularMaps implements Function<StatFilter, Report<com._247ffa.status.api.stat.model.PopularMap>> {

	@Autowired
	protected StatDAO statDAO;

	@Override
	public Report<com._247ffa.status.api.stat.model.PopularMap> apply(StatFilter filter) {
		return new Report<>("Map popularity for 247ffa.com hosted QE servers. Stats since Wed Sep 7 2022.", 
				statDAO.getPopularMaps());
	}

}