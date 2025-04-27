package org.markproject.bills.controllers;

import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public abstract class JavaFXTestBase {

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");

        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @AfterEach
    public void cleanup() throws Exception {
        FxToolkit.cleanupStages();
    }

    public abstract void start(Stage stage) throws Exception;
}