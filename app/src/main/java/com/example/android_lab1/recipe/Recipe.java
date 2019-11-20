package com.example.android_lab1.recipe;

import android.os.Bundle;

@SuppressWarnings("UnusedReturnValue")

public class Recipe {
    public static final String PUBLISHER = "publisher";
    public static final String F2F_URL = "f2f_url";
    public static final String TITLE = "title";
    public static final String SOURCE_URL = "source_url";
    public static final String RECIPE_ID = "recipe_id";
    public static final String IMAGE_URL = "image_url";
    public static final String SOCIAL_RANK = "social_rank";
    public static final String PUBLISHER_URL = "publisher_url";
    public static final String SAVED = "saved";
    public static final String ID = "_id";


    private String publisher;
    private String f2f_url;
    private String title;
    private String source_url;
    private String recipe_id;
    private String image_url;
    private String social_rank;
    private String publisher_url;

    private boolean saved;
    private long _id;

    public Recipe(){
        this("Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown",  "Unknown", false, 1);
    }
    public Recipe(String publisher, String f2f_url, String title, String source_url
                , String recipe_id, String image_url, String social_rank,
                  String publisher_url, boolean saved, long _id){

        setPublisher(publisher);
        setF2f_url(f2f_url);
        setTitle( title);
        setSource_url(source_url);
        setRecipe_id(recipe_id);
        setImage_url( image_url);
        setSocial_rank( social_rank);
        setPublisher_url( publisher_url);
        setSaved(saved);
        setID(_id);
    }

    public Recipe(Bundle data){
        this(
                data.getString(PUBLISHER),
                data.getString(F2F_URL),
                data.getString(TITLE),

                data.getString(SOURCE_URL),
                data.getString(RECIPE_ID),
                data.getString(SOCIAL_RANK),
                data.getString(IMAGE_URL),
                data.getString(PUBLISHER_URL),
                data.getBoolean(SAVED),
                data.getLong(ID)
        );
    }
    public String getPublisher(){
        return publisher;
    }
    private Recipe setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getF2f_url(){
        return f2f_url;
    }
    private Recipe setF2f_url(String f2f_url) {
        this.f2f_url = f2f_url;
        return this;
    }

    public String getTitle(){
        return title;
    }
    private Recipe setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getSource_url(){
        return source_url;
    }
    private Recipe setSource_url(String source_url) {
        this.source_url = source_url;
        return this;
    }
    public String getRecipe_id(){
        return recipe_id;
    }
    private Recipe setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
        return this;
    }
    public String getImage_url(){
        return image_url;
    }
    private Recipe setImage_url(String image_url) {
        this.image_url = image_url;
        return this;
    }
    public String getSocial_rank(){
        return social_rank;
    }
    private Recipe setSocial_rank(String social_rank) {
        this.social_rank = social_rank;
        return this;
    }
    public String getPublisher_url(){
        return publisher_url;
    }
    private Recipe setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
        return this;
    }

    public boolean saved(){
        return saved;
    }
    public Recipe setSaved(boolean saved){
        this.saved = saved;
        return this;
    }
    public long getID(){
        return _id;
    }
    public Recipe setID(long id)  {
        this._id = id;
        return this;
    }

    public Bundle makeBundle(){

        Bundle data = new Bundle();
        data.putString(PUBLISHER, publisher);
        data.putString(F2F_URL, f2f_url);
        data.putString(TITLE, title);
        data.putString(SOURCE_URL, source_url);
        data.putString(RECIPE_ID, recipe_id);
        data.putString(IMAGE_URL, image_url);
        data.putString(SOCIAL_RANK, social_rank);

        data.putString(PUBLISHER_URL, publisher_url);
        data.putBoolean(SAVED, saved);
        data.putLong(ID, _id);
        return data;
    }
}




