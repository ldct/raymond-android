package com.pennapps.raymond;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList arraylist;
    private int layout;
    private String olddate = "";
    private String oldlocaiton ="";

    public CustomAdapter(Context context, ArrayList arrayList, int layout) {
        super(context, layout, arrayList);
        this.layout = layout;
        this.context = context;
        this.arraylist = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = new View(context);
            if (layout == R.layout.receipt) {
                Receipt current = ((ArrayList<Receipt>) arraylist).get(position);
                if (current.getDate() != null && current.getDate().equals(olddate) && current.getLocation().equals(oldlocaiton)) {
                    convertView = inflater.inflate(R.layout.receipt_deet, parent, false);
                    TextView textView = (TextView) convertView.findViewById(R.id.textView);
                    TextView textView1 = (TextView) convertView.findViewById(R.id.textView2);
                    textView.setText(current.getItem());
                    textView1.setText(current.getPrice());
                } else {
                    convertView = inflater.inflate(layout, parent, false);
                    TextView textView = (TextView) convertView.findViewById(R.id.textView);
                    TextView textView1 = (TextView) convertView.findViewById(R.id.textView2);
                    textView.setText(current.getDate());
                    textView1.setText(current.getLocation());
                    //part2
                    TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
                    TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
                    textView3.setText(current.getItem());
                    textView4.setText(current.getPrice());
                }
                olddate = current.getDate();
                oldlocaiton = current.getLocation();
            }
            else if (layout == R.layout.events) {
                Event current = ((ArrayList<Event>) arraylist).get(position);
                convertView = inflater.inflate(layout, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                TextView textView1 = (TextView) convertView.findViewById(R.id.textView2);
                TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
                TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
                TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);

                textView.setText(current.getName());
                textView1.setText(current.getDate());
                textView3.setText(current.getPrice());
                textView4.setText(current.getTime());
                textView5.setText(current.getLocation());
            }
            else if (layout == R.layout.nutrition) {
                Nutrition current = ((ArrayList<Nutrition>) arraylist).get(position);
                convertView = inflater.inflate(layout, parent, false);

                TextView food = (TextView) convertView.findViewById(R.id.food);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
                TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
                TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
                TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
                TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
                food.setText(current.getFood());
                textView.setText(current.getCalories());
                textView1.setText(current.getFat());
                textView2.setText(current.getProtein());
                textView3.setText(current.getCarbs());
                textView4.setText(current.getSodium());
                textView5.setText(current.getSugar());
            }
            else if (layout == R.layout.bcard) {
                BusinessCard current = ((ArrayList<BusinessCard>) arraylist).get(position);
                convertView = inflater.inflate(layout, parent, false);

                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView textView = (TextView) convertView.findViewById(R.id.email);
                TextView textView1 = (TextView) convertView.findViewById(R.id.PhoneNumber);
                TextView textView2 = (TextView) convertView.findViewById(R.id.company);
                name.setText(current.getName());
                textView.setText(current.getEmail());
                textView1.setText(current.getNumber());
                textView2.setText(current.getCompany()+" | " +current.getJob());
            }
        }
        return convertView;
    }



    @Override
    public int getCount() {
        return arraylist.size();
    }


    @Override
    public long getItemId(int arg0) {
        return arg0;
    }
}
