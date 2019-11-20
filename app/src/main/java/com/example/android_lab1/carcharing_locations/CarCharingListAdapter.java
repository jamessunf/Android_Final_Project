package com.example.android_lab1.carcharing_locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_lab1.R;

import java.util.ArrayList;

public class CarCharingListAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private ArrayList<EleCharging> eleChargings;


    public CarCharingListAdapter(CarCharingActivity context, ArrayList<EleCharging> p) {
        this.myContext = context;
        this.myLayoutInflater = LayoutInflater.from(context);
        this.eleChargings = p;
    }

    @Override
    public int getCount() {
        return eleChargings.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{

        public ImageView imgView;
        public TextView txtTitle, txtLocation,txtPhone;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder = null;


             convertView = myLayoutInflater.inflate(R.layout.custom_item_station,null);
             holder = new ViewHolder();
             holder.imgView = (ImageView) convertView.findViewById(R.id.img_station);
             holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
             holder.txtLocation = (TextView) convertView.findViewById(R.id.txt_location);
             holder.txtPhone = (TextView) convertView.findViewById(R.id.txt_phone);

             convertView.setTag(holder);
             holder.imgView.setImageResource(R.drawable.charging);


             holder.txtTitle.setText("Location:" + eleChargings.get(position).getLocalTitle() + "  Address:" + eleChargings.get(position).getAddr());
             holder.txtLocation.setText("Lat:" + Double.toString(eleChargings.get(position).getdLatitude()) + " Lon:" + Double.toString(eleChargings.get(position).getdLongitude()));
             holder.txtPhone.setText("Phone:" + eleChargings.get(position).getPhoneNumber());




        return convertView;
    }
}
