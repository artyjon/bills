package org.markproject.bills.dto;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class HistoryRecord {
    private final StringProperty month;
    private final DoubleProperty coldWater;
    private final DoubleProperty hotWater;
    private final DoubleProperty sewer;
    private final DoubleProperty electricityDay;
    private final DoubleProperty electricityNight;
    private final DoubleProperty total;

    public HistoryRecord(String month, double coldWater, double hotWater, double sewer,
                         double electricityDay, double electricityNight, double total) {
        this.month = new SimpleStringProperty(month.toUpperCase()); // Сохраняем в верхнем регистре для корректного сравнения
        this.coldWater = new SimpleDoubleProperty(coldWater);
        this.hotWater = new SimpleDoubleProperty(hotWater);
        this.sewer = new SimpleDoubleProperty(sewer);
        this.electricityDay = new SimpleDoubleProperty(electricityDay);
        this.electricityNight = new SimpleDoubleProperty(electricityNight);
        this.total = new SimpleDoubleProperty(total);
    }

    public String getMonth() {
        return month.get();
    }

    public String getLocalizedMonth() {
        try {
            Month parsedMonth = Month.valueOf(month.get().toUpperCase());
            String localizedMonth = parsedMonth.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
            return localizedMonth.substring(0, 1).toUpperCase() + localizedMonth.substring(1).toLowerCase();
        } catch (IllegalArgumentException e) {
            return month.get(); // Если месяц не найден, возвращаем как есть
        }
    }


    public DoubleProperty coldWaterProperty() {
        return coldWater;
    }


    public DoubleProperty hotWaterProperty() {
        return hotWater;
    }


    public DoubleProperty sewerProperty() {
        return sewer;
    }


    public DoubleProperty electricityDayProperty() {
        return electricityDay;
    }


    public DoubleProperty electricityNightProperty() {
        return electricityNight;
    }


    public DoubleProperty totalProperty() {
        return total;
    }
}
