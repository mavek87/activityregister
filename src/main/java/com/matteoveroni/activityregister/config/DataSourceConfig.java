package com.matteoveroni.activityregister.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DataSourceConfig {

    private static final String DB_CONFIG_FILE = "db/datasource.properties";
    private static final HikariConfig config = new HikariConfig(DataSourceConfig.class.getClassLoader().getResource(DB_CONFIG_FILE).getPath());
    private static final HikariDataSource dataSource = new HikariDataSource(config);

    private DataSourceConfig() {
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}