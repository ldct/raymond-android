package com.pennapps.raymond;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by David Liu on 17/01/2015.
 */
public class Receipt extends DataStructure{

    private String[] data;
    private Context context;
    public Receipt(String[] details, Context context){
        super("Receipt", new String[]{
                "Date","Time","Location","Item","Price"
        }, details, context);
        data = details;
        this.context = context;
    }

    public void addThis(){
        addData(data);
    }


    public String getDate(){
        return getField(0);
    }
    public String getTime(){
        return getField(1);
    }
    public String getLocation(){
        return getField(2);
    }
    public String getItem(){
        return getField(3);
    }
    public String getPrice(){
        return getField(4);
    }

    public ArrayList<Receipt> getCompleteData(){
        ArrayList<String[]> temp =  getAllData();
        ArrayList<Receipt> returned = new ArrayList<Receipt>();
        for(String[] s: temp){
            returned.add(new Receipt(s, context));
        }
        return returned;
    }
}