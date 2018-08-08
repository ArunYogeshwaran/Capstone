package com.example.ayogeshwaran.capstone.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class PNRContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.ayogeshwaran.railstatus";

    // The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    public static final String PATH_PNRNUMBERS = "pnr";


    public static final class PNREntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PNRNUMBERS).build();

        public static final String TABLE_NAME = "pnrnumbers";
        public static final String PNR = "pnr";
    }
}
