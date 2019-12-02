package com.example.android_lab1.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * It's the database engine for the NY Times activity. The database can save an article with
 * it's snippet and url. Also the database provides "delete" and "clear all" functionality.
 */
public class NewsDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "NEWSArticleDB";
    private static final String DB_TABLE = "Articles_Table";


    //columns
    private static final String COL_TITLE = "Title";
     private static final String COL_DESCRIPTION = "Description";
    private static final String COL_URL = "Url";
    private static final String COL_ID = "ID";
    private static final int VERSION = 3;

    //queries
    private static final String CREATE_TABLE = "CREATE TABLE "+DB_TABLE+" ("
            +COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_TITLE +" TEXT, " + COL_DESCRIPTION + " TEXT,"
           + COL_URL +" TEXT);";

    public NewsDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    //add data
    public boolean add(NewsSearchResultModel model) {
        return add(model.getTitle(),model.getDescription(),model.getUrl());
    }

    public boolean add(String title, String url, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_URL, url);
        contentValues.put(COL_DESCRIPTION, description);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1; //if result = -1 data doesn't insert
    }

    //delete data
    public boolean delete(NewsSearchResultModel model) {
       // return delete(model.getTitle(),model.getUrl());
        return delete(model.getTitle(),model.getDescription() ,model.getUrl());
    }

    public boolean delete(String title, String url, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, COL_TITLE + " = ?", new String[] {title}) > 0;
     //   new String[] {description}) > 0;
    }

    //get all data
    public List<NewsSearchResultModel> getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<NewsSearchResultModel> models = new ArrayList<>();
        String title;
        String url;
        String description;
        while (cursor.moveToNext()) {
          models.add(new NewsSearchResultModel(cursor.getString(1),
                  cursor.getString(2),cursor.getString(3)));




//            title = cursor.getString(cursor.getColumnIndex("Title"));
//            description = cursor.getString(cursor.getColumnIndex("Description"));
//            url = cursor.getString(cursor.getColumnIndex("Url"));


        }
        cursor.close();
        return models;
    }

    //clear DB
    public void clear() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DB_TABLE, "1", null);
        db.close();
    }


}
