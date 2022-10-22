package com._247ffa.status.api.stat.handler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com._247ffa.status.api.stat.model.Report;
import com._247ffa.status.api.stat.model.ServersOnlineInput;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class ServersOnlineHandler extends FunctionInvoker<ServersOnlineInput, Report<?>> {

	@FunctionName("v1StatsServersOnline247FFA")
	public HttpResponseMessage executeServers247FFA(
			@HttpTrigger(name = "request", route = "v1/stats/reports/247ffa_online_history", methods = {
					HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			ExecutionContext context) {
		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(new ServersOnlineInput(), context))
				.header("Content-Type", "application/json").build();
	}

	@FunctionName("v1StatsServerOnline")
	public HttpResponseMessage executeServer(
			@HttpTrigger(name = "request", route = "v1/stats/reports/online_history/{id}", methods = {
					HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			ExecutionContext context, @BindingName("id") int id) {

		// TODO: maybe put this in the spring function? May want to switch to spring
		// boot later
		int duration;
		int minDuration = 30;
		int maxDuration = 4320;
		try {
			duration = Integer.valueOf(
					request.getQueryParameters().getOrDefault("from", Integer.valueOf(maxDuration).toString()));

		} catch (NumberFormatException e) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST).build();
		}

		if (duration < minDuration || duration > maxDuration) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
					.body("from attribute must be between " + minDuration + " and " + maxDuration + ".").build();
		}

		ServersOnlineInput input = new ServersOnlineInput();
		input.setFrom(LocalDateTime.from(getCurrentTIme()).minus(duration, ChronoUnit.MINUTES));
		input.setId(id);

		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(input, context))
				.header("Content-Type", "application/json").build();
	}

	protected LocalDateTime getCurrentTIme() {
		return LocalDateTime.now();
	}
}
