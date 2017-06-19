package com.hk47.realityoverlay.widget;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.PlacesContract;
import com.hk47.realityoverlay.ui.OverlayActivity;
import com.hk47.realityoverlay.ui.PlaceDetailsActivity;
import com.hk47.realityoverlay.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class SuperfluousWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent serviceIntent = new Intent(context, UpdateWidgetService.class);
        context.startService(serviceIntent);
    }

    public static class UpdateWidgetService extends IntentService implements
            LocationListener, Loader.OnLoadCompleteListener<Cursor> {

        private LocationManager mLocationManager;
        private Location mLocation;
        private CursorLoader mCursorLoader;
        private CountDownLatch mDoneSignal = new CountDownLatch(1);

        public UpdateWidgetService() {
            super("UpdateWidgetService");
        }

        @Override
        public void onCreate() {
            super.onCreate();

            // Begin loading the existing database
            mCursorLoader = new CursorLoader(this,
                    PlacesContract.PlaceEntry.CONTENT_URI, null, null, null, null);
            mCursorLoader.registerListener(Constants.WIDGET_LOADER_ID, this);
            mCursorLoader.startLoading();
        }

        @Override
        public void onDestroy() {

            if (mCursorLoader != null) {
                mCursorLoader.unregisterListener(this);
                mCursorLoader.cancelLoad();
                mCursorLoader.stopLoading();
            }
            super.onDestroy();
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            // Unnecessary precaution
            if (intent == null) {
                return;
            }

            // Do nothing if user has not enabled permissions yet
            if (!checkLocationPermission(this)) {
                return;
            }

            // Check to see if the database is empty. If so, populate the database.
            Cursor cursor = this.getContentResolver().query(
                    PlacesContract.PlaceEntry.CONTENT_URI, null, null, null, null);
            if (!cursor.moveToFirst()) {

                // Get current location
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // Get a list of nearby restaurants
                if (mLocation != null) {
                    getNearbyRestaurants(getApplicationContext(), mLocation);
                }
            }

            // Wait for the update to complete.
            try {
                mDoneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLoadComplete(Loader<Cursor> loader, Cursor data) {

            if (data.moveToFirst()) {
                Random random = new Random();
                data.moveToPosition(random.nextInt(data.getCount()));
                String restaurant =
                        data.getString(data.getColumnIndex(PlacesContract.PlaceEntry.COLUMN_NAME));
                String place_id =
                        data.getString(data.getColumnIndex(PlacesContract.PlaceEntry.COLUMN_PLACE_ID));
                long timeOfQuery =
                        data.getLong(data.getColumnIndex(PlacesContract.PlaceEntry.COLUMN_TIMESTAMP));

                if (System.currentTimeMillis() >= timeOfQuery + Constants.ONE_DAY) {
                    // Clear the database every 24 hours
                    int deletedRows = this.getContentResolver().delete(PlacesContract.PlaceEntry.CONTENT_URI, null, null);
                }

                updateAllWidgets(this, restaurant, place_id);
                mDoneSignal.countDown();
            }
        }
    }

    private static void updateAllWidgets(Context context,
                                         String restaurantName,
                                         String place_id) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context.getPackageName(),
                SuperfluousWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.superfluous_widget);

            // Set the Featured Restaurant
            remoteViews.setTextViewText(R.id.widget_text_view, restaurantName);

            // Set the onClickPendingIntent to open Details when the widget is clicked
            Intent detailsIntent = new Intent(context, PlaceDetailsActivity.class);
            detailsIntent.putExtra(Constants.DETAILS_INTENT_PLACE_ID, place_id);
            detailsIntent.putExtra(Constants.DETAILS_INTENT_ICON_ID, Constants.RESTAURANT_ID);

            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addParentStack(OverlayActivity.class)
                    .addNextIntentWithParentStack(detailsIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private static boolean checkLocationPermission(Context context) {
        return ((ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    private static void getNearbyRestaurants(final Context context,
                                             final Location currentLocation) {
        String userLocation = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                WidgetUtilities.getRestaurantsUrlString(userLocation),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Populate the database with nearby restaurants
                        ArrayList<FeaturedRestaurant> featuredRestaurants
                                = WidgetUtilities.processRestaurantsJson(response);
                        WidgetUtilities.populatePlacesDatabase(context, featuredRestaurants);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError ||
                                error instanceof NetworkError ||
                                error instanceof TimeoutError) {
                            // There are worse things than an empty superfluous widget
                        } else {
                            // This could be handled elegantly, but it just isn't necessary
                        }
                    }
                }
        );

        // Get nearby restaurants from the Google Places API
        VolleySingleton.getInstance(
                context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
