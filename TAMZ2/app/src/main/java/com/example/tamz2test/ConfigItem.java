package com.example.tamz2test;

import android.content.Context;

public class ConfigItem implements Comparable<ConfigItem> {
    private String name;
    private int value;
    private int months;
    private boolean selected;

    public ConfigItem(String name, int value, int months) {
        this.name = name;
        this.value = value;
        this.months = months;
    }

    public String getName() {
        return name;
    }

    public String getTimeIntervalStr(String locYear, String locMonth) {
        return String.format("%d" +
                            locYear +
                            " %d" +
                            locMonth,
                            this.months/12,
                            this.months%12);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getMonths() {
        return this.months;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(ConfigItem configItem) {
        return this.value - configItem.value;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
