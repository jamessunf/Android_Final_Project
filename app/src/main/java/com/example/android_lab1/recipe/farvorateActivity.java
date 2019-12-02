package com.example.android_lab1.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.android_lab1.R;
import java.util.ArrayList;

//import android.widget.Toolbar;

public class farvorateActivity extends AppCompatActivity {
    public static final int VIEW_ARTICLE = 109;
    private ListView list;
    private farvorateAdapter adapter;
    private ArrayList<Recipe> recipes;
    private DBManager dbm;
    private boolean tablet;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favorate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.recipe_actionbar_titles);
        setSupportActionBar(toolbar);
       //setActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        dbm = DBManager.getInstance(this);
        recipes = dbm.getRecipes(RecipeDBO.FAVOURITE_TABLE);
        tablet = findViewById(R.id.fragmentLocation) != null;


        for(Recipe recipe : recipes){
            Log.e("FavorateActivity", recipe.getTitle());
        }
        adapter = new farvorateAdapter(recipes, this);
        list = findViewById(R.id.list_recipes);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this::onItemClick);

       // SwipeRefreshLayout refresher = findViewById(R.id.refresh_layout);
        //refresher.setOnRefreshListener(() -> {
           // adapter.notifyDataSetChanged();
         //   refresher.setRefreshing(false);
      //  });

       adapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Bundle data = new Bundle();
        Recipe recipe = (Recipe)parent.getItemAtPosition(position);
        data.putString(Recipe.TITLE,recipe.getTitle());
        data.putString(Recipe.IMAGE_URL,recipe.getImage_url());

        if(tablet){
            RecipeSingleFragment fragment = new RecipeSingleFragment();
            fragment.setArguments(data);
            fragment.setTablet(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, fragment)
                    .addToBackStack(null)
                    .commit();


        }else {
            Intent intent = new Intent(farvorateActivity.this, RecipeSingleActivity.class);
             data = recipes.get(position).makeBundle();
            data.putInt("position", position);
            intent.putExtras(data);
            startActivityForResult(intent, VIEW_ARTICLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
//****************************************************************
        super.onActivityResult(requestCode, resultCode, data);
        //*********************************************************
        if (requestCode == VIEW_ARTICLE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                boolean isSaved = data.getBooleanExtra(Recipe.SAVED, false);
                if (!isSaved) {
                    dbm.deleteRecipe(RecipeDBO.FAVOURITE_TABLE, recipes.get(position).getID());
                    recipes.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            default:
                finish();
                break;
        }
        return true;
    }
}
