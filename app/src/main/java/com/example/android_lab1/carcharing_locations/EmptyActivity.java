package com.example.android_lab1.carcharing_locations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.android_lab1.R;

public class EmptyActivity extends AppCompatActivity {

    private FristFragment fristFragment;
   // private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_empty);

        fristFragment = new FristFragment();
      //  secondFragment = new SecondFragment();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("is_main_help");

       // Toast.makeText(this,"--" + str,Toast.LENGTH_SHORT).show();

       // if(str.equals("y"))

           getSupportFragmentManager().beginTransaction().add(R.id.fl_container,fristFragment,"first_frag").commitAllowingStateLoss();
      //  else
       //     getSupportFragmentManager().beginTransaction().add(R.id.fl_container,secondFragment,"second_frag").commitAllowingStateLoss();

    }
}
