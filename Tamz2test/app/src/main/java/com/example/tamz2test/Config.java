package com.example.tamz2test;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.tamz2test.Unit;
import com.example.tamz2test.ServiceItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private boolean notifyOn;
    private Unit units;
    private List<ConfigItem> intervalConfig;

    private static String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    private boolean create(Context context, String fileName, String jsonString){
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }

    public static Config loadConfig(Context context) {
        Gson gson = new Gson();
        String jsonString = read(context, "appConfig.json");
        return gson.fromJson(jsonString, Config.class);
    }

    public void save(Context context) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this, Config.class);
        create(context, "appConfig.json", jsonString);
    }

    public boolean isNotifyOn() {
        return notifyOn;
    }

    public Unit getUnits() {
        return units;
    }

    public List<ConfigItem> getIntervalConfig() {
        return intervalConfig;
    }

    public void setNotifyOn(boolean notifyOn) {
        this.notifyOn = notifyOn;
    }

    public void setUnits(Unit units) {
        if (this.units == units) {
            return;
        }

        this.units = units;
    }

    public void setIntervalConfig(List<ConfigItem> intervalConfig) {
        this.intervalConfig = intervalConfig;
    }

    /*public void convertToImperial() {

    }

    public void convertToMetric() {

    }*/

    public void addInterval(ConfigItem configItem) {
        this.intervalConfig.add(configItem);
    }

    public void restoreFactorySettings(Context context) {
        this.notifyOn = true;
        this.units = Unit.METRIC;
        this.intervalConfig = new ArrayList<ConfigItem>();

        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsOil), 10000, 12));
        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsAirIntake), 20000, 18));
        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsAirCabin), 20000, 12));
        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsSparkPlugs), 50000, 24));
        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsBelt), 100000, 90));
        this.intervalConfig.add(new ConfigItem(context.getString(R.string.factorySettingsFuel), 100000, 90));
    }
}
