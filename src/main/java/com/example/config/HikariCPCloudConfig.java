package com.example.config;

import java.io.IOException;
import java.util.Iterator;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("cloud")
public class HikariCPCloudConfig {

	private static final String DB_SERVICE_NAME = "sachpostgresqldb";
	private static final String KEY_DB_SERVICE_ENV = "compose-for-postgresql";

	private static final String JDBC_URL_FORMAT = "jdbc:postgresql://HOST_NAME:PORT_NUMBER/compose";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment environment;

	@Primary
	@Bean(destroyMethod = "close", name = "hikariPostgreSqlDatasource")
	public DataSource dataSourcePostgreSql() throws IOException {
		logger.info("Creating the HikariCP datasource for PostgreSQL.");
		HikariConfig config = new HikariConfig();

		String VCAP_SERVICES = environment.getProperty("VCAP_SERVICES");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(VCAP_SERVICES);
			JsonNode arr = rootNode.path(KEY_DB_SERVICE_ENV);
			Iterator<JsonNode> dbNodes = arr.elements();
			while (dbNodes.hasNext()) {
				JsonNode connNode = dbNodes.next();
				String dbServiceName = connNode.path("name").asText();

				if (DB_SERVICE_NAME.equalsIgnoreCase(dbServiceName)) {
					JsonNode credentialNode = connNode.path("credentials");
					String dbUri = credentialNode.path("uri").asText();
					logger.info("dbUri: " + dbUri);
					dbUri = dbUri.replace("postgres://", "");
					dbUri = dbUri.replace("/compose", "");
					String dbUriParts[] = dbUri.split(":");
					String username = dbUriParts[0];
					String password = dbUriParts[1].split("@")[0];
					String host = dbUriParts[1].split("@")[1];
					String port = dbUriParts[2];

					logger.info("username {}", username);
					logger.info("hostname {}", host);
					logger.info("port number {}", port);

					String jdbcUrl = JDBC_URL_FORMAT.replace("HOST_NAME", host).replace("PORT_NUMBER", port);
					logger.info("JdbcUrl formed is {}", jdbcUrl);

					int iMaxConnSize = Integer.valueOf(environment.getProperty("hikari.postgres.maximumPoolSize"));
					logger.info("iMaxConnSize: " + iMaxConnSize);

					config.setJdbcUrl(jdbcUrl);
					config.setUsername(username);
					config.setPassword(password);
					config.setAutoCommit(true);
					config.setMaximumPoolSize(iMaxConnSize);
					config.setDriverClassName(environment.getProperty("hikari.postgres.driverClassName"));
					break;
				}
			}

			DataSource ds = new HikariDataSource(config);
			return ds;
		} catch (IOException e) {
			logger.info("Exception while creating datasource:" + e.getMessage());
			throw e;
		}
	}

	@Bean(name = "hikariPostgreSqlJdbcTemplate")
	public JdbcTemplate jdbcTemplateOracle(@Qualifier("hikariPostgreSqlDatasource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/*
	 * Method 2
	 * 
	 * @Primary
	 * 
	 * @Bean(destroyMethod = "close", name = "hikariPostgreSqlDatasource")
	 * 
	 * @ConfigurationProperties(prefix = "hikari.postgres") public DataSource
	 * dataSourcePostgreSql() { return
	 * DataSourceBuilder.create().type(HikariDataSource.class).build(); }
	 * 
	 * @Bean(name = "hikariPostgreSqlJdbcTemplate") public JdbcTemplate
	 * jdbcTemplateOracle(@Qualifier("hikariPostgreSqlDatasource") DataSource
	 * dataSource) { return new JdbcTemplate(dataSource); }
	 */

	/*
	 * Method 1
	 * 
	 * @Bean(destroyMethod = "close", name = "hikariPostgreSqlDatasource")
	 * public DataSource dataSource() { Cloud cloud = new
	 * CloudFactory().getCloud(); DataSource dataSource =
	 * cloud.getServiceConnector(DB_SERVICE_NAME, DataSource.class, null);
	 * return dataSource; }
	 * 
	 * 
	 * @Bean(name = "hikariPostgreSqlJdbcTemplate") public JdbcTemplate
	 * jdbcTemplateOracle(@Qualifier("hikariPostgreSqlDatasource") DataSource
	 * dataSource) { return new JdbcTemplate(dataSource); }
	 * 
	 * private String DB_SERVICE_NAME = "laurenlandscapes";
	 */

}
