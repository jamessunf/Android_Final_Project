package com.example.android_lab1.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android_lab1.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * This class is the main recipe class
 * @ author peng
 * @ since 03.2019
 * @ version 1.0
 *
 */

public class RecipeMainActivity extends AppCompatActivity {
    public static final int VIEW_ARTICLE = 102;
    private ListView recipeView;
    private RecipeAdapter adapter;
    protected ArrayList<Recipe> recipes;
    private SharedPreferences sp;
    private ProgressBar progressBar;
    private DBManager dbm;
    private boolean isTablet;
    EditText searchBox;
    Button searchButton;
    private static final String PREFERENCE_SEARCH_TERM = "searchQuery";
    private static final String API_URL ="https://www.food2fork.com/api/search?key=2737b9d77d868462a1c63e0f32a60bfc&q=";
    private static final String PREFERENCE_FILE = "preferences";
    private static final String JSON_ARRAY = "recipes";
    private static final String JSON_PUBLISHER = "publisher";
    private static final String JSON_F2F_URL = "f2f_url";
    private static final String JSON_SOURCE_URL = "source_url";
    private static final String JSON_RECIPE_ID = "recipe_id";
    private static final String JSON_SOCIAL_RANK = "social_rank";
    private static final String JSON_IMAGE_URL = "image_url";
    private static final String JSON_PUBLISHER_URL = "publisher_url";
    private static final String JSON_TITLE = "title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
       Toolbar toolbar = findViewById(R.id.toolbar);
       toolbar.setTitle(R.string.activity_name);
       setSupportActionBar(toolbar);
        //setActionBar(toolbar);
        progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setVisibility(View.INVISIBLE);
        dbm = DBManager.getInstance(this);
        recipes = dbm.getRecipes(RecipeDBO.RECIPE_TABLE);
        recipeView = findViewById(R.id.list_recipes);
        adapter = new RecipeAdapter(recipes, this);
        sp = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        isTablet = findViewById(R.id.fragmentLocation) != null;
        searchBox = findViewById(R.id.edit_search_box);
        searchBox.setText(sp.getString(PREFERENCE_SEARCH_TERM, ""));
        searchButton = findViewById(R.id.button_search);
        recipeView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recipeView.setOnItemClickListener(this::tORecipe);

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchBox.getText().toString();
            if(!sp.getString(PREFERENCE_SEARCH_TERM, "").equals(searchQuery)){
                recipes.clear();
                dbm.recreateRecipesTable();
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString(PREFERENCE_SEARCH_TERM, searchQuery);
            editor.apply();

            if(!recipes.isEmpty()){
                recipes.clear();
            }
            Recipepaser recipepaser = new Recipepaser();
            recipepaser.execute(API_URL,  searchQuery);
            adapter.notifyDataSetChanged();
        });




//        SwipeRefreshLayout refresher = findViewById(R.id.refresh_layout);
//        refresher.setOnRefreshListener(() -> {
//            adapter.notifyDataSetChanged();
//            refresher.setRefreshing(false);
//        });
    }

    private void tORecipe(AdapterView<?> parent, View view, int position, long id){

        Bundle data = new Bundle();
        Recipe recipe = (Recipe)parent.getItemAtPosition(position);
        data.putString(Recipe.TITLE,recipe.getTitle());
        data.putString(Recipe.IMAGE_URL,recipe.getImage_url());

        if(isTablet){

            RecipeSingleFragment fragment = new RecipeSingleFragment();
            fragment.setArguments(data);
            fragment.setTablet(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, fragment)
                    .addToBackStack(null)
                    .commit();


        }else {
            Intent articleIntent = new Intent(RecipeMainActivity.this, RecipeSingleActivity.class);
             data = recipes.get(position).makeBundle();
            data.putInt("position", position);
            articleIntent.putExtras(data);
            startActivityForResult(articleIntent, VIEW_ARTICLE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //**************************************************************************************
        if (requestCode == VIEW_ARTICLE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                boolean isSaved = data.getBooleanExtra(Recipe.SAVED, false);
                recipes.get(position).setSaved(isSaved);
                dbm.setSaved(recipes.get(position));
                if (!isSaved)
                    dbm.deleteRecipe(RecipeDBO.FAVOURITE_TABLE, recipes.get(position).getID());
                adapter.notifyDataSetChanged();
                Log.e("RecipeMainActivity", " updated");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_home:
                returnToMain();
                break;
            case R.id.menu_favourite:
                goToFavorate();
                break;
            case R.id.menu_help:
                appInfo();
                break;
        }
        return true;
    }

    private void returnToMain(){
        RelativeLayout layout = findViewById(R.id.relative_root_layout);
        Snackbar.make(layout, R.string.recipe_snackbar_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.recipe_snackbar_button, v -> finish())
                .show();
    }

    private void appInfo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.recipe_instructions);
        builder.setPositiveButton("Close", (dialog, which) -> {
        }).show();

    }

    private void goToFavorate(){
        Intent favourate = new Intent(RecipeMainActivity.this, farvorateActivity.class);
        startActivity(favourate);
    }


    private class Recipepaser extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String searchQuery = strings[1];
                searchQuery = URLEncoder.encode(searchQuery, "UTF-8");
                String newsURL = strings[0]  + searchQuery ;
                Log.i("URL:", newsURL);

                URL url = new URL(newsURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                JSONObject searchResults = new JSONObject(result);
                processResults(searchResults.getJSONArray(JSON_ARRAY));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }


        private void processResults(JSONArray results)  {

            JSONObject json;
            Recipe recipe;
            Bitmap image = null;

            String publisher = null;
            String f2f_url = null;
            String title = null;
            String source_url = null;
            String recipe_id = null;
            String image_url = null;
            String social_rank = null;
            String publisher_url = null;

            double total = results.length()/20d;
            int progress = 0;

            for (int i = 0; i < results.length(); i++) {
                try {
                    json = results.getJSONObject(i);
                    social_rank= json.getString(JSON_SOCIAL_RANK);
                    f2f_url = json.getString(JSON_F2F_URL);
                    publisher_url = json.getString(JSON_PUBLISHER_URL);
                    source_url = json.getString(JSON_SOURCE_URL);
                    recipe_id = json.getString(JSON_RECIPE_ID);
                    publisher = json.getString(JSON_PUBLISHER );
                    title= json.getString(JSON_TITLE);
                    image_url = json.getString(JSON_IMAGE_URL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recipe = new Recipe(publisher,f2f_url,title,source_url,recipe_id,image_url,
                        social_rank,  publisher_url,false, 1);
                recipes.add(recipe);
                recipes.get(i).setID(dbm.insertRecipe(recipe, RecipeDBO.RECIPE_TABLE));

                if(i >= total){
                    publishProgress(progress);
                    progress += 5;

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            publishProgress(100);
        }
    }
}

