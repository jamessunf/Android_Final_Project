package com.example.android_lab1.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RecipeDBO extends SQLiteOpenHelper {


    public static final String DB_NAME = "RecipeDB";
    public static final int DB_VERSION = 3;
    public static final String RECIPE_TABLE = "recipes";
    public static final String FAVOURITE_TABLE = "favourites";
    public static final String COL_PUBLISHER = Recipe.PUBLISHER;
    public static final String COL_F2F_URL = Recipe.F2F_URL;
    public static final String COL_TITLE = Recipe.TITLE;
    public static final String COL_SOURCE_URL = Recipe.SOURCE_URL;
    public static final String COL_RECIPE_ID = Recipe.RECIPE_ID;
    public static final String COL_IMAGE_URL = Recipe.IMAGE_URL;
    public static final String COL_SOCIAL_RANK = Recipe.SOCIAL_RANK;
    public static final String COL_PUBLISHER_URL = Recipe.PUBLISHER_URL;
    public static final String COL_SAVED = Recipe.SAVED;
    public static final String COL_ID = Recipe.ID;

    public RecipeDBO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }
//    public RecipeDBO(Activity ctx) {
//          super(ctx, DB_NAME, null, DB_VERSION);
//    }

    public void onCreate(SQLiteDatabase db) {
        createTable(db, RECIPE_TABLE);
        createTable(db, FAVOURITE_TABLE);
        Log.i("RecipeDBO", "Database was created");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RecipeDBO", "Database upgraded from version "+ oldVersion + " to version "+ newVersion);
        dropTable(db, RECIPE_TABLE);
        dropTable(db, FAVOURITE_TABLE);
        onCreate(db);
    }
    public void createTable(SQLiteDatabase db, String table){
        int saved = 0;
        if(table.equals(RECIPE_TABLE))
            saved = 0;
        else if(table.equals(FAVOURITE_TABLE))
            saved = 1;

        db.execSQL("CREATE TABLE "+ table +" (" +
                COL_PUBLISHER  +" TEXT," +
                COL_F2F_URL +" TEXT," +
                COL_TITLE +" TEXT," +
                COL_SOURCE_URL +" TEXT," +
                COL_RECIPE_ID +" TEXT," +
                COL_IMAGE_URL +" TEXT," +
                COL_SOCIAL_RANK +" TEXT," +
                COL_PUBLISHER_URL + " TEXT," +
                COL_SAVED + " INTEGER DEFAULT "+ saved + ","+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)");
    }

    public void dropTable(SQLiteDatabase db, String table){
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }
}

