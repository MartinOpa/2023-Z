package com.example.tamz2test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServiceItem {
    private int id;
    private String vehicleVin;
    private List<String> intervalTypes;
    private int mileage;
    private int date;
    private String userNote;
    private boolean dueSoon;

    public ServiceItem(int id, String vehicleVin, List<String> intervalTypes, int mileage, int date, String userNote) {
        this.id = id;
        this.vehicleVin = vehicleVin;
        this.intervalTypes = intervalTypes;
        this.mileage = mileage;
        this.date = date;
        this.userNote = userNote;
        this.dueSoon = false;
    }

    public ServiceItem(int id, String vehicleVin, List<String> intervalTypes, int mileage, int date, String userNote, boolean dueSoon) {
        this.id = id;
        this.vehicleVin = vehicleVin;
        this.intervalTypes = intervalTypes;
        this.mileage = mileage;
        this.date = date;
        this.userNote = userNote;
        this.dueSoon = dueSoon;
    }

    public ServiceItem(int id, String vehicleVin, String intervalTypes, int mileage, int date, String userNote) {
        this.id = id;
        this.vehicleVin = vehicleVin;
        this.setIntervalTypes(intervalTypes);
        this.mileage = mileage;
        this.date = date;
        this.userNote = userNote;
        this.dueSoon = false;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public List<String> getIntervalTypes() {
        return intervalTypes;
    }

    public void setIntervalTypes(List<String> intervalTypes) {
        this.intervalTypes = intervalTypes;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStringIntervalTypes() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        String jsonString = gson.toJson(this.intervalTypes, listType);

        return jsonString;
    }

    public String getStringIntervalTypesForDesc() {
        String res = "";

        for (int i = 0; i < this.intervalTypes.size(); i++) {
            res += this.intervalTypes.get(i);
            if (i < this.intervalTypes.size() - 1) {
                res += ", ";
            }
        }

        return res;
    }

    public void setIntervalTypes(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        this.intervalTypes = gson.fromJson(jsonString, listType);
    }

    public void addIntervalType(String intervalType) {
        if (this.intervalTypes == null) {
            this.intervalTypes = new ArrayList<String>();
        }

        this.intervalTypes.add(intervalType);
    }

    public boolean isDueSoon() {
        return this.dueSoon;
    }
}
