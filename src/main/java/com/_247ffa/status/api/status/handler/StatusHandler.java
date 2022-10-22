package com._247ffa.status.api.status.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.model.Input;
import com._247ffa.status.api.status.handler.model.ServersParameters;
import com._247ffa.status.api.status.model.Server;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class StatusHandler extends FunctionInvoker<Input, List<Server>> {

	@FunctionName("v1Servers")
	public HttpResponseMessage execute(@HttpTrigger(name = "request", route = "v1/servers", methods = {
			HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ServersParameters>> request,
			ExecutionContext context) {
		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(new Input(), context))
				.header("Content-Type", "application/json").build();
	}
}