package com.example.android_lab1.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

/**
 * DBManager class handles  SQLite operations
 * @ author peng
 * @ since 03.12.2019
 * @ version 1.0
 *
 */

public class DBManager {
    private RecipeDBO dbo;
    private SQLiteDatabase db;
    private static DBManager instance = null;
    private static final int PUBLISHER_INDEX = 0;
    private static final int F2F_URL_INDEX = 1;
    private static final int TITLE_INDEX = 2;
    private static final int SOURCE_URL_INDEX = 3;
    private static final int RECIPE_ID_INDEX = 4;
    private static final int IMAGE_URL_INDEX = 5;
    private static final int SOCIAL_RANK_INDEX = 6;
    private static final int PUBLISHER_URL_INDEX = 7;
    private static final int SAVED_INDEX = 8;
    private static final int ID_INDEX = 9;

    /**
     *
     * @param context
     */
    private DBManager(Context context){
        dbo = new RecipeDBO(context, RecipeDBO.DB_NAME, null, RecipeDBO.DB_VERSION);
        db = dbo.getWritableDatabase();
    }

    /**
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context){
        if(instance == null){
            instance = new DBManager(context);
        }
        return instance;
    }

    /**
     *
     * @param recipe
     * @param table
     * @return
     */
    public long insertRecipe(Recipe recipe, String table){
        ContentValues cv = contentValues(recipe);
        return db.insert(table, null, cv);
    }

    /**
     *
     * @param table
     * @param id
     */
    public void deleteRecipe(String table, long id){
        db.delete(table,  RecipeDBO.COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     *
     * @param recipe
     * @return
     */
    private ContentValues contentValues(Recipe recipe){
        ContentValues cv = new ContentValues();
        cv.put(RecipeDBO.COL_PUBLISHER, recipe.getPublisher());
        cv.put(RecipeDBO.COL_F2F_URL, recipe.getF2f_url());
        cv.put(RecipeDBO.COL_TITLE, recipe.getTitle());
        cv.put(RecipeDBO.COL_SOURCE_URL, recipe.getSource_url());
        cv.put(RecipeDBO.COL_RECIPE_ID, recipe.getRecipe_id());
        cv.put(RecipeDBO.COL_IMAGE_URL, recipe.getImage_url());
        cv.put(RecipeDBO.COL_SOCIAL_RANK, recipe.getSocial_rank());
        cv.put(RecipeDBO.COL_PUBLISHER_URL, recipe.getPublisher_url());
        cv.put(RecipeDBO.COL_SAVED, recipe.saved());
        cv.put(RecipeDBO.COL_ID, recipe.getID());
        return cv;
    }

    public ArrayList<Recipe> getRecipes(String table){
        Cursor result = db.rawQuery("SELECT * FROM "+ table, null);
        ArrayList<Recipe> recipes;
        recipes = new ArrayList<>(result.getCount());
        String publisher;
        String f2f_url;
        String title;
        String source_url;
        String recipe_id;
        String image_url;
        String social_rank;
        String publisher_url;
        boolean saved=false;;
        while (result.moveToNext()) {
            publisher = result.getString(PUBLISHER_INDEX);
            f2f_url = result.getString(F2F_URL_INDEX);
            title = result.getString(TITLE_INDEX);
            source_url= result.getString(SOURCE_URL_INDEX);
            recipe_id = result.getString(RECIPE_ID_INDEX);
            image_url= result.getString(IMAGE_URL_INDEX);
            social_rank = result.getString(SOCIAL_RANK_INDEX);
            publisher_url= result.getString(PUBLISHER_URL_INDEX);
            if(result.getInt(SAVED_INDEX) == 1)
                saved = true;
            else if(result.getInt(SAVED_INDEX) == 0)
                saved = false;
            long id = result.getInt(ID_INDEX);
            recipes.add(new Recipe(publisher,f2f_url,title,source_url,recipe_id,image_url,
                    social_rank,  publisher_url, saved, id));
        }
        return recipes;
    }

    /**
     *
     * @param recipe
     */
    public void setSaved(Recipe recipe){
        ContentValues cv = new ContentValues();
        cv.put(RecipeDBO.COL_SAVED, recipe.saved()?1:0);
        db.update(RecipeDBO.RECIPE_TABLE, cv, RecipeDBO.COL_ID + " = ?", new String[]{String.valueOf(recipe.getID())});
    }

    public void recreateRecipesTable(){
        dbo.dropTable(db, RecipeDBO.RECIPE_TABLE);
        dbo.createTable(db, RecipeDBO.RECIPE_TABLE);
    }

    public RecipeDBO getDBO(){
        return dbo;
    }
    public SQLiteDatabase getDB(){
        return db;
    }
}


