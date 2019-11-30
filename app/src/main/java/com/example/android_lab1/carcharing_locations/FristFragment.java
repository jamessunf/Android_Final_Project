package com.example.android_lab1.carcharing_locations;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_lab1.R;


public class FristFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frist, container, false);

        TextView txIns = (TextView) view.findViewById(R.id.txt_help_ins);
        txIns.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }



}
