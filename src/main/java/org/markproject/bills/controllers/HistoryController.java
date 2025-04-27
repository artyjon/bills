package org.markproject.bills.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.markproject.bills.connection.LocalConnection;
import org.markproject.bills.dto.HistoryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;

public class HistoryController {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryController.class);
    private final LocalConnection localConnection = LocalConnection.getInstance();


    public void updateHistoryTable() throws SQLException {
        try (Statement stmt = localConnection.getConnection().createStatement()) {
            // Проверяем, существует ли столбец total в таблице history
            ResultSet rs = localConnection.getConnection().getMetaData().getColumns(null, null, "history", "total");
            if (!rs.next()) {
                LOG.info("Столбец 'total' не найден. Добавляем его...");
                stmt.execute("ALTER TABLE history ADD COLUMN total REAL");
                LOG.info("Столбец 'total' успешно добавлен.");
            } else {
                LOG.info("Столбец 'total' уже существует.");
            }
        }
    }

    public void saveToHistory(String month, double cold, double hot, double sewer,
                               double electricityDay, double electricityNight, double total) throws SQLException {
        String sqlUpdate = "UPDATE history SET cold_water = ?, hot_water = ?, sewer = ?, electricity_day = ?, electricity_night = ?, total = ? WHERE month = ?";
        try (PreparedStatement stmt = localConnection.getConnection().prepareStatement(sqlUpdate)) {
            stmt.setDouble(1, cold);
            stmt.setDouble(2, hot);
            stmt.setDouble(3, sewer);
            stmt.setDouble(4, electricityDay);
            stmt.setDouble(5, electricityNight);
            stmt.setDouble(6, total);
            stmt.setString(7, month);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) { // Если обновление не затронуло строки
                String sqlInsert = "INSERT INTO history (month, cold_water, hot_water, sewer, electricity_day, electricity_night, total) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = localConnection.getConnection().prepareStatement(sqlInsert)) {
                    insertStmt.setString(1, month);
                    insertStmt.setDouble(2, cold);
                    insertStmt.setDouble(3, hot);
                    insertStmt.setDouble(4, sewer);
                    insertStmt.setDouble(5, electricityDay);
                    insertStmt.setDouble(6, electricityNight);
                    insertStmt.setDouble(7, total);
                    insertStmt.executeUpdate();
                }
            } else {
                LOG.info("Данные успешно обновлены.");
            }
        }
    }

    public ObservableList<HistoryRecord> loadHistoryData() {
        ObservableList<HistoryRecord> history = FXCollections.observableArrayList();
        String sqlPath = localConnection.getDatabasePath();
        File sqlFile = new File(sqlPath);

        if (!sqlFile.exists()) {
            LOG.error("Файл базы данных не найден: {}", sqlPath);
            return history;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqlPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT * FROM history ORDER BY strftime('%m', month || '-01') DESC")) {

            while (rs.next()) {
                history.add(new HistoryRecord(
                        rs.getString("month"),
                        rs.getDouble("cold_water"),
                        rs.getDouble("hot_water"),
                        rs.getDouble("sewer"),
                        rs.getDouble("electricity_day"),
                        rs.getDouble("electricity_night"),
                        rs.getDouble("total")
                ));
            }
            LOG.info("Данные истории успешно загружены.");
        } catch (SQLException e) {
            LOG.error("Ошибка загрузки истории: {}", e.getMessage());
        }
        return history;
    }

    public void deleteRecordFromDatabase(HistoryRecord r) {
        String sqlPath = localConnection.getDatabasePath();
        String query = "DELETE FROM history WHERE month = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqlPath);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, r.getMonth());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOG.info("Запись удалена: {}", r.getMonth());
            } else {
                LOG.error("Запись с таким месяцем не найдена.");
            }
        } catch (SQLException e) {
            LOG.error("Ошибка удаления записи: {}", e.getMessage());
        }
    }
}
