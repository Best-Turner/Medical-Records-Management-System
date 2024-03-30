package ru.astondevs.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.astondevs.util.ReadResources;
import ru.astondevs.util.ReaderFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConnector implements ConnectionManager {
    private static final String KEY_URL = "url";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private ReaderFile readerFile;

    public DatabaseConnector(ReaderFile readerFile) {
        this.readerFile = readerFile;
    }

    private HikariDataSource dataSource;
    private static final String PATH_PROPERTIES = "application.properties";


    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = new HikariDataSource(configuration());
        }
        return dataSource.getConnection();
    }

    private HikariConfig configuration() {
        Map<String, String> data = getConnectionDetails();
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(data.get(KEY_URL));
        config.setUsername(data.get(KEY_NAME));
        config.setPassword(data.get(KEY_PASSWORD));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setDriverClassName("org.postgresql.ds.PGSimpleDataSource");
        return config;
    }

    private Map<String, String> getConnectionDetails() {
        InputStream resource = readerFile.read(PATH_PROPERTIES);
        Map<String, String> connectionDetails = new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(resource);
            connectionDetails.put(KEY_URL, properties.get(KEY_URL).toString());
            connectionDetails.put(KEY_NAME, properties.get(KEY_NAME).toString());
            connectionDetails.put(KEY_PASSWORD, properties.get(KEY_PASSWORD).toString());
            System.out.println("Прочитано!");
        } catch (IOException e) {
            System.out.println("Данные для подключения не найдены");
        }
        return connectionDetails;
    }
}
