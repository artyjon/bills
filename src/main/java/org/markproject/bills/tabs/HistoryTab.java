package org.markproject.bills.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.markproject.bills.alerts.PopUpAlert;
import org.markproject.bills.controllers.HistoryController;
import org.markproject.bills.dto.HistoryRecord;
import java.time.Month;
import java.util.Comparator;

public class HistoryTab {

    private final HistoryController historyController = new HistoryController();
    private final PopUpAlert alert = new PopUpAlert();

    public Node createHistoryTab() {
        TableView<HistoryRecord> tableView = new TableView<>();
        tableView.setStyle("-fx-font-size: 14px;");

        // Колонка "Месяц" с сортировкой по календарному порядку
        TableColumn<HistoryRecord, String> monthColumn = new TableColumn<>("Месяц");
        monthColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getLocalizedMonth()));
        monthColumn.setComparator(Comparator.comparingInt(r -> Month.valueOf(r.toUpperCase()).getValue()));
        monthColumn.setPrefWidth(100);

        // Остальные колонки
        TableColumn<HistoryRecord, Double> coldColumn = new TableColumn<>("ХВС (м³)");
        coldColumn.setCellValueFactory(
                data -> data.getValue().coldWaterProperty().asObject());
        coldColumn.setPrefWidth(100);

        TableColumn<HistoryRecord, Double> hotColumn = new TableColumn<>("ГВС (м³)");
        hotColumn.setCellValueFactory(
                data -> data.getValue().hotWaterProperty().asObject());
        hotColumn.setPrefWidth(100);

        TableColumn<HistoryRecord, Double> sewerColumn = new TableColumn<>("Водоотведение (м³)");
        sewerColumn.setCellValueFactory(
                data -> data.getValue().sewerProperty().asObject());
        sewerColumn.setPrefWidth(120);

        TableColumn<HistoryRecord, Double> electricityDayColumn = new TableColumn<>("Электроэнергия (кВт⋅ч) Дн.");
        electricityDayColumn.setCellValueFactory(
                data -> data.getValue().electricityDayProperty().asObject());
        electricityDayColumn.setPrefWidth(150);

        TableColumn<HistoryRecord, Double> electricityNightColumn = new TableColumn<>("Электроэнергия (кВт⋅ч) Ноч.");
        electricityNightColumn.setCellValueFactory(
                data -> data.getValue().electricityNightProperty().asObject());
        electricityNightColumn.setPrefWidth(150);

        TableColumn<HistoryRecord, Double> totalColumn = new TableColumn<>("Сумма (руб.)");
        totalColumn.setCellValueFactory(
                data -> data.getValue().totalProperty().asObject());
        totalColumn.setPrefWidth(100);

        // Колонка с кнопкой удаления
        TableColumn<HistoryRecord, Void> actionColumn = new TableColumn<>("Действие");
        actionColumn.setPrefWidth(100);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Удалить");

            {
                deleteButton.setOnAction(event -> {
                    HistoryRecord r = getTableView().getItems().get(getIndex());
                    alert.showDeleteConfirmation(r, tableView);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        tableView.getColumns().addAll(
                monthColumn, coldColumn, hotColumn, sewerColumn,
                electricityDayColumn, electricityNightColumn, totalColumn, actionColumn
        );

        // Кнопка "Обновить"
        Button refreshButton = new Button("Обновить");
        refreshButton.setOnAction(e -> tableView.setItems(historyController.loadHistoryData()));

        VBox layout = new VBox(10, tableView, refreshButton);
        layout.setPadding(new Insets(10));

        return layout;
    }
}
