package me.anutley.urlshortener.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try {
            final File dbFile = new File("database.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        ds = new HikariDataSource(config);

        createURLTable();
    }

    public Database() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void createURLTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS urls (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "url TEXT NOT NULL," +
                    "code TEXT NOT NULL," +
                    "permanent BOOLEAN NOT NULL DEFAULT FALSE," +
                    "hits INTEGER NOT NULL DEFAULT 0," +
                    "time_created INTEGER" +
                    ");");

            LOGGER.info("Guild settings table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static ObjectMapper getMapper() {
        return mapper;
    }


    public static List<URL> getAllURLs() {

        List<URL> urls = new ArrayList<>();

        try (final Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM urls")) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                urls.add(new URL(result.getString("code"))
                        .setUrl(result.getString("url"))
                        .setPermanent(result.getBoolean("permanent"))
                        .setHits(result.getLong("hits")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urls;
    }
}
