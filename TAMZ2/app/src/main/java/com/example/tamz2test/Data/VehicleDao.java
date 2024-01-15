package com.example.tamz2test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tamz2test.ConfigItem;
import com.example.tamz2test.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleDao extends DataAccess {
    private Context context;
    private static final String TABLE_NAME = "Vehicle";
    private static final String VIN_COL = "vin";
    private static final String MAKE_COL = "make";
    private static final String MODEL_COL = "model";
    private static final String YEAR_COL = "year";
    private static final String PLATE_COL = "plate";
    private static final String MILEAGE_COL = "mileage";
    private static final String CUSTOMNOTE_COL = "customNote";
    private static final String VEHICLEPIC_COL = "vehiclePic";

    public VehicleDao(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                VIN_COL + " TEXT PRIMARY KEY," +
                MAKE_COL + " TEXT," +
                MODEL_COL + " TEXT," +
                YEAR_COL + " INTEGER," +
                PLATE_COL + " TEXT," +
                MILEAGE_COL + " INTEGER," +
                CUSTOMNOTE_COL + " TEXT," +
                VEHICLEPIC_COL + " BLOB" +
                ")";

        db.execSQL(query);
    }

    public void addVehicleItem(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(VIN_COL, vehicle.getVin());
        values.put(MAKE_COL, vehicle.getMake());
        values.put(MODEL_COL, vehicle.getModel());
        values.put(YEAR_COL, vehicle.getYear());
        values.put(PLATE_COL, vehicle.getPlate());
        values.put(MILEAGE_COL, vehicle.getMileage());
        values.put(CUSTOMNOTE_COL, vehicle.getCustomNote());
        values.put(VEHICLEPIC_COL, vehicle.getVehiclePic());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateVehicleItem(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String selection = VIN_COL + " LIKE ?";
        String[] selectionArgs = { vehicle.getVin() };

        values.put(MAKE_COL, vehicle.getMake());
        values.put(MODEL_COL, vehicle.getModel());
        values.put(YEAR_COL, vehicle.getYear());
        values.put(PLATE_COL, vehicle.getPlate());
        values.put(MILEAGE_COL, vehicle.getMileage());
        values.put(CUSTOMNOTE_COL, vehicle.getCustomNote());
        values.put(VEHICLEPIC_COL, vehicle.getVehiclePic());

        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void deleteVehicleItem(String vin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = VIN_COL + " LIKE ?";
        String[] selectionArgs = { vin };
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        ServiceItemDao serviceItemDao = new ServiceItemDao(this.context);
        serviceItemDao.deleteServiceItems(vin);
    }

    public List<Vehicle> getVehicleItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                VIN_COL,
                MAKE_COL,
                MODEL_COL,
                YEAR_COL,
                PLATE_COL,
                MILEAGE_COL,
                CUSTOMNOTE_COL,
                VEHICLEPIC_COL
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();

        while (cursor.moveToNext()) {
            vehicleList.add(new Vehicle(
                cursor.getString(cursor.getColumnIndexOrThrow(VIN_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(MAKE_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(MODEL_COL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(YEAR_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(PLATE_COL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(MILEAGE_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMNOTE_COL)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(VEHICLEPIC_COL))
                )
            );
        }

        cursor.close();
        db.close();
        return vehicleList;
    }

    public Vehicle getVehicle(String vin) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                VIN_COL,
                MAKE_COL,
                MODEL_COL,
                YEAR_COL,
                PLATE_COL,
                MILEAGE_COL,
                CUSTOMNOTE_COL,
                VEHICLEPIC_COL
        };

        String selection = VIN_COL + " = ?";
        String[] selectionArgs = { vin };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if ((cursor.getCount() == 1) && cursor.moveToNext()) {
            Vehicle vehicle = new Vehicle(
                cursor.getString(cursor.getColumnIndexOrThrow(VIN_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(MAKE_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(MODEL_COL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(YEAR_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(PLATE_COL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(MILEAGE_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMNOTE_COL)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(VEHICLEPIC_COL))
            );

            cursor.close();
            db.close();
            return vehicle;
        } else {
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
