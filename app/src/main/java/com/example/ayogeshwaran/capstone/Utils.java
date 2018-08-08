package com.example.ayogeshwaran.capstone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.ayogeshwaran.capstone.db.PNRContract;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";
    private static final String PNR_KEY = "pnr-key";
    private static final int PNR_LENGTH = 10;

    public static String getPNRApiKey(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(PNR_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void addPNRToDb(Context context, String pnrNoToAdd) {
        Uri uri = PNRContract.PNREntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(PNRContract.PNREntry.PNR, pnrNoToAdd);
        Uri uri1 = resolver.insert(uri, values);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<String> getAllPNRFromDb(Context context) {
        List<String> pnrNumbers = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        try (Cursor data = resolver.query(PNRContract.PNREntry.CONTENT_URI,
                null, null, null, null)) {
            if (data != null && data.moveToFirst()) {
                do {
                    String pnr = data.getString(data.getColumnIndex(PNRContract.PNREntry.PNR));
                    pnrNumbers.add(pnr);
                } while (data.moveToNext());
            }
        }
        return pnrNumbers;
    }

    public static void removePNRFromDB(Context context, String pnrNo) {
        Uri uri = PNRContract.PNREntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        int rows = resolver.delete(uri, PNRContract.PNREntry.PNR + " = ? ",
                new String[]{pnrNo + ""});
        if (rows > 0) {
            Toast.makeText(context, R.string.entry_deleted,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkForValidPNR(Context context, String enteredPNR) {
        if (enteredPNR.matches("[0-9]+") && enteredPNR.length() == PNR_LENGTH) {
            return true;
        } else {
            Toast.makeText(context, R.string.enter_valid_pnr,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
