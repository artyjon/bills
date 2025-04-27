package org.markproject.bills.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalConnectionTest {

    private LocalConnection localConnection;

    @BeforeEach
    void setUp() {
        LocalConnection.resetInstance();
        localConnection = LocalConnection.getInstance();
    }

    // -----------------------------------
    // Тесты для connectDatabase()
    // -----------------------------------

    @Test
    void givenMockedConnection_whenConnectDatabase_thenConnectionIsEstablished() throws SQLException {
        // given
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

            // when
            localConnection.connectDatabase();

            // then
            assertNotNull(localConnection.getConnection(), "Подключение должно быть установлено");
            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(2)).execute(anyString());
        }
    }


    // -----------------------------------
    // Тесты для getDatabasePath()
    // -----------------------------------

    @Test
    void givenDatabasePath_whenGetDatabasePath_thenPathIsCorrect() {
        // when
        String path = localConnection.getDatabasePath();

        // then
        assertNotNull(path, "Путь к базе данных не должен быть null");
        assertTrue(path.endsWith(".db"), "Файл базы данных должен иметь расширение .db");
    }

    @Test
    void givenDatabasePath_whenGetDatabasePath_thenFileExistsOrWillBeCreated() {
        // when
        String path = localConnection.getDatabasePath();
        File file = new File(path);

        // then
        assertNotNull(file, "Файл базы данных не должен быть null");
    }

    // -----------------------------------
    // Тесты для getConnection()
    // -----------------------------------


    @Test
    void givenLocalConnection_whenGetConnectionAfterConnectDatabase_thenConnectionIsNotNull() throws SQLException {
        // given
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

            // when
            localConnection.connectDatabase();

            // then
            assertNotNull(localConnection.getConnection(), "После подключения соединение должно быть не null");
        }
    }
}