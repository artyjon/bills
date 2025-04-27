package org.markproject.bills.controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.markproject.bills.alerts.PopUpAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.Month;

import static org.markproject.bills.controllers.InputController.commaToDot;

public class CalculationController {

    private static final Logger LOG = LoggerFactory.getLogger(CalculationController.class);

    private final TariffsController tariffsController;
    private final HistoryController historyController;
    private final PopUpAlert alert;

    // Новый конструктор
    public CalculationController(TariffsController tariffsController, HistoryController historyController, PopUpAlert alert) {
        this.tariffsController = tariffsController;
        this.historyController = historyController;
        this.alert = alert;
    }

    public void calculateBill(ComboBox<Month> month, TextField cold, TextField hot, TextField sewer,
                               TextField electricityDay, TextField electricityNight, Label result) {
        try {
            // Проверяем ввод на корректность
            double coldValue = commaToDot(cold.getText());
            double hotValue = commaToDot(hot.getText());
            double sewerValue = commaToDot(sewer.getText());
            double electricityDayValue = commaToDot(electricityDay.getText());
            double electricityNightValue = commaToDot(electricityNight.getText());

            // Получаем тарифы
            double coldTariff = tariffsController.getTariff("cold");
            double hotTariff = tariffsController.getTariff("hot");
            double sewerTariff = tariffsController.getTariff("sewer");
            double electricityDayTariff = tariffsController.getTariff("electricity_day");
            double electricityNightTariff = tariffsController.getTariff("electricity_night");

            // Выполняем расчет
            double total = coldValue * coldTariff +
                           hotValue * hotTariff +
                           sewerValue * sewerTariff +
                           electricityDayValue * electricityDayTariff +
                           electricityNightValue * electricityNightTariff;

            result.setText("Общая сумма: " + total + " руб.");

            // Сохраняем расчет в историю
            historyController.saveToHistory(month.getValue().toString(), coldValue, hotValue, sewerValue,
                    electricityDayValue, electricityNightValue, total);

            alert.showAlert("Расчет выполнен успешно!");
        } catch (NumberFormatException e) {
            alert.showAlert("Пожалуйста, введите числовые значения для расчета.");
        } catch (SQLException e) {
            LOG.error("Ошибка при расчете платежей", e); // Выводим стек ошибки в консоль
            alert.showAlert("Ошибка при расчете платежей");
        }
    }
}
