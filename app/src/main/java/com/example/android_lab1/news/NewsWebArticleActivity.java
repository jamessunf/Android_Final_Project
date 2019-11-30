package com.example.android_lab1.news;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab1.R;
import com.google.android.material.snackbar.Snackbar;

//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;

/**
 * This activity is for phone use.
 * When the use clicks a search result, this activity is created with the search result's
 * content and url being displayed. When the user clicks the url, the web page is loaded.
 */
public class NewsWebArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_article);

        //display article title
        TextView titleTextView = findViewById(R.id.news_article_title);
        titleTextView.setText(getIntent().getStringExtra(NewsMainActivity.EXTRA_TITLE));

        //display article description
        TextView descriptionTextView = findViewById(R.id.news_article_description);
        descriptionTextView.setText(getIntent().getStringExtra(NewsMainActivity.EXTRA_DESCRIPTION));

        //display article url
        TextView urlTextView = findViewById(R.id.news_article_url);
        urlTextView.setText(getIntent().getStringExtra(NewsMainActivity.EXTRA_URL));

        //when url textview is clicked, the web page is loaded in the webview
        urlTextView.setOnClickListener(v->{
            ((WebView)findViewById(R.id.news_article_web))
                    .loadUrl(getIntent().getStringExtra(NewsMainActivity.EXTRA_URL));
        });

        //when the save button is clicked, this article is saved in the database
        (findViewById(R.id.news_save_article))
                .setOnClickListener(v->{
                    NewsDBHelper db = new NewsDBHelper(NewsWebArticleActivity.this);
                    if (db.add(titleTextView.getText().toString(), descriptionTextView.getText().toString(),
                            urlTextView.getText().toString())) {
                        Snackbar.make(v, "Article saved!", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        Snackbar.make(v, "Article exists!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
    }

}
