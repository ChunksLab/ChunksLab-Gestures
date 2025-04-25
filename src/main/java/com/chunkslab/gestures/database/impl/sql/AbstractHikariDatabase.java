package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.util.LogUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractHikariDatabase extends AbstractSQLDatabase {

    private final GesturesPlugin plugin;
    private final ConfigurationSection section;
    private HikariDataSource dataSource;
    private final String driverClass;
    private final String sqlBrand;

    public AbstractHikariDatabase(GesturesPlugin plugin, ConfigurationSection section, String sqlBrand, String driverClass) {
        this.plugin = plugin;
        this.section = section;
        this.sqlBrand = sqlBrand;
        this.driverClass = driverClass;
    }

    @Override
    public void enable() {
        if (section == null) {
            LogUtils.severe("Database configuration section is null. Please check your database config.");
            return;
        }

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found: " + driverClass, e);
        }

        this.tablePrefix = section.getString("table-prefix", "chunkslab-gestures");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(section.getString("user", "root"));
        hikariConfig.setPassword(section.getString("password", ""));
        hikariConfig.setJdbcUrl(buildJdbcUrl());
        hikariConfig.setDriverClassName(driverClass);
        hikariConfig.setMaximumPoolSize(section.getInt("Pool-Settings.max-pool-size", 10));
        hikariConfig.setMinimumIdle(section.getInt("Pool-Settings.min-idle", 5));
        hikariConfig.setMaxLifetime(section.getLong("Pool-Settings.max-lifetime", 1800000L));
        hikariConfig.setConnectionTimeout(section.getLong("Pool-Settings.time-out", 20000L));
        hikariConfig.setPoolName("GesturesHikariPool");

        try {
            hikariConfig.setKeepaliveTime(section.getLong("Pool-Settings.keep-alive-time", 60000L));
        } catch (NoSuchMethodError ignored) {}

        Properties properties = new Properties();
        properties.putAll(Map.of(
                "cachePrepStmts", "true",
                "prepStmtCacheSize", "250",
                "prepStmtCacheSqlLimit", "2048",
                "useServerPrepStmts", "true",
                "rewriteBatchedStatements", "true"
        ));
        hikariConfig.setDataSourceProperties(properties);

        dataSource = new HikariDataSource(hikariConfig);

        createTableIfNotExist();
    }

    private String buildJdbcUrl() {
        String host = section.getString("host", "localhost");
        int port = section.getInt("port", 3306);
        String database = section.getString("database", "gestures");
        String parameters = section.getString("connection-parameters", "?useSSL=false&autoReconnect=true");

        if (sqlBrand.equalsIgnoreCase("sqlite")) {
            return "jdbc:sqlite:" + plugin.getDataFolder() + "/" + database + ".db";
        } else if (sqlBrand.equalsIgnoreCase("h2")) {
            return "jdbc:h2:" + plugin.getDataFolder() + "/" + database;
        }
        return String.format("jdbc:%s://%s:%d/%s%s", sqlBrand.toLowerCase(Locale.ENGLISH), host, port, database, parameters);
    }

    @Override
    public void disable() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}