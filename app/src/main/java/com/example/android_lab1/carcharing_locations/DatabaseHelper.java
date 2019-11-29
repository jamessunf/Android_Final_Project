package com.example.android_lab1.carcharing_locations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cLocation.db";
    private static final String TABLE_NAME = "location_table";
    private static final String TABLE_NAME_FOV = "fov_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TITLE";
    private static final String COL_3 = "ADDRESS";
    private static final String COL_4 = "LATITUDE";
    private static final String COL_5 = "LONGITUDE";
    private static final String COL_6 = "PHONE";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT,ADDRESS TEXT,LATITUDE TEXT,LONGITUDE TEXT,PHONE TEXT)");
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_FOV + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT,ADDRESS TEXT,LATITUDE TEXT,LONGITUDE TEXT,PHONE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertHistry(EleCharging eleCharging){

       // SQLiteDatabase db = this.getWritableDatabase();
       // return db.delete(TABLE_NAME,"ID=?",new String[]{id});

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,eleCharging.getLocalTitle());
        contentValues.put(COL_3,eleCharging.getAddr());
        contentValues.put(COL_4,eleCharging.getdLatitude());
        contentValues.put(COL_5,eleCharging.getdLongitude());
        contentValues.put(COL_6,eleCharging.getPhoneNumber());



        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1)
            return false;  //
        else
            return  true; //


    }

    public boolean insertFov(EleCharging eleCharging){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,eleCharging.getLocalTitle());
        contentValues.put(COL_3,eleCharging.getAddr());
        contentValues.put(COL_4,eleCharging.getdLatitude());
        contentValues.put(COL_5,eleCharging.getdLongitude());
        contentValues.put(COL_6,eleCharging.getPhoneNumber());



        long result = db.insert(TABLE_NAME_FOV,null,contentValues);

        if(result == -1)
            return false;  //
        else
            return  true; //


    }

    public ArrayList<EleCharging> getAllData(){

        ArrayList<EleCharging> eleChargings = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        if(res.getCount() != 0){
            eleChargings.clear();
            while (res.moveToNext()){
                eleChargings.add(new EleCharging(res.getString(res.getColumnIndex("ID")),
                        res.getString(res.getColumnIndex("TITLE")),
                        res.getString(res.getColumnIndex("ADDRESS")),
                        res.getString(res.getColumnIndex("LATITUDE")),
                        res.getString(res.getColumnIndex("LONGITUDE")),
                        res.getString(res.getColumnIndex("PHONE"))
                        ));
            }

        }
        return eleChargings;

    }

    public Integer getV(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.getVersion();
    }



    public long deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{id});


    }

}
