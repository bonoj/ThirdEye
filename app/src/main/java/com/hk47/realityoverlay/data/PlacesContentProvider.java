package com.hk47.realityoverlay.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PlacesContentProvider extends ContentProvider {

    public static final int PLACES = 100;
    public static final int PLACE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PlacesContract.AUTHORITY, PlacesContract.PATH_PLACES, PLACES);

        uriMatcher.addURI(PlacesContract.AUTHORITY, PlacesContract.PATH_PLACES + "/#", PLACE_WITH_ID);

        return uriMatcher;
    }


    private PlacesDbHelper mPlacesDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mPlacesDbHelper = new PlacesDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mPlacesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match) {
            case PLACES:
                cursor = db.query(PlacesContract.PlaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLACE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                cursor = db.query(PlacesContract.PlaceEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mPlacesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case PLACES:
                long id = db.insert(PlacesContract.PlaceEntry.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PlacesContract.PlaceEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mPlacesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int placesDeleted;

        switch (match) {
            case PLACES:
                placesDeleted = db.delete(PlacesContract.PlaceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PLACE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                placesDeleted = db.delete(PlacesContract.PlaceEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknwn uri: " + uri);
        }

        if (placesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return placesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}