package org.markproject.bills.tabs;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.markproject.bills.alerts.PopUpAlert;
import org.markproject.bills.controllers.CalculationController;
import org.markproject.bills.controllers.HistoryController;
import org.markproject.bills.controllers.TariffsController;

import java.time.Month;

public class CalculationTab {

    private final CalculationController calcController = new CalculationController(
            new TariffsController(),
            new HistoryController(),
            new PopUpAlert()
    );


    public GridPane createCalculationTab() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label monthLabel = new Label("Месяц:");
        ComboBox<Month> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(Month.values());

        Label coldLabel = new Label("ХВС (м3):");
        TextField coldField = new TextField();
        Label hotLabel = new Label("ГВС (м3):");
        TextField hotField = new TextField();
        Label sewerLabel = new Label("Водоотведение (м3):");
        TextField sewerField = new TextField();
        Label electricityDayLabel = new Label("Электроэнергия (день, кВт*ч):");
        TextField electricityDayField = new TextField();
        Label electricityNightLabel = new Label("Электроэнергия (ночь, кВт*ч):");
        TextField electricityNightField = new TextField();
        Label resultLabel = new Label();

        Button calculateButton = new Button("Рассчитать");
        calculateButton.setOnAction(e -> calcController.calculateBill(
                monthComboBox, coldField, hotField, sewerField,
                electricityDayField, electricityNightField, resultLabel));

        grid.add(monthLabel, 0, 0);
        grid.add(monthComboBox, 1, 0);
        grid.add(coldLabel, 0, 1);
        grid.add(coldField, 1, 1);
        grid.add(hotLabel, 0, 2);
        grid.add(hotField, 1, 2);
        grid.add(sewerLabel, 0, 3);
        grid.add(sewerField, 1, 3);
        grid.add(electricityDayLabel, 0, 4);
        grid.add(electricityDayField, 1, 4);
        grid.add(electricityNightLabel, 0, 5);
        grid.add(electricityNightField, 1, 5);
        grid.add(calculateButton, 1, 6);
        grid.add(resultLabel, 1, 7);

        return grid;
    }
}
