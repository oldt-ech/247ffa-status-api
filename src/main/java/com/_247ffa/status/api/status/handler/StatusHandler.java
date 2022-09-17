package com._247ffa.status.api.status.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.status.model.Server;
import com._247ffa.status.api.status.model.ServerFilter;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class StatusHandler extends FunctionInvoker<ServerFilter, List<Server>> {

	@FunctionName("v1servers")
	public HttpResponseMessage execute(@HttpTrigger(name = "request", route = "v1/servers", methods = {
			HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ServerFilter>> request,
			ExecutionContext context) {
		ServerFilter filter = request.getBody().filter((item -> item.getFilter() != null))
				.orElseGet(() -> new ServerFilter());

		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(filter, context))
				.header("Content-Type", "application/json").build();
	}
}