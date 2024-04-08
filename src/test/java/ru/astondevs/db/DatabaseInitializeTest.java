package ru.astondevs.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.astondevs.util.ReaderFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Testcontainers
class DatabaseInitializeTest {

    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD_DB)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
    @Mock
    private ConnectionManager connectionManager;
    @Mock
    private ReaderFile readerFile;
    @InjectMocks
    private DatabaseInitialize initialize;

    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatingDataBase() {
        final String createSomeTable = "CREATE TABLE some_table" +
                "(id serial primary key not null," +
                "name varchar(100) not null," +
                "age int not null);";
        String existsTable = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'some_table';";
        try (Connection connection = new TestConnection().getConnection();
             Statement statement = connection.createStatement()) {
            InputStream inputStream = new ByteArrayInputStream(createSomeTable.getBytes());
            when(readerFile.read(anyString())).thenReturn(inputStream);
            when(connectionManager.getConnection()).thenReturn(new TestConnection().getConnection());
            initialize.initDataBase();
            ResultSet resultSet = statement.executeQuery(existsTable);
            assertNotNull(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        final String dropSomeTable = "DROP TABLE some_table;";
        try (Connection connection = new TestConnection().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropSomeTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestConnection implements ConnectionManager {
        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), USERNAME, PASSWORD_DB);
        }
    }
}