package com.hk47.realityoverlay.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PlacesContract {

    public static final String AUTHORITY = "com.hk47.realityoverlay";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PLACES = "places";

    public static final class PlaceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();

        public static final String TABLE_NAME = "place";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_PLACE_ID = "place_id";
    }

}
