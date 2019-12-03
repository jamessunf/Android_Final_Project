package com.example.android_lab1.forex;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CurrencyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_CURRENCY = "CurrencyDatabaseFile";
    public static final int VERSION_NUM = 1;

    public static final String TABLE_NAME = "currency";
    public static final String COLUMN_ID = "_id";
    public static final String COL_SOURCE = "currencySource";
    public static final String COL_DESTINATION = "currencyDestination";
    public static final String COL_EXCHANGEURL = "exchangeURL";
    public CurrencyDatabaseHelper(Activity context){
        super(context, DATABASE_CURRENCY, null, VERSION_NUM);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SOURCE + " TEXT, "
                + COL_DESTINATION + " TEXT, "
                + COL_EXCHANGEURL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        Log.i("Database upgrade", "Old version:" + oldV + " newVersion:"+newV);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV){
        Log.i("Database downgrade", "Old version:" + oldV + " newVersion:"+newV);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

