package com.hk47.realityoverlay.widget;

import android.content.ContentValues;
import android.content.Context;

import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.PlacesContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetUtilities {

    public static String getRestaurantsUrlString(String userLocation) {
        return Constants.BASE_URL +
                Constants.USER_LOCATION +
                userLocation +
                Constants.RANK_BY_DISTANCE +
                Constants.PLACE_TYPES +
                Constants.TYPE_RESTAURANT +
                Constants.PLACES_API_KEY;
    }

    public static ArrayList<FeaturedRestaurant> processRestaurantsJson(JSONObject response) {

        ArrayList<FeaturedRestaurant> featuredRestaurants = new ArrayList<>();
        long timestamp = System.currentTimeMillis();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject restaurant = results.getJSONObject(i);
                String name = restaurant.getString("name");
                String place_id = restaurant.getString("place_id");
                featuredRestaurants.add(new FeaturedRestaurant(name, place_id, timestamp));
            }
        } catch (Exception e) {

        }
        return featuredRestaurants;
    }

    public static void populatePlacesDatabase(Context context,
                                              ArrayList<FeaturedRestaurant> featuredRestaurants) {
        for (FeaturedRestaurant restaurant : featuredRestaurants) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlacesContract.PlaceEntry.COLUMN_NAME, restaurant.getName());
            contentValues.put(PlacesContract.PlaceEntry.COLUMN_TIMESTAMP, restaurant.getTimeInMillis());
            contentValues.put(PlacesContract.PlaceEntry.COLUMN_PLACE_ID, restaurant.getPlace_id());

            try {
                context.getContentResolver().insert(PlacesContract.PlaceEntry.CONTENT_URI, contentValues);
            } catch (Exception e) {
                // The restaurant is already in the database, so we ignore the thrown exception
            }
        }
    }
}