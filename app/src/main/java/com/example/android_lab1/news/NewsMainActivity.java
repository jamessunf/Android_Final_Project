package com.example.android_lab1.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab1.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * The type Ny time main activity.
 */

public class NewsMainActivity extends AppCompatActivity {


    public static final String apiUrl="https://newsapi.org/v2/everything?q=";
    public static final String apiKey="&apiKey=6711e1ec609c4fb2afbb33c24838652e";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_DESCRIPTION = "description";
    //private ToolbarMenu toolItem;
    private String searchString,myUrl;

    private EditText userInput;
    private List<NewsSearchResultModel> searchResults = new ArrayList<>();
    private NewsSearchResultAdapter adapter;

    private NewsDBHelper db;

    /**
     * When the activity is created.
     * @param savedInstanceState
     */
    //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

         //phone or tablet?
       boolean isTablet = findViewById(R.id.news_fragment) != null;

        //restore cached search string, if any
        String cachedSearchString = getPreferences(Context.MODE_PRIVATE)
                .getString("NEWSSearchString", "");
        ((EditText)findViewById(R.id.news_searchText)).setText(cachedSearchString);

        //popup message to when step in
        Toast.makeText(this,"This is NEWS",Toast.LENGTH_SHORT).show();

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarm4);
       // setSupportActionBar(toolbar);

       //toolItem = new ToolbarMenu(NewsMainActivity.this);

        //create Button onClickListener
        findViewById(R.id.news_searchButtonn).setOnClickListener(v->{
            userInput = findViewById(R.id.news_searchText);
            searchString = userInput.getText().toString().toLowerCase();

            //verify if user input any word to lookup
            if (searchString != null && !searchString.isEmpty()) {
                myUrl = apiUrl+searchString+apiKey;
                new NYTimeRequest().execute(myUrl);
            } else {
                Toast.makeText(this,"Please enter search string!", Toast.LENGTH_SHORT)
                        .show();
            }

        });

        // get all article snippets from the database
        db = new NewsDBHelper(this);
        searchResults = db.getAll();

        //set listview adapter
        adapter = new NewsSearchResultAdapter(searchResults, getApplicationContext());
        ((ListView)findViewById(R.id.news_listVie))
                .setAdapter(adapter);

        //create listview onItemClickListener
        ((ListView)findViewById(R.id.news_listVie))
                .setOnItemClickListener((parent, view, pos, id)->{
                    if (isTablet) {
                        Bundle data = new Bundle();
                        data.putString(EXTRA_TITLE, searchResults.get(pos).getTitle());
                        data.putString(EXTRA_URL, searchResults.get(pos).getUrl());
                        data.putString(EXTRA_DESCRIPTION, searchResults.get(pos).getDescription());
                        NewsWebArticleFragment fragment = new NewsWebArticleFragment();
                        fragment.setArguments(data);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.news_fragment, fragment)
                                .addToBackStack("AnyName")
                                .commit();
                    } else {
                        Intent intent = new Intent(this, NewsWebArticleActivity.class);
                        intent.putExtra(EXTRA_TITLE, searchResults.get(pos).getTitle());
                        intent.putExtra(EXTRA_URL, searchResults.get(pos).getUrl());
                        intent.putExtra(EXTRA_DESCRIPTION, searchResults.get(pos).getDescription());
                        startActivity(intent);
                    }
                });

        //create listview onItemLongClickListener
        ListView listview = findViewById(R.id.news_listVie);
        listview.setOnItemLongClickListener((parent, view, pos, id)->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Do you want to delete the article title?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (d, i) -> {
                                db.delete(searchResults.get(pos));
                                searchResults.remove(pos);
                                adapter.notifyDataSetChanged();
                                Snackbar.make(view, "Article title delete!", Snackbar.LENGTH_LONG)
                                        .show();
                            })
                            .setNegativeButton("No", (d, i) -> {
                                //do nothing
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete");
                    alert.show();
                    return true;
                });
    }

    /**
     * Read database to get all saved articles and set the list view adapter
     */
    @Override
    protected void onResume() {
        super.onResume();

        // get all article titles from the database
        db = new NewsDBHelper(this);
        searchResults = db.getAll();

        //set listview adapter
        adapter = new NewsSearchResultAdapter(searchResults, getApplicationContext());
        ((ListView)findViewById(R.id.news_listVie))
                .setAdapter(adapter);
    }

    /**
     * Store the last search string
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("NEWSSearchString",
                ((EditText)findViewById(R.id.news_searchText)).getText().toString());
        editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(4).setVisible(false);
//        toolItem.setHelpTitle(getString(R.string.m4_help_title));
//        toolItem.setHelpMessage(getString(R.string.m4_help_message));
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
//        Intent intent = toolItem.onToolbarItemSelected(item);
//        if (intent != null) {
//            startActivity(intent);
//            NewsMainActivity.this.finish();
//        }
        return super.onOptionsItemSelected(item);
    }




        public class NYTimeRequest extends AsyncTask<String,Integer,String> {


            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    URL url = new URL(myUrl);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();


                    // read the output from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }

                    result = stringBuilder.toString();

                }
                catch (Exception e) {
                    e.printStackTrace();

                }


                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("onPostExcute:",result);

                try {

                    JSONObject jo= new JSONObject(result);
//                    JSONArray joArray = jo.getJSONObject("response").getJSONArray("docs");
//                  JSONArray joArray = jo.getJSONObject("articles").getJSONArray("source");
                    JSONArray joArray = jo.getJSONArray("articles");
                    searchResults.clear();

                    for (int i = 0; i < joArray.length(); i++) {
                        NewsSearchResultModel model = new NewsSearchResultModel();
                        model.setTitle(joArray.getJSONObject(i).getString("title"));
                        model.setUrl(joArray.getJSONObject(i).getString("url"));
                        model.setDescription(joArray.getJSONObject(i).getString("description"));
                        searchResults.add(model);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                super.onProgressUpdate(values);

        }
    }
}
