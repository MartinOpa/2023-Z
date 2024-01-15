package com.example.tamz2test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public abstract class DataAccess extends SQLiteOpenHelper {
    private static final String DB_NAME = "Carfaux";
    private static final int DB_VERSION = 1;
    public DataAccess(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
