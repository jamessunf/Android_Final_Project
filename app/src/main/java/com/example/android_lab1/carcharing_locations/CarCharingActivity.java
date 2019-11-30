package com.example.android_lab1.carcharing_locations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android_lab1.R;
import com.example.android_lab1.forex.forexActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static android.widget.Toast.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.*;

public class CarCharingActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

//delete

    DatabaseHelper mydb;
    ArrayList<EleCharging>  eleHistry = new ArrayList<>();

    EleCharging eleSelectLoction;


    Button btnFind,btnNew;
    ListView lstResults;
    EditText edtLat,edtLon;
    ProgressBar loading_locations;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_charing);

        Toolbar toolbar =(Toolbar) findViewById(R.id.car_toolbar);
        toolbar.setTitle("Find Car Station");
        setSupportActionBar(toolbar);



        btnFind = (Button) findViewById(R.id.btn_find);
        btnNew = (Button) findViewById(R.id.btn_new) ;
        lstResults = (ListView) findViewById(R.id.lst_results);
        edtLat =(EditText) findViewById(R.id.edt_lat);
        edtLon = (EditText) findViewById(R.id.edt_lon);
        loading_locations = (ProgressBar) findViewById(R.id.loading_locations);



        mydb = new DatabaseHelper(this);

        renewData();
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickNew();
            }
        });

        lstResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                eleSelectLoction = eleHistry.get(i);

                Toast.makeText(CarCharingActivity.this,eleSelectLoction.getLocalTitle(),Toast.LENGTH_SHORT).show();

                showPopup(view);

            }
        });



        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtLat.getText().toString().trim().length() != 0 && edtLon.getText().toString().trim().length() != 0){

                    Snackbar.make(view,"System is finding 10 locations near you!",Snackbar.LENGTH_LONG).show();

                    String dLat = edtLat.getText().toString();
                    String dLon = edtLon.getText().toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("input_location",MODE_PRIVATE);
                    SharedPreferences.Editor myInput = sharedPreferences.edit();
                    myInput.putString("lat",dLat);
                    myInput.putString("lon",dLon);
                    myInput.commit();


                    loading_locations.setVisibility(View.VISIBLE);

                    String[] str = {"https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=" + dLat + "&longitude=" + dLon + "&maxresults=10"};
                    Log.i("url:",str[0]);
                    new HttpUtil().execute(str);



                }else{

                    Toast.makeText(CarCharingActivity.this, "Please input your location's latitude & longitude.", Toast.LENGTH_SHORT).show();



                }

            }
        });
    }

    private void clickNew() {

        edtLat.setText("");
        edtLon.setText("");

        mydb.deleteAll();
        eleHistry = mydb.getAllData();
        lstResults.setAdapter(new CarCharingListAdapter(CarCharingActivity.this,eleHistry,false));

    }

    private void renewData() {

        eleHistry = mydb.getAllData();
        lstResults.setAdapter(new CarCharingListAdapter(CarCharingActivity.this,eleHistry,false));
        readPreferences();
    }

    private void readPreferences(){
        SharedPreferences sh = getSharedPreferences("input_location",MODE_PRIVATE);
        edtLat.setText(sh.getString("lat",""));
        edtLon.setText(sh.getString("lon",""));

    }

