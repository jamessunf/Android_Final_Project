package com.example.android_lab1.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.android_lab1.R;
import com.google.android.material.snackbar.Snackbar;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * This fragment implements the onCreateView() method which displays a web page with
 * the given url from the calling activity.
 */
public class NewsWebArticleFragment extends Fragment {
    private boolean isTablet = true;
    private Bundle dataFromMainActivity;

    public NewsWebArticleFragment() {
        // Required empty public constructor
    }

    /**
     * When this fragment is created, a url is passed to it and a web page to this url
     * is loaded in the web view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_news_web_article, container, false);

        dataFromMainActivity = getArguments();
        String title = dataFromMainActivity.getString(NewsMainActivity.EXTRA_TITLE);
        String url = dataFromMainActivity.getString(NewsMainActivity.EXTRA_URL);
        String description = dataFromMainActivity.getString(NewsMainActivity.EXTRA_DESCRIPTION);


        //display article title
        TextView titleTextView = result.findViewById(R.id.news_article_title);
        titleTextView.setText(title);

        //display article url
        TextView urlTextView = result.findViewById(R.id.news_article_url);
        urlTextView.setText(url);

        //display article description
        TextView descriptionTextView = result.findViewById(R.id.news_article_description);
        descriptionTextView.setText(description);

        //when url textview is clicked, the web page is loaded in the webview
        urlTextView.setOnClickListener(v->{
            ((WebView)result.findViewById(R.id.news_article_web))
                    .loadUrl(url);
        });

        //when the save button is clicked, this article is saved in the database
        (result.findViewById(R.id.news_save_article))
                .setOnClickListener(v->{
                    NewsDBHelper db = new NewsDBHelper(getActivity());
                    if (db.add(title,description,url)) {
                        Snackbar.make(v, "Article saved!", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        Snackbar.make(v, "Article exists!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                });

        return result;
    }
}
