package org.markproject.bills;

import javafx.application.Application;
import javafx.stage.Stage;
import org.markproject.bills.connection.LocalConnection;
import org.markproject.bills.initializer.AppInitializer;

public class UtilityBillApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new AppInitializer().init(primaryStage);
    }

    @Override
    public void stop() {
        LocalConnection.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.verbose", "true");

        launch(args);
    }
}
