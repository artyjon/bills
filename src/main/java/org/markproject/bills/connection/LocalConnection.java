package org.markproject.bills.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalConnection {

    private static final String DB_FILE = "utility_bills.sql";

    private Connection connection;

    private static final Logger LOG = LoggerFactory.getLogger(LocalConnection.class);

    private static LocalConnection localConnection;

    private  LocalConnection() {}
    public static LocalConnection getInstance() {
        if (localConnection == null) {
            localConnection = new LocalConnection();
        }
        return localConnection;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectDatabase();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOG.info("Соединение с базой данных закрыто");
            }
        } catch (SQLException e) {
            LOG.error("Ошибка при закрытии соединения: {}", e.getMessage());
        }
    }


    public String getDatabasePath() {
        try {
            // Получаем путь к базе данных в ресурсе .jar
            URL resource = getClass().getClassLoader().getResource(DB_FILE);

            if (resource != null) {
                // Если база данных существует как ресурс, копируем её во временную директорию
                File tempFile = new File(System.getProperty("user.home"),
                        "Library/Application Support/YourAppName/utility_bills.db");

                if (!tempFile.exists()) {
                    tempFile.getParentFile().mkdirs(); // Создаем директорию, если не существует
                    try (InputStream inputStream = resource.openStream();
                         OutputStream outputStream = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
                return tempFile.getAbsolutePath();
            } else {
                // Если база данных не существует, создаем её в рабочей директории
                String sqlPath = new File(DB_FILE).getAbsolutePath();
                copyDatabaseFromResources(sqlPath); // Копирование из ресурсов
                return sqlPath;
            }
        } catch (Exception e) {
            LOG.error("Ошибка получения пути к базе данных", e);
            return null;
        }
    }

    public void connectDatabase() {
        try {
            // Получаем путь к базе данных
            String sqlPath = getDatabasePath();

            // Подключаемся к базе данных
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqlPath);
            LOG.info("Подключение к базе данных успешно установлено.");

            // Создание таблиц, если они не существуют
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS Tariffs (" +
                             "cold REAL, hot REAL, sewer REAL, electricity_day REAL, electricity_night REAL)");
                stmt.execute("CREATE TABLE IF NOT EXISTS history (" +
                             "month TEXT PRIMARY KEY, cold_water REAL, hot_water REAL, sewer REAL, " +
                             "electricity_day REAL, electricity_night REAL, total REAL)");
               LOG.info("Таблицы успешно созданы.");
            }

        } catch (SQLException e) {
            LOG.error("Ошибка при подключении или работе с базой данных", e);
        }
    }

    private void copyDatabaseFromResources(String sqlPath) throws IOException {
        // Копирование базы данных из ресурсов, если она не существует
        File sqlFile = new File(sqlPath);
        if (!sqlFile.exists()) {
            try (InputStream sqlStream = getClass().getClassLoader().getResourceAsStream(DB_FILE);
                 OutputStream out = new FileOutputStream(sqlPath)) {
                if (sqlStream == null) {
                    throw new FileNotFoundException("Файл базы данных не найден в ресурсах!");
                }
                sqlStream.transferTo(out);
                LOG.info("Файл базы данных скопирован из ресурсов.");
            }
        } else {
            LOG.info("База данных уже существует, копирование не требуется.");
        }
    }

    public static void resetInstance() {
        localConnection = null;
    }

}
