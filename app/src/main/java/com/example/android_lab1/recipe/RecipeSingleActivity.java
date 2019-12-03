package com.example.android_lab1.recipe;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_lab1.R;

/**
 * This class hold an empty fragment
 * @ author peng
 * @ since 03.12.2019
 * @ version 1.0
 *
 */
public class RecipeSingleActivity extends AppCompatActivity {

    boolean saved;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_fram);
        Bundle data = getIntent().getExtras();
        saved = data.getBoolean(Recipe.SAVED);

        RecipeSingleFragment fragment = new RecipeSingleFragment();
        fragment.setArguments(data);
        fragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, fragment)
                .commit();
    }

}

