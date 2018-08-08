package com.example.ayogeshwaran.capstone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.ayogeshwaran.capstone.db.PNRContract.PNREntry.PNR;
import static com.example.ayogeshwaran.capstone.db.PNRContract.PNREntry.TABLE_NAME;

class PNRDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pnrnumbers.db";
    private static final int DATABASE_VERSION = 1;

    public PNRDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PNR_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PNR + " TEXT UNIQUE NOT NULL," +

                "UNIQUE (" + PNR + ") ON CONFLICT REPLACE" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_PNR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
