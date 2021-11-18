package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myHelper extends SQLiteOpenHelper {

    private static final String dbName = "LocationDB"; //Setting database name and version
    private static final int version = 1;

    public myHelper(Context context){
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //On creation the database is created and the columns are set with the names
        String sql = "CREATE TABLE LOCATIONS (_id INTEGER PRIMARY KEY AUTOINCREMENT, ADDRESS TEXT, LATITUDE TEXT, LONGITUDE TEXT)";
        sqLiteDatabase.execSQL(sql);
        insertData("23 Luce dr", 100.1212, -76.2133, sqLiteDatabase); //example value in database
    }

    void insertData(String Address, Double Latitude, Double Longitude, SQLiteDatabase db){ //method that is called to insert values to insert data to the database
        ContentValues locations = new ContentValues();
        locations.put("ADDRESS", Address);
        locations.put("LATITUDE", Latitude);
        locations.put("LONGITUDE", Longitude);
        db.insert("LOCATIONS", null, locations);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
