package com.example.tamz2test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class VehicleDao extends DataAccess {
    private static final String TABLE_NAME = "Vehicle";
    private static final String NAME_COL = "name";
    private static final String VALUE_COL = "value";

    public VehicleDao(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + NAME_COL + " TEXT PRIMARY KEY,"
                + VALUE_COL + " INTEGER)";

        db.execSQL(query);
    }

    public void addConfigItem(String name, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(VALUE_COL, value);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
