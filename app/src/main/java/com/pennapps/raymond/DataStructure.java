package com.pennapps.raymond;

import android.content.Context;

import java.util.ArrayList;

/**
 * DataStructure defines the variety of data that this app
 * is able to support
 * All types of data structures must be a sub class of DataStructure
 */
public abstract class DataStructure {
    private String[] header;
    private String[] data;
    private String title;

    private SQLWriter sqlWriter;
    public DataStructure(String databaseName, String[] fields, String[] values, Context context){
        title = databaseName;
        header = fields;
        data = values;
        sqlWriter = new SQLWriter(context, fields, databaseName);
    }

    /**
     *
     * @return the fields for this data type
     */
    public String[] getAllFields(){
        return header;
    }

    /**
     * Get the requested field in id
     *
     * @param id Must be zero indexed
     * @return data
     */
    public String getField(int id){
        return data[id];
    }

    public ArrayList<String[]> getAllData(){
        return sqlWriter.getAllContacts();
    }

    public void addData(String[] data){
        sqlWriter.addData(data);
    }

}
