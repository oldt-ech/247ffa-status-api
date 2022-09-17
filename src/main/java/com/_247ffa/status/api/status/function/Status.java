package com._247ffa.status.api.status.function;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com._247ffa.status.api.status.model.Server;
import com._247ffa.status.api.status.model.ServerFilter;
import com._247ffa.status.api.status.service.DocumentService;
import com._247ffa.status.api.status.service.ExtractionService;

@Component("v1servers")
public class Status implements Function<ServerFilter, List<Server>> {

	@Value("#{${urls}}")
	private List<String> urls;

	@Autowired
	protected DocumentService grabber;

	@Autowired
	protected ExtractionService extractor;

	public List<Server> apply(ServerFilter filter) {
		return urls.parallelStream().map(url -> { // async grab steam info
			Optional<Document> doc = grabber.getDocument(url);
			Server server = new Server();
			server.setId(extractor.extractId(url));
			doc.ifPresent(document -> server.setName(extractor.extractName(document))
					.setSession(extractor.extractSession(document)));
			System.out.println("grabbing");
			return server;
		}).collect(toUnmodifiableList());
	}
}