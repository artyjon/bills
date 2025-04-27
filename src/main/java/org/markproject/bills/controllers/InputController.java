package org.markproject.bills.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputController {

    private InputController() {}

    private static final Logger LOG = LoggerFactory.getLogger(InputController.class);

    public static double commaToDot(String value) {
        try {
            return Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException e) {
            LOG.info("Неверный формат числа. Используйте точку или запятую как разделитель.");
            throw new NumberFormatException("Неверный формат числа. Используйте точку или запятую как разделитель.");
        }
    }
}
