package com.example.android_lab1.forex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_lab1.R;

public class FavListFragActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_currency_list);


        Bundle dataToPass = getIntent().getExtras();

        ExchangeFragment exchangeFragment = new ExchangeFragment();
        exchangeFragment.setArguments( dataToPass ); //pass data to the the fragment
        exchangeFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, exchangeFragment)
                .addToBackStack("AnyName")
                .commit();

    }
}
