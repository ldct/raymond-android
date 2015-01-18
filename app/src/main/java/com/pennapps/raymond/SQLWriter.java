package com.pennapps.raymond;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Reads and Writes SQL
 */
public class SQLWriter extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static String DATABASE_NAME = "Raymond";

    // Contacts table name
    private static String TABLE_NAME;

    private String[] titles;

    /**
     * Creates a SQL Database Writer/Reader with the given set of data
     * @param context
     * @param datanames the column names
     * @param name the name of the database
     */
    public SQLWriter(Context context, String[] datanames, String name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = name;
        titles = datanames;
        onCreate(getWritableDatabase());

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (";
        for(String s: titles) {
            CREATE_CONTACTS_TABLE += s + " TEXT,";
            }
        db.execSQL(CREATE_CONTACTS_TABLE + "Token TEXT, Image TEXT)");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**********************************************************************
     * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
     *
     * This is an example of how to retrieve all data from a database
     * table using this class.  You should edit this method to suit your
     * needs.
     *
     * the key is automatically assigned by the database
     *
     * @return Arraylist of all the Songs (in Song)
     */

    public ArrayList<String[]> getAllContacts() {
        ArrayList<String[]> returnlist = new ArrayList<String[]>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> data = new ArrayList<String>();
                for (int i =0; i<cursor.getColumnCount(); i++){
                    data.add(cursor.getString(i));
                }
                // Adding contact to list
                returnlist.add(new String[returnlist.size()]);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        db.close();
        return returnlist;
    }


    // Update new contact
    public void updateData(String[] data, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT Token FROM " + TABLE_NAME + "WHERE Token = ?";


        ContentValues values = new ContentValues();
        int i=0;
                for(String s: titles ){
                    values.put(s, data[i]); // Contact Name
                    i++;
                }

        // Inserting Row
        db.update(TABLE_NAME, values, selectQuery, new String[]{token});
        db.close(); // Closing database connection
    }
    // Adding new contact
    public void addData(String token, String file) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Token", token); // Contact Name
        values.put("Image", file); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
}
