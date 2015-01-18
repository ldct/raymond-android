package com.pennapps.raymond;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by David Liu on 17/01/2015.
 */
public class BusinessCard extends DataStructure{

    private String[] data;
    private Context context;
    public BusinessCard(String[] details, Context context){
        super("BusinessCard", new String[]{
                "Name","Email","Number","Job","Company"
        }, details, context);
        data = details;
        this.context = context;
    }

    public void addThis(){
        addData(data);
    }


    public String getName(){
        return getField(0);
    }
    public String getEmail(){
        return getField(1);
    }
    public String getNumber(){
        return getField(2);
    }
    public String getJob(){
        return getField(3);
    }
    public String getCompany(){
        return getField(4);
    }

    public ArrayList<BusinessCard> getCompleteData(){
        ArrayList<String[]> temp =  getAllData();
        ArrayList<BusinessCard> returned = new ArrayList<BusinessCard>();
        for(String[] s: temp){
            returned.add(new BusinessCard(s, context));
        }
        return returned;
    }
}
