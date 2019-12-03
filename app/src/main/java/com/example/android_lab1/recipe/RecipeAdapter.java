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



/**
 * adapter class
 * @ author peng
 * @ since 03.12.2019
 * @ version 1.0
 *
 */
public class RecipeAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private Context context;
    public RecipeAdapter(ArrayList<Recipe> recipes, Context context){
        this.recipes = recipes;
        this.context = context;
    }


    @Override
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
        View articleView;
        RelativeLayout layout = null;
        ImageView image;
        TextView title;
        TextView f2f_url;

        articleView = inflater.inflate(R.layout.item_recipe, parent, false);
        layout = articleView.findViewById(R.id.relative_root_layout);
        title = layout.findViewById(R.id.text_title);
        f2f_url = layout.findViewById(R.id.text_websit);
        image = layout.findViewById(R.id.image_main);
        title.setText(getItem(position).getTitle());
        f2f_url.setText(getItem(position).getF2f_url());
        Glide.with(parent.getContext()).asBitmap().load(recipes.get(position).getImage_url()).into(image);
        return layout;
    }
}

