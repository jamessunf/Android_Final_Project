package com.example.android_lab1.forex;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android_lab1.R;
import com.google.android.material.snackbar.Snackbar;

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

public class CurrencyExchange extends AppCompatActivity {

    private Toolbar currencyToolBar;
    public static final int RESULT_FAV = 345;
    //static ProgressBar pbProcess;
    TextView currentFromRate, currentToRate;
    TextView exchangeAmount,  convertNumber;
    TextView saveInfo;
    private SQLiteOpenHelper dbHelper;
    private ArrayList<ExchangeData> dataList = new ArrayList<>();
    int defaultIndexFrom, defaultIndexTo;
    int fromIndex,toIndex;
    private static final String FROMKEY = "from";
    private static final String TOKEY = "to";
    Spinner fromSpinner,toSpinner;

    private CurrencyChangeListAdapter adapter;

    private SharedPreferences preferences;

    final String[] currencyData = new String[]{"USD","CAD","EUR","GBP","HKD","ISK","PHP","DKK","HUF","CZK","AUD","RON","SEK","IDR","INR"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new CurrencyDatabaseHelper(this);
        adapter = new CurrencyChangeListAdapter(this, dataList);

        setContentView(R.layout.currency_exchange);
        Button bExchange = (Button) findViewById(R.id.confirm);
        Button bSave = (Button)findViewById(R.id.save_fav);

        currentFromRate = findViewById(R.id.current_from_rate);
        currentToRate = findViewById(R.id.current_to_rate);
        saveInfo = findViewById(R.id.save_info);

        currencyToolBar = findViewById(R.id.toolbarPage);
        currencyToolBar.setTitle(R.string.currency_exchange);
        setSupportActionBar(currencyToolBar);

        //init From spinner selection
        fromSpinner = (Spinner) findViewById(R.id.convert_from);
        ArrayAdapter<String> fromArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyData);
        // Specify the layout to use when the list of choices appears
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //load preference first
        preferences = getSharedPreferences("savePreference", Context.MODE_PRIVATE);

        // Tell the spinner about our adapter
        fromSpinner.setAdapter(fromArrayAdapter);
        int fromIndex = preferences.getInt(FROMKEY, -1);

