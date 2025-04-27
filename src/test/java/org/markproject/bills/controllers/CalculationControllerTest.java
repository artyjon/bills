package org.markproject.bills.controllers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.time.Month;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.markproject.bills.UtilityBillApp;
import org.testfx.api.FxRobot;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

class CalculationControllerTest extends JavaFXTestBase {

    static {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
    }

    @Override
    public void start(Stage stage) throws Exception {
        new UtilityBillApp().start(stage);
    }

    @Test
    void calculateBill_withValidInput_shouldShowCorrectResult(FxRobot robot) throws TimeoutException {
        // Заполняем данные
        fillTariffs(robot);

        // Переходим на вкладку расчета
        robot.clickOn("#calculationTab");

        // Заполняем поля расчета
        fillCalculationFields(robot, "10.5", "5.2", "3.0", "100", "50");

        // Выбираем месяц
        ComboBox<Month> monthCombo = robot.lookup("#monthCombo").queryComboBox();
        robot.interact(() -> monthCombo.getSelectionModel().select(Month.JANUARY));

        // Нажимаем кнопку расчета
        robot.clickOn("#calculateButton");

        // Проверяем результат
        verifyThat("#resultLabel", hasText("Общая сумма: 3815.0 руб."));
    }

    private void fillTariffs(FxRobot robot) {
        robot.clickOn("#tariffsTab");

        robot.clickOn("#coldTariffField").write("30");
        robot.clickOn("#hotTariffField").write("50");
        robot.clickOn("#sewerTariffField").write("20");
        robot.clickOn("#dayTariffField").write("4.5");
        robot.clickOn("#nightTariffField").write("2.0");

        robot.clickOn("#saveTariffsButton");
    }

    private void fillCalculationFields(FxRobot robot, String cold, String hot, String sewer, String day, String night) {
        setTextFieldValue(robot, "#coldField", cold);
        setTextFieldValue(robot, "#hotField", hot);
        setTextFieldValue(robot, "#sewerField", sewer);
        setTextFieldValue(robot, "#dayField", day);
        setTextFieldValue(robot, "#nightField", night);
    }

    private void setTextFieldValue(FxRobot robot, String selector, String value) {
        TextField field = robot.lookup(selector).query();
        robot.interact(() -> field.setText(value));
    }
}