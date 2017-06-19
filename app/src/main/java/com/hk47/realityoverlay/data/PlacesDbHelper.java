package com.hk47.realityoverlay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlacesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "places.db";
    public static final int DATABASE_VERSION = 1;

    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PLACES_TABLE =

                "CREATE TABLE " + PlacesContract.PlaceEntry.TABLE_NAME + " (" +

                        PlacesContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        PlacesContract.PlaceEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                        PlacesContract.PlaceEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +

                        PlacesContract.PlaceEntry.COLUMN_PLACE_ID + " TEXT UNIQUE NOT NULL" + ");";

        db.execSQL(SQL_CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + PlacesContract.PlaceEntry.TABLE_NAME);

        onCreate(db);
    }
}

