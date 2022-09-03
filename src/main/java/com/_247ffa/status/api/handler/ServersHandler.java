package com._247ffa.status.api.handler;

import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.model.Server;
import com._247ffa.status.api.model.ServerFilter;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import reactor.core.publisher.Flux;

public class ServersHandler extends FunctionInvoker<ServerFilter, Flux<Server>> {

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