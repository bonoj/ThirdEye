package com.hk47.realityoverlay.data;

import android.location.Location;

import com.hk47.realityoverlay.BuildConfig;

public class Constants {

    private Constants() {

    }

    // Details Intents
    public static final String DETAILS_INTENT_PLACE_ID = "com.hk47.realityoverlay.DETAILS_INTENT_PLACE_ID";
    public static final String DETAILS_INTENT_ICON_ID = "com.hk47.realityoverlay.DETAILS_INTENT_ICON_ID";

    // Search Intents
    public static final String REFINE_SEARCH_INTENT = "com.hk47.realityoverlay.REFINE_SEARCH_INTENT";
    public static final int SEARCH_RESULT_CODE = 1000;
    public static final String SEARCH_RETURN_INTENT = "com.hk47.realityoverlay.RETURN_INTENT";

    // Permissions
    public static final int BOTH_PERMISSIONS_REQUEST_CODE = 100;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    public static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 300;

    // Sensors
    public static final float LOW_PASS_FILTER_CONSTANT = 0.25f;

    // Location
    public static final int LOCATION_UPDATE_INTERVAL = 1000; // seconds
    public static final int LOCATION_UPDATE_DISPLACEMENT = 10; // meters

    // Places
    public static final String PLACES_API_KEY = "&key=" + BuildConfig.API_KEY;
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String USER_LOCATION = "location=";
    public static final String RANK_BY_DISTANCE = "&rankby=distance";
    public static final String PLACE_TYPES = "&types=";
    public static final String TYPE_CAFE = "cafe|";
    public static final String TYPE_RESTAURANT = "restaurant|";
    public static final String TYPE_BAR = "bar|";
    public static final String TYPE_PARK = "park|";
    public static final String TYPE_BOOKSTORE = "book_store|";
    public static final String TYPE_GALLERY = "art_gallery|";
    public static final String TYPE_MOVIE = "movie_theater|";
    public static final String TYPE_LODGING = "lodging|";
    public static final String TYPE_GROCERY = "grocery_or_supermarket|";
    public static final String TYPE_ATM = "atm|";
    public static final String TYPE_PHARMACY = "pharmacy|";
    public static final String TYPE_TRANSIT = "bus_station|train_station|transit_station|subway_station|";

    // Details
    public static final String DETAILS_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    public static final String PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=";

    // Icons and Colors
    public static final int DEFAULT_ID = 0;
    public static final int RESTAURANT_ID = 1;
    public static final int CAFE_ID = 2;
    public static final int BAR_ID = 3;
    public static final int PARK_ID = 4;
    public static final int BOOKSTORE_ID = 5;
    public static final int GALLERY_ID = 6;
    public static final int MOVIE_ID = 7;
    public static final int LODGING_ID = 8;
    public static final int GROCERY_ID = 9;
    public static final int ATM_ID = 10;
    public static final int PHARMACY_ID = 11;
    public static final int TRANSIT_ID = 12;
    public static final String RESTAURANT = "restaurant";
    public static final String CAFE = "cafe";
    public static final String BAR = "bar";
    public static final String PARK = "park";
    public static final String BOOKSTORE = "book_store";
    public static final String GALLERY = "art_gallery";
    public static final String MOVIE = "movie_theater";
    public static final String LODGING = "lodging";
    public static final String GROCERY = "grocery_or_supermarket";
    public static final String ATM = "atm";
    public static final String PHARMACY = "pharmacy";
    public static final String TRANSIT = "transit_station";
    public static final String BUS = "bus_station";
    public static final String TRAIN = "train_station";
    public static final String SUBWAY = "subway_station";

    public final static Location TEST_LOCATION = new Location("manual");
    static {
        TEST_LOCATION.setLatitude(45.4408f);
        TEST_LOCATION.setLongitude(12.3155f);
        TEST_LOCATION.setAltitude(0f);
    }

    // Widget
    public static final int WIDGET_LOADER_ID = 7000;
    public static final long ONE_DAY = 1000 * 60 * 60 * 24; // In milliseconds
}

