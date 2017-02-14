package com.example.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("local")
public class HikariCPConfig {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Primary
	@Bean(destroyMethod = "close", name = "hikariPostgreSqlDatasource")
	@ConfigurationProperties(prefix = "hikari.postgres")
	public DataSource dataSourcePostgreSql() {
		logger.info("Creating HikariCP datasource.");
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "hikariPostgreSqlJdbcTemplate")
	public JdbcTemplate jdbcTemplateOracle(@Qualifier("hikariPostgreSqlDatasource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
