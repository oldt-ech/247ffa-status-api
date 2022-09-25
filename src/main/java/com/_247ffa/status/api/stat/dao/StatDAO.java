package com._247ffa.status.api.stat.dao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com._247ffa.status.api.stat.model.ConnectedPlayers;
import com._247ffa.status.api.stat.model.PopularMap;
import com._247ffa.status.api.stat.model.ServersOnline;
import com._247ffa.status.api.stat.model.Stats;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.cosmos.models.ThroughputProperties;

@Repository
public class StatDAO {
	private final CosmosAsyncClient client;
	private final CosmosAsyncDatabase database;
	private final CosmosAsyncContainer container;
	private final String databaseName;
	private final String databaseContainerName;

	public StatDAO() {
		databaseName = "247ffa";
		databaseContainerName = System.getenv("QE_STATS_CONTAINER");
		client = new CosmosClientBuilder().endpoint(System.getenv("DB_HOST")).key(System.getenv("DB_KEY"))
				.consistencyLevel(ConsistencyLevel.EVENTUAL).buildAsyncClient();
		CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(databaseName).block();
		database = client.getDatabase(databaseResponse.getProperties().getId());
		CosmosContainerProperties containerProperties = new CosmosContainerProperties(databaseContainerName, "/server");
		CosmosContainerResponse containerResponse = database
				.createContainerIfNotExists(containerProperties, ThroughputProperties.createManualThroughput(400))
				.block();
		container = database.getContainer(containerResponse.getProperties().getId());
	}

	public List<ConnectedPlayers> getConnectedPlayers() {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter("@cutoff",
				Date.from(LocalDateTime.now().minus(3, ChronoUnit.DAYS).toInstant(ZoneOffset.UTC))));
		SqlQuerySpec query = new SqlQuerySpec(
				"SELECT sum(c.currentPlayers - 1) as connectedPlayers, c.date as time from c"
						+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
						+ " and c.currentPlayers > 0 and c.date >= @cutoff group by c.date",
				params); // 0 = server offline

		return container.queryItems(query, new CosmosQueryRequestOptions(), ConnectedPlayers.class)
				.collectSortedList((a, b) -> {
					return (int) (a.getTime() - b.getTime());
				}).block();
	}

	public List<PopularMap> getPopularMaps() {
		String sql = "select sum(c.currentPlayers - 1) as connectedPlayers, c.map as map FROM c"
				+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
				+ " and c.currentPlayers > 1 group by c.map";

		return container.queryItems(sql, new CosmosQueryRequestOptions(), PopularMap.class).collectList().block();
	}

	public List<ServersOnline> getServersOnline() {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter("@cutoff",
				Date.from(LocalDateTime.now().minus(3, ChronoUnit.DAYS).toInstant(ZoneOffset.UTC))));

		// query: get max grouping in 7 minute intervals (steam rich presence isn't 100%
		// reliable, this is done to remove false outages, 7 mins should be enough for 3
		// polls done 2 mins apart - my cache time)
		// subquery: get counts per poll
		SqlQuerySpec query = new SqlQuerySpec(
				"select max(servers.serversOnline) as serversOnline, floor(servers.time/420000)*420000 as time"
						+ " from (SELECT count(1) as serversOnline, c.date as time from c"
						+ " WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
						+ " and c.currentPlayers > 0 and c.date > @cutoff group by c.date) as servers"
						+ " group by floor(servers.time/420000)*420000",
				params);

		return container.queryItems(query, new CosmosQueryRequestOptions(), ServersOnline.class)
				.collectSortedList((a, b) -> {
					return (int) (a.getTime() - b.getTime());
				}).block();
	}

	public Stats getStats() {
		String sql = "select value max(ok.connectedPlayers) from (SELECT sum(c.currentPlayers - 1) as connectedPlayers from c"
				+ "	WHERE c.miniProfileId in ('1426333927','1426388016','1425838691','1128505857','1426512674','1426297538')"
				+ "	and c.currentPlayers > 1 group by c.date) as ok";

		Stats stats = new Stats();
		stats.setMaxPlayers(container.queryItems(sql, new CosmosQueryRequestOptions(), Integer.class).collectList()
				.block().iterator().next());
		return stats;
	}
}
