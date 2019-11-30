package com.example.android_lab1.carcharing_locations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android_lab1.R;

public class EmptyActivity extends AppCompatActivity {

    private FristFragment fristFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_empty);

        fristFragment = new FristFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,fristFragment,"first_frag").commitAllowingStateLoss();

    }
}
