package com.example.android_lab1.forex;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab1.R;

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

public class FavListActivity extends AppCompatActivity {

    private SQLiteOpenHelper dbHelper;
    private ListView mListView;
    private ArrayList<ExchangeData> dataList = new ArrayList<>();
    private CurrencyChangeListAdapter adapter;
    public static final int EMPTY_ACTIVITY = 345;
    public static final String REALTIME_RATE = "realtimeRate";
    private Bundle dataToPassFrag ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favlist);
        mListView = findViewById(R.id.lv_fav);
        //Button saveButton = findViewById(R.id.btn_savefav);

        dbHelper = new CurrencyDatabaseHelper(this);

        if (query() != null) {
            dataList = query();
        }
        adapter = new CurrencyChangeListAdapter(this, dataList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //set item click listern
        boolean isTablet = findViewById(R.id.fl_detail) != null;
        mListView.setOnItemClickListener((list, item, position, id) -> {
            dataToPassFrag = new Bundle();
            dataToPassFrag.putLong(CurrencyDatabaseHelper.COLUMN_ID, dataList.get(position).id);
            dataToPassFrag.putString(CurrencyDatabaseHelper.COL_SOURCE, dataList.get(position).src);
            dataToPassFrag.putString(CurrencyDatabaseHelper.COL_DESTINATION, dataList.get(position).des);
            try {
                //qurey url for exchange rate
                double queryRate = new RealtimeCurrencyQuery().execute().get();
                Log.d("Query Rate put is : ","query" + queryRate);
                dataToPassFrag.putDouble(REALTIME_RATE,queryRate);
            } catch (NumberFormatException nfe){
                nfe.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (isTablet) {
                Log.d("Dong", "go to tablet " + 1);
                ExchangeFragment eFragment = new ExchangeFragment();
                eFragment.setArguments(dataToPassFrag);
                eFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_detail, eFragment)
                        .addToBackStack("tablet")
                        .commit();
            } else {
                Intent nextActivity = new Intent(this, FavListFragActivity.class);
                nextActivity.putExtras(dataToPassFrag);
                startActivityForResult(nextActivity, EMPTY_ACTIVITY);
            }
        });
    }
    // query all of data in the sqlite
    public ArrayList<ExchangeData> query() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<ExchangeData> exchangeData = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + CurrencyDatabaseHelper.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            ExchangeData exData = new ExchangeData(cursor.getInt(cursor.getColumnIndex(CurrencyDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.COL_SOURCE)),
                    cursor.getString(cursor.getColumnIndex(CurrencyDatabaseHelper.COL_DESTINATION)));
            exchangeData.add(exData);
        }
//        printCursor(cursor);

        cursor.close();
        return exchangeData;
    }
    // insert input data to sqlite
    public void insert(String from, String to) {
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra(CurrencyDatabaseHelper.COLUMN_ID, 0);
                deleteMessageId((int) id);
            }
        }
    }


    public void deleteMessageId(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CurrencyDatabaseHelper.TABLE_NAME,
                CurrencyDatabaseHelper.COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});
        db.close();
        Log.d("Delete msg being rv: "," ID iS" + id);
        dataList = query();
        adapter = new CurrencyChangeListAdapter(this, dataList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class RealtimeCurrencyQuery extends AsyncTask<String, Integer, Double> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pbProcess.setVisibility(View.VISIBLE);
        }
        @Override
        protected Double doInBackground(String... strings) {
            Double ret = 0.0;
            String queryURL = "https://api.exchangeratesapi.io/latest";

            JSONObject json = new JSONObject();

            // Get the currency from the end user
            String sTo = dataToPassFrag.getString(CurrencyDatabaseHelper.COL_DESTINATION);
            String sFrom = dataToPassFrag.getString(CurrencyDatabaseHelper.COL_SOURCE);

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
                double fromValue = 1.0;
                double toValue = 1.0;
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject  jsonRates = jsonObject.getJSONObject("rates");

                if(sFrom.equals("EUR")) {
                    json.put("from", 1);

                }else{
                    fromValue = jsonRates.getDouble(sFrom);
                    json.put("from", fromValue);
                }

                if(sTo.equals("EUR")) {
                    json.put("to", 1);

                }else{
                    Log.d("Search to msg is : ","Currency " + sTo);
                    toValue = jsonRates.getDouble(sTo);
                    json.put("to", toValue);
                }
                double realtimeRate = fromValue/toValue;
                Log.d("Realtime Rate put is : ","query" + realtimeRate);

                return realtimeRate;

            } catch (MalformedURLException mfe) {
                mfe.printStackTrace();
                ret = 0.0;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.getStackTrace();
                ret = 0.0;
            }
            return ret;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected void onPostExecute(Double s) {
            super.onPostExecute(s);
        }
    }
}