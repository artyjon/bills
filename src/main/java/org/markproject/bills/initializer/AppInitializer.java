package org.markproject.bills.initializer;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.markproject.bills.connection.LocalConnection;
import org.markproject.bills.tabs.CalculationTab;
import org.markproject.bills.tabs.HistoryTab;
import org.markproject.bills.tabs.TariffsTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class AppInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);

    private final TariffsTab tariffs = new TariffsTab();
    private final CalculationTab calculation = new CalculationTab();
    private final HistoryTab history = new HistoryTab();

    public void init(Stage stage) {

        try {
            // Инициализируем соединение в основном потоке
            LocalConnection.getInstance().getConnection();
            LOG.info("Соединение с базой данных установлено");
        } catch (SQLException e) {
            LOG.error("Ошибка инициализации БД: {}", e.getMessage());
        }

        // Вывод переменных среды
        LOG.info("Environment Variables:");
        System.getenv().forEach((key, value) -> LOG.info("{} = {}", key, value));

        // Вывод системных свойств (например, рабочая директория)
        LOG.info("\nSystem Properties:");
        System.getProperties().forEach((key, value) -> LOG.info("{} = {}", key, value));

        // Создание вкладок
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Отключаем закрытие вкладок

        Tab tariffsTab = new Tab("Тарифы");
        tariffsTab.setContent(tariffs.createTariffsTab());

        Tab calculationTab = new Tab("Расчет");
        calculationTab.setContent(calculation.createCalculationTab());

        Tab historyTab = new Tab("История");
        historyTab.setContent(history.createHistoryTab());

        tabPane.getTabs().addAll(tariffsTab, calculationTab, historyTab);

        // Создание сцены
        Scene scene = new Scene(tabPane, 1000, 600); // Начальный размер окна
        stage.setScene(scene);
        stage.setTitle("Коммунальные платежи");

        // Установка минимальных размеров окна
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Отображение окна
        stage.show();
    }
}
