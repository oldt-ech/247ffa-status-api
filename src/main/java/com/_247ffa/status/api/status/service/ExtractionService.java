package com._247ffa.status.api.status.service;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com._247ffa.status.api.status.model.Session;

@Service
public class ExtractionService {
	public String extractName(Document document) {
		return document.select("span.persona").text();
	}

	public String extractId(String url) {
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}

	public Optional<Session> extractSession(Document document) {
		String match = document.select("span.rich_presence").text();
		Session session = null;
		if (match.contains("Playing Multiplayer")) {
			session = new Session();
			session.setCurrentPlayers(Integer.parseInt(match.substring(match.indexOf('(') + 1, match.indexOf('/'))));
			session.setMaxPlayers(Integer.parseInt(match.substring(match.indexOf('/') + 1, match.indexOf(')'))));
			session.setMapname(match.substring(match.indexOf('-') + 2, match.length()));
		}
		return Optional.ofNullable(session);
	}
}
