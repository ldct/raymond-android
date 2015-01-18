package com.pennapps.raymond;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by David Liu on 17/01/2015.
 */
public class Nutrition extends DataStructure{

    private String[] data;
    private Context context;
    public Nutrition(String[] details, Context context){
        super("Nutrition", new String[]{
                "Food","Calories","Fat","Protein","Carbs", "Sodium", "Sugar"
        }, details, context);
        data = details;
        this.context = context;
    }

    public void addThis(String token){
        addData(data, token);
    }
    public void addtoDB(String token, String filename){
        addData(token, filename);
    }


    public String getFood(){
        return getField(0);
    }
    public String getCalories(){
        return getField(1);
    }
    public String getFat(){
        return getField(2);
    }
    public String getProtein(){
        return getField(3);
    }
    public String getCarbs(){
        return getField(4);
    }
    public String getSodium(){
        return getField(5);
    }
    public String getSugar(){
        return getField(6);
    }

    public ArrayList<Nutrition> getCompleteData(){
        ArrayList<String[]> temp =  getAllData();
        ArrayList<Nutrition> returned = new ArrayList<Nutrition>();
        for(String[] s: temp){
            returned.add(new Nutrition(s, context));
        }
        return returned;
    }
}
