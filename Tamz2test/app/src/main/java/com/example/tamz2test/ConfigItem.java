package com.example.tamz2test;

public class ConfigItem implements Comparable<ConfigItem> {
    private String name;
    private int value;
    private int months;

    public ConfigItem(String name, int value, int months) {
        this.name = name;
        this.value = value;
        this.months = months;
    }

    public ConfigItem(String name, int value) {
        this.name = name;
        this.value = value;
        this.months = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(ConfigItem configItem) {
        return this.value - configItem.value;
    }
}
