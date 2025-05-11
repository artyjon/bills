module org.markproject.bills {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires org.slf4j;
    requires javafx.swing; // Добавьте, если используется JFXPanel

    opens org.markproject.bills to javafx.fxml;
    opens org.markproject.bills.controllers to javafx.fxml;

    exports org.markproject.bills;
    exports org.markproject.bills.controllers;
    exports org.markproject.bills.alerts;
}