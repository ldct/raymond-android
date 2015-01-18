package com.pennapps.raymond;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList arraylist;
    private int layout;

    public CustomAdapter(Context context, ArrayList arrayList, int layout) {
        super(context, layout, arrayList);
        this.layout = layout;
        this.context = context;
        this.arraylist = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = new View(context);
        if(layout == R.layout.receipt){
        rowView = inflater.inflate(layout, parent, false);
            Receipt current = ((ArrayList<Receipt>)arraylist).get(position);
            TextView textView = (TextView) rowView.findViewById(R.id.textView);
            TextView textView1 = (TextView) rowView.findViewById(R.id.textView2);
            textView.setText(current.getDate());
            textView1.setText(current.getLocation());
        }

        return rowView;
    }
}