//**********************Menu**********************************************
//**********************Menu**********************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.car_menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_new:
                clickNew();
                Toast.makeText(this,"New Search",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(CarCharingActivity.this);

                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to quit?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_help:

                Intent intent = new Intent(CarCharingActivity.this,EmptyActivity.class);

                intent.putExtra("is_main_help","y");
                startActivity(intent);
                return true;

            case R.id.item_Favourites: case R.id.item_fav:

                //Toast.makeText(this,"Favourites",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(CarCharingActivity.this,FavourActivity.class);
                startActivity(intent1);

               // Snackbar snackbar = Snackbar.make(CarCharingActivity,"Add to favourites.", Snackbar.LENGTH_LONG);
               // snackbar.show();

                return true;


        }

        return super.onOptionsItemSelected(item);
    }



    //******************** Pop up menu ********************

    public void showPopup(View v){

        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.popup_map:

                Uri gmmIntentUri = Uri.parse("geo:" + eleSelectLoction.getdLatitude() + "," + eleSelectLoction.getdLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);

                return true;
            case R.id.popup_save:
                boolean isSave = mydb.insertFov(eleSelectLoction);
                if(isSave) {
                    Toast.makeText(this, eleSelectLoction.getLocalTitle() + " is saved to favourites.", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(this, eleSelectLoction.getLocalTitle() + " can not save to favourites.", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.popup_delete:
                mydb.deleteData(eleSelectLoction.getLocalTitle());
                renewData();
                Toast.makeText(this,"Delete Location" + eleSelectLoction.getLocalTitle(),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.popup_dec:


                AlertDialog.Builder builder = new AlertDialog.Builder(CarCharingActivity.this);

                builder.setTitle("Location Detail:");
                builder.setMessage("Location Title:" + eleSelectLoction.getLocalTitle() + "Address:" + eleSelectLoction.getAddr() + "Location Latitude:" +
                                    eleSelectLoction.getdLatitude() + "Location Longitude" + eleSelectLoction.getdLongitude() + "Location phone number:" +
                                    eleSelectLoction.getPhoneNumber());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
              builder.show();

                return true;
            default:
                return  false;

        }
    }

//************************************Async Task*********************************************************************************
//************************************Async Task*********************************************************************************


    private class HttpUtil extends AsyncTask<String,Integer,ArrayList<EleCharging>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_locations.setProgress(10);
        }

        @Override
        protected ArrayList<EleCharging> doInBackground(String... strings) {

           return readJson(strings[0]);


        }

        @Override
        protected void onPostExecute(final ArrayList<EleCharging> eleChargings) {

            saveHistry(eleChargings);
            renewData();






        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            loading_locations.setProgress(values[0]);
        }

        //************JSON*****************
        private ArrayList<EleCharging> readJson(String url){

           // EleCharging eleCharging = null;
            ArrayList<EleCharging> eleChargings = new ArrayList<>();

            try {
                URL jsonUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) jsonUrl.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                publishProgress(20);

                //Set up the JSON object parser:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                publishProgress(30);

                String line = null;
               // eleCharging = new EleCharging();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                publishProgress(40);


                JSONArray root = new JSONArray(result);


                for(int i=0;i<root.length();i++){

                    JSONObject jsonObject = root.getJSONObject(i);
                        JSONObject addressInfo = jsonObject.getJSONObject("AddressInfo");

                       eleChargings.add(new EleCharging(addressInfo.getString("Title"),
                                                        addressInfo.getString("AddressLine1"),
                                                        Double.toString(addressInfo.getDouble("Latitude")),
                                                        Double.toString(addressInfo.getDouble("Longitude")),
                                                        addressInfo.getString("ContactTelephone1")));

                }
                publishProgress(50);

                for(int i=0;i<eleChargings.size();i++){
                    Log.i("title:",eleChargings.get(i).getLocalTitle());
                    Log.i("title:",eleChargings.get(i).getAddr());
                  //  Log.i("Lon:",Double.toString(eleChargings.get(i).getdLongitude()));
                  //  Log.i("Lat:",Double.toString(eleChargings.get(i).getdLatitude()));
                    Log.i("Phone:",eleChargings.get(i).getPhoneNumber());

                }
                publishProgress(100);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

                return eleChargings;
        }


    }

    private void saveHistry(ArrayList<EleCharging> arrEle) {

        mydb.deleteAll();


        Iterator<EleCharging> iterator = arrEle.iterator();

        while (iterator.hasNext()){

            saveData(iterator.next());
        }

    }






    private void saveData(EleCharging ele){

        boolean isSave = mydb.insertHistry(ele);
        if(isSave) {
            Toast.makeText(this, ele.getLocalTitle() + " is found.", Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(this, ele.getLocalTitle() + "can not save to database.", Toast.LENGTH_SHORT).show();
        }

    }


}
