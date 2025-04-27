package org.markproject.bills.tabs;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.markproject.bills.controllers.TariffsController;

public class TariffsTab {

    private final TariffsController tariffsController = new TariffsController();

    public GridPane createTariffsTab() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label coldLabel = new Label("ХВС (руб/м3):");
        TextField coldField = new TextField();
        Label hotLabel = new Label("ГВС (руб/м3):");
        TextField hotField = new TextField();
        Label sewerLabel = new Label("Водоотведение (руб/м3):");
        TextField sewerField = new TextField();
        Label electricityDayLabel = new Label("Электроэнергия (день, руб/кВт*ч):");
        TextField electricityDayField = new TextField();
        Label electricityNightLabel = new Label("Электроэнергия (ночь, руб/кВт*ч):");
        TextField electricityNightField = new TextField();

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> tariffsController.saveTariffs(
                coldField, hotField, sewerField, electricityDayField, electricityNightField));

        grid.add(coldLabel, 0, 0);
        grid.add(coldField, 1, 0);
        grid.add(hotLabel, 0, 1);
        grid.add(hotField, 1, 1);
        grid.add(sewerLabel, 0, 2);
        grid.add(sewerField, 1, 2);
        grid.add(electricityDayLabel, 0, 3);
        grid.add(electricityDayField, 1, 3);
        grid.add(electricityNightLabel, 0, 4);
        grid.add(electricityNightField, 1, 4);
        grid.add(saveButton, 1, 5);

        return grid;
    }
}
