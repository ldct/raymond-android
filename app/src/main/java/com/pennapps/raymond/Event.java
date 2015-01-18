package com.pennapps.raymond;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by David Liu on 17/01/2015.
 */
public class Event extends DataStructure{

    private String[] data;
    private Context context;
    public Event(String[] details, Context context){
        super("Event", new String[]{
                "Date","Time","Name","Location","Price"
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
    public String getName(){
        return getField(2);
    }
    public String getLocation(){
        return getField(3);
    }
    public String getPrice(){
        return getField(4);
    }

    public ArrayList<Event> getCompleteData(){
        ArrayList<String[]> temp =  getAllData();
        ArrayList<Event> returned = new ArrayList<Event>();
        for(String[] s: temp){
            returned.add(new Event(s, context));
        }
        return returned;
    }
}
