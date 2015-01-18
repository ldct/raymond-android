package com.pennapps.raymond;

import android.content.Context;
import android.util.Log;

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
    protected String[] getAllFields(){
        return header;
    }

    /**
     * Get the requested field in id
     *
     * @param id Must be zero indexed
     * @return data
     */
    protected String getField(int id){
        return data[id];
    }

    protected ArrayList<String[]> getAllData(){
        return sqlWriter.getAllContacts();
    }

    protected void addData(String[] data, String token){
        sqlWriter.updateData(data, token);
    }

    protected void addData(String token, String filename){
        sqlWriter.addData(token, filename, header[0]);
    }

}
