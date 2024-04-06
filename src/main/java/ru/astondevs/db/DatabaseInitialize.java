package ru.astondevs.db;

import ru.astondevs.util.ReaderFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialize {

    private static final String PATH_SQL_SCRIPT = "initDb.sql";
    private final ConnectionManager connectionManager;
    private ReaderFile readerFile;

    public DatabaseInitialize(ConnectionManager connectionManager, ReaderFile readerFile) {
        this.connectionManager = connectionManager;
        this.readerFile = readerFile;
    }

    public void initDataBase() {
        String sqlText = getSqlText();
        String[] sqlCommand = getSqlCommand(sqlText);
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            for (String sql : sqlCommand) {
                statement.executeUpdate(sql);
                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String[] getSqlCommand(String textSql) {
        return textSql.split(";");
    }

    private String getSqlText() {
        StringBuilder builder = new StringBuilder();

        try (InputStream read = readerFile.read(PATH_SQL_SCRIPT);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(read, "UTF-8"))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                builder.append(temp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }
}
