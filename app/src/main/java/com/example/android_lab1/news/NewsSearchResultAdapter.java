package com.example.android_lab1.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.android_lab1.R;
import java.util.List;

/**
 * It's the listview adapter which contains searched article snippets and url's
 */
public class NewsSearchResultAdapter extends BaseAdapter {
    private List<NewsSearchResultModel> searchResults;
    private Context context;
    private LayoutInflater inflater;

    public NewsSearchResultAdapter(List<NewsSearchResultModel> messageModels, Context context) {
        this.searchResults = messageModels;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.activity_news_searchresult_listview, null);

        ((TextView)view.findViewById(R.id.news_searchresult_title))
                .setText(searchResults.get(position).getTitle());
       ((TextView)view.findViewById(R.id.news_searchresult_description))
               .setText(searchResults.get(position).getDescription());

     ((TextView)view.findViewById(R.id.news_searchresult_url))
            .setText(searchResults.get(position).getUrl());

        return view;
    }
}
