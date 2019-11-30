package com.example.android_lab1.carcharing_locations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.android_lab1.R;

import java.util.ArrayList;
import java.util.List;

public class FavourActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ListView lstFavour;
    EleCharging eleSelectLoction;
    ArrayList<EleCharging> eleFavour;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour);

        mydb = new DatabaseHelper(this);
        eleFavour = new ArrayList<>();
        lstFavour = (ListView) findViewById(R.id.lst_favour);
        lstFavour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                eleSelectLoction = eleFavour.get(i);

                Toast.makeText(FavourActivity.this,eleSelectLoction.getLocalTitle(),Toast.LENGTH_SHORT).show();

                showPopup(view);

            }
        });

        renewData();


    }

    public void showPopup(View v){

        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.favour_menu);
        popup.show();

    }

    private void renewData() {

        eleFavour = mydb.getAllFavData();
        lstFavour.setAdapter(new CarCharingListAdapter(FavourActivity.this,eleFavour,true));
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.favour_delete:
                mydb.deleteFavData(eleSelectLoction.getLocalTitle());
                renewData();
                Toast.makeText(this,"Location deleted",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favour_map:

                Uri gmmIntentUri = Uri.parse("geo:" + eleSelectLoction.getdLatitude() + "," + eleSelectLoction.getdLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);


            default:
                return  false;
        }



    }
}
