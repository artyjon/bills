package org.markproject.bills.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import org.markproject.bills.controllers.HistoryController;
import org.markproject.bills.dto.HistoryRecord;

import java.util.Optional;

public class PopUpAlert {

    private final HistoryController historyController = new HistoryController();

    public void showDeleteConfirmation(HistoryRecord r, TableView<HistoryRecord> tableView) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы точно хотите удалить запись?");
        alert.setContentText("Месяц: " + r.getMonth());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            historyController.deleteRecordFromDatabase(r);
            tableView.getItems().remove(r); // Удаляем запись из таблицы
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
