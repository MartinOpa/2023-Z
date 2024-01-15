package com.example.tamz2test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.tamz2test.Config;
import com.example.tamz2test.ConfigItem;
import com.example.tamz2test.ServiceItem;
import com.example.tamz2test.Utils;
import com.example.tamz2test.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemDao extends DataAccess {
    private static final String TABLE_NAME = "ServiceItem";
    private static final String ID_COL = "id";
    private static final String VIN_COL = "vin";
    private static final String INTERVALS_COL = "intervals";
    private static final String MILEAGE_COL = "mileage";
    private static final String DATE_COL = "date";
    private static final String USERNOTE_COL = "userNote";

    public ServiceItemDao(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY," +
                VIN_COL + " TEXT," +
                INTERVALS_COL + " TEXT," +
                MILEAGE_COL + " INTEGER," +
                DATE_COL + " INTEGER," +
                USERNOTE_COL + " TEXT" +
                ")";

        db.execSQL(query);
    }

    public void addServiceItem(ServiceItem serviceItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(VIN_COL, serviceItem.getVehicleVin());
        values.put(INTERVALS_COL, serviceItem.getStringIntervalTypes());
        values.put(MILEAGE_COL, serviceItem.getMileage());
        values.put(DATE_COL, serviceItem.getDate());
        values.put(USERNOTE_COL, serviceItem.getUserNote());
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void updateServiceItem(ServiceItem serviceItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String selection = ID_COL + " LIKE ?";
        String[] selectionArgs = { Integer.toString(serviceItem.getId()) };

        values.put(VIN_COL, serviceItem.getVehicleVin());
        values.put(INTERVALS_COL, serviceItem.getStringIntervalTypes());
        values.put(MILEAGE_COL, serviceItem.getMileage());
        values.put(DATE_COL, serviceItem.getDate());
        values.put(USERNOTE_COL, serviceItem.getUserNote());

        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void deleteServiceItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ID_COL + " LIKE ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void deleteServiceItems(String vin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = VIN_COL + " LIKE ?";
        String[] selectionArgs = { vin };
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public ServiceItem getServiceItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                VIN_COL,
                INTERVALS_COL,
                MILEAGE_COL,
                DATE_COL,
                USERNOTE_COL
        };

        String selection = ID_COL + " = ?";
        String[] selectionArgs = { Integer.toString(id) };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ServiceItem serviceItem = null;
        if ((cursor.getCount() == 1) && cursor.moveToNext()) {
            serviceItem = new ServiceItem(
                    id,
                    cursor.getString(cursor.getColumnIndexOrThrow(VIN_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(INTERVALS_COL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MILEAGE_COL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DATE_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERNOTE_COL))
            );
        }

        cursor.close();
        db.close();
        return serviceItem;
    }

    public List<ServiceItem> getServicePlan(Context context) {
        List<ServiceItem> servicePlan = new ArrayList<ServiceItem>();

        VehicleDao vehicleDao = new VehicleDao(context);
        List<Vehicle> vehicleList = vehicleDao.getVehicleItems();
        Config config = Config.loadConfig(context);
        List<ConfigItem> configItems = config.getIntervalConfig();

        for (Vehicle vehicle : vehicleList) {
            for (ConfigItem configItem : configItems) {
                ServiceItem serviceItem = getServiceItemPlan(vehicle.getVin(), configItem.getName());

                List<String> intervalType = new ArrayList<String>();
                intervalType.add(configItem.getName());
                if (serviceItem == null) {
                    servicePlan.add(new ServiceItem(
                            0,
                            vehicle.getVin(),
                            intervalType,
                            vehicle.getMileage(),
                            Utils.currentTimeToTimestamp(0),
                            "",
                            true
                    ));
                } else {
                    servicePlan.add(new ServiceItem(
                            0,
                            vehicle.getVin(),
                            intervalType,
                            serviceItem.getMileage() + configItem.getValue(),
                            Utils.currentTimeToTimestamp(configItem.getMonths()),
                            "",
                            (serviceItem.getMileage() + configItem.getValue() <= vehicle.getMileage() - 1000) ||
                             Utils.getIsDue(configItem.getMonths(), serviceItem.getDate())
                    ));
                }
            }
        }

        return servicePlan;
    }

    public ServiceItem getServiceItemPlan(String vin, String interval) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                ID_COL,
                VIN_COL,
                INTERVALS_COL,
                MILEAGE_COL,
                DATE_COL,
                USERNOTE_COL
        };

        String selection = VIN_COL + " LIKE ? AND " + INTERVALS_COL + " LIKE '%' || ? || '%'";
        String[] selectionArgs = { vin, interval };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                DATE_COL + " DESC"
        );

        ServiceItem serviceItem = null;
        if (cursor.moveToNext()) {
            serviceItem = new ServiceItem(
                                cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)),
                                cursor.getString(cursor.getColumnIndexOrThrow(VIN_COL)),
                                cursor.getString(cursor.getColumnIndexOrThrow(INTERVALS_COL)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(MILEAGE_COL)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DATE_COL)),
                                cursor.getString(cursor.getColumnIndexOrThrow(USERNOTE_COL))
                            );
        }

        cursor.close();
        db.close();
        return serviceItem;
    }

    public List<ServiceItem> getServiceItems(String vin) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                ID_COL,
                VIN_COL,
                INTERVALS_COL,
                MILEAGE_COL,
                DATE_COL,
                USERNOTE_COL
        };

        Cursor cursor;
        if (!vin.equals("")) {
            String selection = VIN_COL + " LIKE ?";
            String[] selectionArgs = { vin };

            cursor = db.query(
                    TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        } else {
            cursor = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }


        List<ServiceItem> serviceItemList = new ArrayList<ServiceItem>();

        while (cursor.moveToNext()) {
            serviceItemList.add(new ServiceItem(
                            cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(VIN_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(INTERVALS_COL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(MILEAGE_COL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DATE_COL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(USERNOTE_COL))
                    )
            );
        }

        cursor.close();
        db.close();
        return serviceItemList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
