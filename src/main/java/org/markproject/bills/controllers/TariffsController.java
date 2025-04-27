package org.markproject.bills.controllers;

import javafx.scene.control.TextField;
import org.markproject.bills.alerts.PopUpAlert;
import org.markproject.bills.connection.LocalConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.markproject.bills.controllers.InputController.commaToDot;

public class TariffsController {

    private final PopUpAlert alert = new PopUpAlert();

    private final LocalConnection localConnection = LocalConnection.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(TariffsController.class);

    public double getTariff(String type) throws SQLException {
        String query = "SELECT " + type + " FROM Tariffs";
        try (Statement stmt = localConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (!rs.next()) {
                throw new SQLException("Тарифы не найдены. Пожалуйста, установите тарифы.");
            }
            double tariff = rs.getDouble(1);
            if (tariff <= 0) {
                throw new SQLException("Некорректное значение тарифа для типа: " + type);
            }
            return tariff;
        }
    }

    public void saveTariffs(TextField cold, TextField hot, TextField sewer,
                            TextField electricityDay, TextField electricityNight) {
        Connection conn = null;
        try {
            conn = localConnection.getConnection();
            String checkQuery = "SELECT COUNT(*) FROM Tariffs";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                 ResultSet rs = checkStmt.executeQuery()) {

                rs.next();
                boolean exists = rs.getInt(1) > 0;

                String query = exists ?
                        "UPDATE Tariffs SET cold=?, hot=?, sewer=?, electricity_day=?, electricity_night=?" :
                        "INSERT INTO Tariffs (cold, hot, sewer, electricity_day, electricity_night) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, commaToDot(cold.getText()));
                    stmt.setDouble(2, commaToDot(hot.getText()));
                    stmt.setDouble(3, commaToDot(sewer.getText()));
                    stmt.setDouble(4, commaToDot(electricityDay.getText()));
                    stmt.setDouble(5, commaToDot(electricityNight.getText()));

                    int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    alert.showAlert("Тарифы успешно сохранены!");
                    LOG.info("Тарифы успешно сохранены! \n cold {}\n hot {}\n sewer {}\n electricityDay {}\n electricityNight {}"
                            , commaToDot(cold.getText())
                            , commaToDot(hot.getText())
                            , commaToDot(sewer.getText())
                            , commaToDot(electricityDay.getText())
                            , commaToDot(electricityNight.getText()));
                } else {
                    alert.showAlert("Ошибка при сохранении тарифов.");
                }
            }
                }

        } catch (NumberFormatException e) {
            alert.showAlert("Пожалуйста, введите числовые значения для тарифов.");
        } catch (SQLException e) {
            LOG.error("Ошибка при сохранении тарифов: {}", e.getMessage());
            alert.showAlert("Ошибка при сохранении тарифов.");
        } finally {
            // Не закрываем соединение здесь!
        }
    }

    public void initializeDefaultTariffs() throws SQLException {
        String query = "SELECT COUNT(*) FROM Tariffs";
        try (Statement stmt = localConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                LOG.info("Тарифы не найдены. Устанавливаются значения по умолчанию.");

                String insertQuery = "INSERT INTO Tariffs (cold, hot, sewer, electricity_day, electricity_night) " +
                                     "VALUES (30.0, 50.0, 20.0, 4.5, 3.0)";

                try (Statement insertStmt = localConnection.getConnection().createStatement()) {
                    int rowsAffected = insertStmt.executeUpdate(insertQuery);
                    if (rowsAffected > 0) {
                        LOG.info("Тарифы по умолчанию установлены.");
                    } else {
                        LOG.error("Ошибка при установке тарифов по умолчанию.");
                    }
                }
            } else {
                LOG.info("Тарифы уже существуют в базе данных.");
            }
        } catch (SQLException e) {
            LOG.error("Ошибка при инициализации тарифов: {}", e.getMessage());
            System.err.println("Ошибка при инициализации тарифов: " + e.getMessage());
        }
    }
}
