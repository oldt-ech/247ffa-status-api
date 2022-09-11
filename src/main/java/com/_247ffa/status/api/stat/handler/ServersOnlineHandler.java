package com._247ffa.status.api.stat.handler;

import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.StatFilter;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class ServersOnlineHandler extends FunctionInvoker<StatFilter, Report<?>> {

	@FunctionName("v1statsservers")
	public HttpResponseMessage execute(
			@HttpTrigger(name = "request", route = "v1/stats/reports/247ffa_online_history", methods = {
					HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<StatFilter>> request,
			ExecutionContext context) {
		StatFilter filter = request.getBody().filter((item -> item.getFilter() != null))
				.orElseGet(() -> new StatFilter());

		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(filter, context))
				.header("Content-Type", "application/json").build();
	}
}
