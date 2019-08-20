package com.lessons.config;

import com.lessons.services.ReportService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${app.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${app.datasource.url}")
    private String url;

    @Value("${app.datasource.username}")
    private String username;

    @Value("${app.datasource.password}")
    private String password;

    @Value("${app.datasource.maxPoolSize:20}")
    private int maxPoolSize;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(this.driverClassName);
        hikariConfig.setJdbcUrl(this.url);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setMaximumPoolSize(this.maxPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("app1_jdbc_connection_pool");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    public DataSourceConfig() {
        logger.debug("In DataSourceConfig constructor");
    }

    @PostConstruct
    public void dataSourcePostConstruct() {
        logger.debug("In dataSourcePostConstruct: ");
    }

}