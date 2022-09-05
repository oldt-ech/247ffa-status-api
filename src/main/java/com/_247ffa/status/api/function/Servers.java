package com._247ffa.status.api.function;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.model.Server;
import com._247ffa.status.api.model.ServerFilter;
import com._247ffa.status.api.service.DocumentService;
import com._247ffa.status.api.service.ExtractionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class Servers implements Function<ServerFilter, Flux<Server>> {

	private static final Logger logger = LoggerFactory.getLogger(Servers.class);

	@Value("#{${urls}}")
	private List<String> urls;

	@Autowired
	protected DocumentService grabber;

	@Autowired
	protected ExtractionService extractor;

	public Flux<Server> apply(ServerFilter filter) {
		return Flux.fromStream(urls.stream().map(url -> {
			Optional<Document> doc = grabber.getDocument(url);
			Server server = new Server();
			server.setId(extractor.extractId(url));
			doc.ifPresent(document -> server.setName(extractor.extractName(document))
					.setSession(extractor.extractSession(document)));
			try {
				// todo: maybe just log response body...?
				ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new Jdk8Module());
				logger.info("server=" + mapper.writeValueAsString(server));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return server;
		})).subscribeOn(Schedulers.parallel());
	}

}