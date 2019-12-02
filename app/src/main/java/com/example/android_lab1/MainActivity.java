package com.example.android_lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android_lab1.carcharing_locations.CarCharingActivity;
import com.example.android_lab1.carcharing_locations.EmptyActivity;
import com.example.android_lab1.forex.forexActivity;
import com.example.android_lab1.recipe.RecipeMainActivity;


public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =(Toolbar) findViewById(R.id.my_toolbar) ;
        setSupportActionBar(toolbar);

        ImageView imgCharging = (ImageView) findViewById(R.id.img_charging);
        ImageView imgForex = (ImageView) findViewById(R.id.img_forex);
        ImageView imgNews = (ImageView) findViewById(R.id.img_news);
        ImageView imgRecipe = (ImageView) findViewById(R.id.img_recipe);






        imgCharging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCharging();
            }
        });

        imgForex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForex();

            }
        });
        imgNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNews();
            }
        });
        imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipe();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                openCharging();
                return  true;
            case R.id.item2:
                openForex();
                return  true;
            case R.id.item3:
                openRecipe();
                return  true;
            case R.id.item4:
                openNews();
                return  true;
            case R.id.item_1:
                openCharging();
                return  true;
            case R.id.item_2:
                openForex();
                return  true;
            case R.id.item_3:
                openRecipe();
                return  true;
            case R.id.item_4:
                openNews();
                return  true;



        }

        return  true;
    }

    private void openHelp() {
        //Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
       // startActivity(intent);
        Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    private void openRecipe() {
        Intent intent = new Intent(this, RecipeMainActivity.class);
        startActivity(intent);
    }

    private void openNews() {
       // Intent intent = new Intent(this, newsActivity.class);
       // startActivity(intent);
    }

    private void openForex() {
        Intent intent = new Intent(this, forexActivity.class);
       startActivity(intent);
    }

    private void openCharging() {
        Intent intent = new Intent(this, CarCharingActivity.class);
        startActivity(intent);
    }
}
