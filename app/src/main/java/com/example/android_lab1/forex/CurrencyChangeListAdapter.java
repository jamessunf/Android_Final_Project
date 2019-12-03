package com.example.android_lab1.forex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.android_lab1.R;

import java.util.ArrayList;


public class CurrencyChangeListAdapter extends BaseAdapter {
    private ArrayList<ExchangeData> dataList;
    private LayoutInflater mInflater;

    public CurrencyChangeListAdapter(Context mContext, ArrayList<ExchangeData> dataList) {
        this.dataList = dataList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public ExchangeData getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = mInflater.inflate(R.layout.item_currency_data_list, null);;
        ViewHolderExchange holder;
        ExchangeData exchangeData = getItem(i);
        holder = new ViewHolderExchange();
        if(getCount() != 0){

            view.setTag(holder);
            holder.from =view.findViewById(R.id.currency_from);
            holder.to = view.findViewById(R.id.currency_to);
            holder.from.setText(exchangeData.src);
            holder.to.setText(exchangeData.des);
        }

        return view;
    }
    class ViewHolderExchange {
        TextView from;
        TextView to;
    }
}
