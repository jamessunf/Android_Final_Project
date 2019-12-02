package com.example.android_lab1.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.android_lab1.R;
import java.util.ArrayList;

public class farvorateAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private Context context;

    public farvorateAdapter(ArrayList<Recipe> recipes, Context context){
        this.recipes = recipes;
        this.context = context;
    }

    public int getCount() {
        return recipes.size();
    }


    @Override
    public Recipe getItem(int position) {
        return recipes.get(position);
    }


    @Override
    public long getItemId(int position) {
        return recipes.get(position).getID();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_recipe, parent, false);
        RelativeLayout layout = view.findViewById(R.id.relative_root_layout);

        ImageView imageBox = layout.findViewById(R.id.image_main);
        TextView titleBox = layout.findViewById(R.id.text_title);
        TextView siteBox = layout.findViewById(R.id.text_websit);

        Glide.with(context).load(recipes.get(position).getSocial_rank()).into(imageBox);
        titleBox.setText(getItem(position).getTitle());
        siteBox.setText(getItem(position).getF2f_url());
        return layout;
    }
}
