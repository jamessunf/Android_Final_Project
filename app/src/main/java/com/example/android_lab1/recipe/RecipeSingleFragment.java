package com.example.android_lab1.recipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.android_lab1.R;

//import android.widget.Toast;
//import android.widget.Toolbar;


@SuppressWarnings("FieldCanBeLocal")
public class RecipeSingleFragment extends Fragment {

    private View frameView;
    private Bundle data;
    private boolean isTablet;
    private boolean saved;
    private int position;
    private ImageView imageBox;
    private TextView titleBox;
    private TextView f2f_url;
    private TextView social_rank;
    private Recipe recipe;
    private DBManager dbm;

    public void onCreate(  @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments();
        recipe = new Recipe(data);
        saved = recipe.saved();
        dbm = DBManager.getInstance(getActivity());
        position = data.getInt("position");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frameView = inflater.inflate(R.layout.activity_recipe_fragment, container, false);
        imageBox = frameView.findViewById(R.id.image_main);
        titleBox = frameView.findViewById(R.id.text_title);
        f2f_url = frameView.findViewById(R.id.text_websit);
        social_rank = frameView.findViewById(R.id.text_content);
       Toolbar toolbar = frameView.findViewById(R.id.toolbar);

        //getActivity().setActionBar(toolbar);
        // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity)  getActivity()).setSupportActionBar(toolbar);

       ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).asBitmap().load(recipe.getSocial_rank()).into(imageBox);
        titleBox.setText(recipe.getTitle());
        // f2f_url.setText(recipe.getF2f_url());
        //  social_rank.setText(recipe.getSocial_rank());
        toolbar.setTitle(R.string.activity_name);
        setHasOptionsMenu(true);
        return frameView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_link_actionbar, menu);
        Log.e("RecipeSingleFragment", "Boolean value is " + (saved?"true":"false"));
        if(saved){
            menu.findItem(R.id.menu_favourite).setIcon(R.drawable.ic_menu_saved);
            menu.findItem(R.id.menu_favourite).setTitle(R.string.menu_saved);
        } else {
            menu.findItem(R.id.menu_favourite).setIcon(R.drawable.ic_menu_unsaved);
            menu.findItem(R.id.menu_favourite).setTitle(R.string.menu_unsaved);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_favourite:
                if(saved){
                    saved = false;
                    recipe.setSaved(saved);
                    item.setIcon(R.drawable.ic_menu_unsaved);
                    item.setTitle(R.string.menu_saved);
                    Toast.makeText(getActivity(), R.string.recipe_unsaved_message, Toast.LENGTH_SHORT).show();
                    Log.i("Favourite button", "Article Unfavourited");
                    dbm.deleteRecipe(RecipeDBO.FAVOURITE_TABLE, recipe.getID());

                } else {
                    saved = true;
                    recipe.setSaved(saved);
                    item.setIcon(R.drawable.ic_menu_saved);
                    item.setTitle(R.string.menu_unsaved);
                    Toast.makeText(getActivity(), R.string.recipe_saved_message, Toast.LENGTH_SHORT).show();
                    Log.i("Favourite button", "Article Favourited");
                    dbm.insertRecipe(recipe, RecipeDBO.FAVOURITE_TABLE);
                }
                break;

            case R.id.menu_link:
                goToSite(item.getActionView());
                break;

            default:
                Intent resultData = new Intent();
                resultData.putExtra("position", position);
                resultData.putExtra(Recipe.SAVED, saved);
                getActivity().setResult(Activity.RESULT_OK, resultData);
              //  Log.e("RecipeSingleFragment", "Article is "+ (saved?"saved":"unsaved"));
                getActivity().finish();
                break;
        }

        return true;
    }

    public void setTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    public void goToSite(View v){


        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(recipe.getF2f_url()));
        startActivity(webIntent);
    }
}


