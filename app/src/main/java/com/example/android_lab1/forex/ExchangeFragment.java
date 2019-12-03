package com.example.android_lab1.forex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.android_lab1.R;

import static com.example.android_lab1.forex.FavListActivity.REALTIME_RATE;

public class ExchangeFragment extends Fragment {
    private Button delete;
    private View view;
    private TextView convertFrom, messageId, convertTo, currentRate;

    private boolean isTablet;
    private Bundle dataFromActivity;

    private long id;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.currency_fragment, container, false);
        Log.d("Dong", "Fall on the  " + 1);
        delete = view.findViewById(R.id.btn_exchange_delete);
        convertFrom = view.findViewById(R.id.tv_currency_from);
        messageId = view.findViewById(R.id.tv_message_id);
        convertTo = view.findViewById(R.id.tv_currency_to);
        currentRate = view.findViewById(R.id.tv_realtime_rate);

        dataFromActivity = getArguments();
        if (dataFromActivity != null) {
            double realtimeRate;
            id = dataFromActivity.getLong(CurrencyDatabaseHelper.COLUMN_ID);
            messageId.setText(id + "");

            convertFrom.setText(dataFromActivity.getString(CurrencyDatabaseHelper.COL_SOURCE));
            convertTo.setText(dataFromActivity.getString(CurrencyDatabaseHelper.COL_DESTINATION));
            realtimeRate = dataFromActivity.getDouble(REALTIME_RATE);
            currentRate.setText(String.format("Real time Exchange Rate is  : %.8f", realtimeRate));
        }

        delete.setOnClickListener(clk -> {
            if (isTablet) {
                FavListActivity parent = (FavListActivity) getActivity();
                if (parent != null) {
                    parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                    parent.deleteMessageId((int) id);
                }
            } else {
                FragmentActivity parent = getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(CurrencyDatabaseHelper.COLUMN_ID, dataFromActivity.getLong(CurrencyDatabaseHelper.COLUMN_ID));

                if (parent != null) {
                    parent.setResult(Activity.RESULT_OK, backToFragmentExample);
                    parent.finish();
                }
            }
        });
        return view;
    }
}