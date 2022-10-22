package com._247ffa.status.api.stat.handler;

import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.model.Input;
import com._247ffa.status.api.stat.model.Report;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class StatsHandler extends FunctionInvoker<Input, Report<?>> {

	@FunctionName("v1Stats247FFA")
	public HttpResponseMessage execute(@HttpTrigger(name = "request", route = "v1/stats/reports/247ffa_general", methods = {
			HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			ExecutionContext context) {
		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(new Input(), context))
				.header("Content-Type", "application/json").build();
	}
}
