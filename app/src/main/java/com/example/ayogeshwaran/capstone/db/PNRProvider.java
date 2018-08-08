package com.example.ayogeshwaran.capstone.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PNRProvider extends ContentProvider {

    private PNRDbHelper pnrDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int PNR_NUMBERS = 100;
    private static final int PNR_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PNRContract.AUTHORITY;
        matcher.addURI(authority, PNRContract.PATH_PNRNUMBERS, PNR_NUMBERS);
        matcher.addURI(authority, PNRContract.PATH_PNRNUMBERS + "/#", PNR_ID);

        return matcher;
    }

    public PNRProvider() {

    }

    @Override
    public boolean onCreate() {
        pnrDbHelper = new PNRDbHelper(getContext());
        pnrDbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case PNR_NUMBERS:
                cursor = getAllPnrs(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case PNR_ID:
                cursor = getPnrWithId(uri, projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri for query: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = pnrDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PNR_NUMBERS: {
                long id = db.insertOrThrow(PNRContract.PNREntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(PNRContract.PNREntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = pnrDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (match) {
            case PNR_NUMBERS:
                rowsDeleted = db.delete(PNRContract.PNREntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PNR_ID:
                rowsDeleted = db.delete(PNRContract.PNREntry.TABLE_NAME,
                        PNRContract.PNREntry.PNR + "=" + uri.getLastPathSegment(),
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private Cursor getAllPnrs(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor pnrCursor;
        SQLiteDatabase database = pnrDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(PNRContract.PNREntry.TABLE_NAME);
        pnrCursor = builder.query(database,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return pnrCursor;
    }

    private Cursor getPnrWithId(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor restaurantCursor;
        SQLiteDatabase database = pnrDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(PNRContract.PNREntry.TABLE_NAME);
        builder.appendWhere(PNRContract.PNREntry.PNR + "=" + uri.getLastPathSegment());
        restaurantCursor = builder.query(database,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return restaurantCursor;
    }
}