        Log.d("Get" , "Value to from : "+ fromIndex);
        // Select defaultindex as the base
        fromSpinner.setSelection(fromIndex);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //init to spinner selection
        toSpinner = (Spinner) findViewById(R.id.convert_to);
        ArrayAdapter<String> toArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currencyData);

        // Specify the layout to use when the list of choices appears
        toArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Tell the spinner about our adapter
        toSpinner.setAdapter(toArrayAdapter);
        int toIndex = preferences.getInt(TOKEY, 0);

        Log.d("Get" , "Value to to : "+ toIndex);
        // Select defaultindex as the base
        toSpinner.setSelection(toIndex);

        // Create a listener whenever something is selected.
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        convertNumber = (EditText) findViewById(R.id.convert_number);

        bExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bExchange.setOnClickListener(click -> {
                    try {

                        //qurey url for exchange rate
                        new CurrencyQuery().execute();
                        savePreference();

                    } catch (NumberFormatException nfe){
                        nfe.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
        );
        //save to favorite list

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveFavoriteList();
                Snackbar.make(view,"Data save to Favorites!",Snackbar.LENGTH_LONG).show();
                Snackbar snackbar;

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_exchange_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exch_help:
                showCustomDialogHelp();
                break;
            case R.id.exch_fav:
                showFavoriteList();
                break;

            default:
                break;
        }
        return true;
    }

    // show custom dialog
    public void showCustomDialogHelp() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_help, null);
        //EditText input = view.findViewById(R.id.et_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyExchange.this);
        builder.setView(view)
                .setMessage(R.string.help_currency)
                .show();
    }

    private class CurrencyQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pbProcess.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "https://api.exchangeratesapi.io/latest";

            JSONObject json = new JSONObject();

            // Get the currency from the end user
            int iTo = toSpinner.getSelectedItemPosition();
            int iFrom = fromSpinner.getSelectedItemPosition();
            String sTo = currencyData[iTo];
            String sFrom = currencyData[iFrom];

            try {
                // start request exchange rate
                URL url1 = new URL(queryURL);
                HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
                InputStream inStream1 = urlConnection1.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream1));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine())!= null){
                    response.append(line);
                }
                //  json parse and  get response of exchange rate
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject  jsonRates = jsonObject.getJSONObject("rates");
                //all the currency is EUR based
                if(sFrom != "EUR") {
                    double fromValue = jsonRates.getDouble(sFrom);
                    json.put("from", fromValue);
                } else{
                    json.put("from", 1);
                }

                if(sTo != "EUR" ) {
                    double toValue = jsonRates.getDouble(sTo);
                    json.put("to", toValue);
                }else{
                    json.put("to", 1);
                }
                return json.toString();

            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.getStackTrace();
                ret = "IO Exception. Is the Wifi connected?";
            }
            return ret;

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pbProcess.setVisibility(View.INVISIBLE);

            try {
                JSONObject jObject = new JSONObject(s);
                double fromRate = jObject.getDouble("from");
                double toRate = jObject.getDouble("to");
                Log.d("From Rate" , "Value : "+ Double.toString(fromRate));
                Log.d("To Rate" , "Value : "+ Double.toString(toRate));
                Toast.makeText(CurrencyExchange.this,"From Rate:" + fromRate + " to " + toRate,Toast.LENGTH_SHORT).show();
                //rate is EUR based, set default if EUR
                double exRate = fromRate/toRate;
                double reverseRate = toRate/fromRate;

                //display on Rate
                // Get the currency from the end user
                int iTo = toSpinner.getSelectedItemPosition();
                int iFrom = fromSpinner.getSelectedItemPosition();
                String sTo = currencyData[iTo];
                String sFrom = currencyData[iFrom];
                currentFromRate.setText("1  "+ sTo + "  =  :      " + exRate + " " + sFrom);
                currentToRate.setText("1  "+ sFrom + "  =  :      " + reverseRate + " " + sTo);

                String numberToConvert = convertNumber.getText().toString();
                double oonverAmount ;

                oonverAmount = Double.parseDouble(numberToConvert);
                oonverAmount *= reverseRate;
                exchangeAmount = findViewById(R.id.convert_amount);
                Log.d("Amount Converted" , "value" + oonverAmount);
                Toast.makeText(CurrencyExchange.this,"Amount Converted, value:" + oonverAmount,Toast.LENGTH_SHORT).show();

                exchangeAmount.setText(String.format("Exchange Amount is  : %.8f", oonverAmount));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Query the whole database
    public ArrayList<ExchangeData> query() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<ExchangeData> messages = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + CurrencyDatabaseHelper.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            ExchangeData message = new ExchangeData(cursor.getInt(cursor.getColumnIndex(CurrencyDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.COL_SOURCE)),
                    cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.COL_DESTINATION)));
            messages.add(message);
        }
        cursor.close();
        return messages;
    }

    public boolean queryFavList(String from , String to){
        return false;
    }

    //Delete the message by ID
    public void deleteMessageId(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CurrencyDatabaseHelper.TABLE_NAME,
                CurrencyDatabaseHelper.COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});
        db.close();

        dataList = query();
    }
    public void showFavoriteList() {
        //send the current selection data to list, for save use
        Bundle dataToPass = new Bundle();
        // Get the currency from the user
        int iTo = toSpinner.getSelectedItemPosition();
        int iFrom = fromSpinner.getSelectedItemPosition();
        String sTo = currencyData[iTo];
        String sFrom = currencyData[iFrom];


        dataToPass.putString(CurrencyDatabaseHelper.COL_SOURCE,sFrom);
        dataToPass.putString(CurrencyDatabaseHelper.COL_DESTINATION,sTo);

        Intent nextActivity = new Intent(this, FavListActivity.class);
        nextActivity.putExtras(dataToPass);
        startActivityForResult(nextActivity, RESULT_FAV);
    }
    public void saveFavoriteList(){
        // Get the currency from the user
        int iTo = toSpinner.getSelectedItemPosition();
        int iFrom = fromSpinner.getSelectedItemPosition();
        String sTo = currencyData[iTo];
        String sFrom = currencyData[iFrom];

        if(false == queryFavList(sFrom,sTo)){
            insert(sFrom,sTo);
        }else{
            saveInfo.setText("Save failed, already exist!");
        }
    }
    // insert input data to sqlite
    private void insert(String from, String to) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrencyDatabaseHelper.COL_SOURCE, from);
        values.put(CurrencyDatabaseHelper.COL_DESTINATION, to);
        long id = (int) db.insert(CurrencyDatabaseHelper.TABLE_NAME, null, values);
        Log.d("Dong", "insert id is -->" + id);
        updateList(id, from, to);
    }
    // update the data and notify the listview
    private void updateList(long id, String from, String to) {
        ExchangeData exchangeData = new ExchangeData(id, from, to);
        dataList.add(exchangeData);
        adapter.notifyDataSetChanged();
        //inputText.setText("");
    }

    private void savePreference(){
        SharedPreferences.Editor editor =  preferences.edit();

        // Get the currency from the end user
        int iTo = toSpinner.getSelectedItemPosition();
        int iFrom = fromSpinner.getSelectedItemPosition();

        editor.putInt(FROMKEY, iFrom);
        editor.putInt(TOKEY, iTo);
        Log.d("Save" , "Value from : "+ iFrom);
        Log.d("Save" , "Value to : "+ iTo);
        editor.apply();
    }



}
