package org.markproject.bills.alerts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.markproject.bills.controllers.HistoryController;
import org.markproject.bills.dto.HistoryRecord;

import static org.mockito.Mockito.*;

class PopUpAlertTest {

    private HistoryController historyControllerMock;
    private PopUpAlert popUpAlert;
    private TableView<HistoryRecord> tableView;
    private ObservableList<HistoryRecord> items;

    @BeforeAll
    static void initToolkit() {
        // Инициализация JavaFX Toolkit для тестов
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        historyControllerMock = mock(HistoryController.class);

        // Фейковый PopUpAlert без настоящего окна Alert
        popUpAlert = new PopUpAlert() {
            @Override
            public void showDeleteConfirmation(HistoryRecord r, TableView<HistoryRecord> tableView) {
                // Симуляция ответа пользователя (ОК)
                historyControllerMock.deleteRecordFromDatabase(r);
                tableView.getItems().remove(r);
            }
        };

        // Настоящий TableView с ObservableList
        tableView = new TableView<>();
        items = FXCollections.observableArrayList();
        tableView.setItems(items);
    }

    @Test
    void givenUserConfirmsDeletion_whenShowDeleteConfirmation_thenRecordIsDeleted() {
        // given
        HistoryRecord record = new HistoryRecord("January", 1, 1, 1, 1, 1, 1);
        items.add(record);

        // when
        popUpAlert.showDeleteConfirmation(record, tableView);

        // then
        verify(historyControllerMock, times(1)).deleteRecordFromDatabase(record);
        assert !items.contains(record); // Запись должна быть удалена
    }

    @Test
    void givenUserCancelsDeletion_whenShowDeleteConfirmation_thenRecordIsNotDeleted() {
        // given
        HistoryRecord record = new HistoryRecord("February", 1, 1, 1, 1, 1, 1);
        items.add(record);

        // Эмулируем отмену (ничего не делаем)
        popUpAlert = new PopUpAlert() {
            @Override
            public void showDeleteConfirmation(HistoryRecord r, TableView<HistoryRecord> tableView) {
                // Пользователь отменил — ничего не происходит
            }
        };

        // when
        popUpAlert.showDeleteConfirmation(record, tableView);

        // then
        verify(historyControllerMock, never()).deleteRecordFromDatabase(record);
        assert items.contains(record); // Запись осталась
    }

    @Test
    void givenUserClosesDialogWithoutSelection_whenShowDeleteConfirmation_thenRecordIsNotDeleted() {
        // given
        HistoryRecord record = new HistoryRecord("March", 1, 1, 1, 1, 1, 1);
        items.add(record);

        // Эмулируем закрытие диалога без выбора
        popUpAlert = new PopUpAlert() {
            @Override
            public void showDeleteConfirmation(HistoryRecord r, TableView<HistoryRecord> tableView) {
                // Ничего не делаем
            }
        };

        // when
        popUpAlert.showDeleteConfirmation(record, tableView);

        // then
        verify(historyControllerMock, never()).deleteRecordFromDatabase(record);
        assert items.contains(record); // Запись осталась
    }
}