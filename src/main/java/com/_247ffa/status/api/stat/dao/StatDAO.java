package com._247ffa.status.api.stat.dao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com._247ffa.status.api.stat.model.ConnectedPlayers;
import com._247ffa.status.api.stat.model.PopularMap;
import com._247ffa.status.api.stat.model.ServersOnline;
import com._247ffa.status.api.stat.model.Stats;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.cosmos.models.ThroughputProperties;

@Repository
public class StatDAO {
	private final CosmosClient client;
	private final CosmosDatabase database;
	private final CosmosContainer container;
	private final String databaseName;
	private final String databaseContainerName;

	public StatDAO() {
		databaseName = "247ffa";
		databaseContainerName = System.getenv("QE_STATS_CONTAINER");
		client = new CosmosClientBuilder().endpoint(System.getenv("DB_HOST")).key(System.getenv("DB_KEY"))
				.buildClient();
		database = client.getDatabase(client.createDatabaseIfNotExists(databaseName).getProperties().getId());
		CosmosContainerProperties containerProperties = new CosmosContainerProperties(databaseContainerName, "/server");
		container = database.getContainer(database
				.createContainerIfNotExists(containerProperties, ThroughputProperties.createManualThroughput(400))
				.getProperties().getId());
	}

	public List<ConnectedPlayers> getConnectedPlayers() {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter("@cutoff",
				Date.from(LocalDateTime.now().minus(3, ChronoUnit.DAYS).toInstant(ZoneOffset.UTC))));
		SqlQuerySpec query = new SqlQuerySpec(
				"SELECT sum(c.currentPlayers - 1) as connectedPlayers, c.date as time from c"
						+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
						+ " and c.currentPlayers > 0 and c.date >= @cutoff group by c.date", params); // 0 = server offline

		return container.queryItems(query, new CosmosQueryRequestOptions(), ConnectedPlayers.class).stream().parallel()
				.sorted((a, b) -> {
					return (int) (b.getTime() - a.getTime());
				}).collect(Collectors.toList());
	}

	public List<PopularMap> getPopularMaps() {
		String sql = "select sum(c.currentPlayers - 1) as connectedPlayers, c.map as map FROM c"
				+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
				+ " and c.currentPlayers > 0 and c.map <> \"In Lobby\" group by c.map";

		return container.queryItems(sql, new CosmosQueryRequestOptions(), PopularMap.class).stream()
				.collect(Collectors.toList());
	}

	public List<ServersOnline> getServersOnline() {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter("@cutoff",
				Date.from(LocalDateTime.now().minus(3, ChronoUnit.DAYS).toInstant(ZoneOffset.UTC))));
		SqlQuerySpec query = new SqlQuerySpec("SELECT count(1) as serversOnline, c.date as time from c"
				+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
				+ " and c.currentPlayers > 0 and c.date >= @cutoff group by c.date", params);

		return container.queryItems(query, new CosmosQueryRequestOptions(), ServersOnline.class).stream().parallel()
				.sorted((a, b) -> {
					return (int) (b.getTime() - a.getTime());
				}).collect(Collectors.toList());
	}

	public Stats getStats() {
		String sql = "select value max(ok.connectedPlayers) from (SELECT sum(c.currentPlayers - 1) as connectedPlayers from c"
				+ "	WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
				+ "	and c.currentPlayers > 0 group by c.date) as ok";

		Stats stats = new Stats();
		stats.setMaxPlayers(
				container.queryItems(sql, new CosmosQueryRequestOptions(), Integer.class).iterator().next());
		return stats;
	}
}
