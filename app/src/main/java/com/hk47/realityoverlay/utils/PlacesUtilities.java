package com.hk47.realityoverlay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.NearbyPlace;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlacesUtilities {

    public static String getPlacesUrlString(String userLocation, String typesString) {
        return Constants.BASE_URL +
                Constants.USER_LOCATION +
                userLocation +
                Constants.RANK_BY_DISTANCE +
                Constants.PLACE_TYPES +
                typesString +
                Constants.PLACES_API_KEY;
    }

    public static String getTypesString(Context context,
                                  SharedPreferences sharedPreferences) {
        StringBuilder builder = new StringBuilder();

        boolean cafesAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_cafe), true);
        if (cafesAreActivated) {
            builder.append(Constants.TYPE_CAFE);
        }
        boolean restaurantsAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_restaurant), true);
        if (restaurantsAreActivated) {
            builder.append(Constants.TYPE_RESTAURANT);
        }
        boolean barsAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_bar), false);
        if (barsAreActivated) {
            builder.append(Constants.TYPE_BAR);
        }
        boolean parksAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_park), false);
        if (parksAreActivated) {
            builder.append(Constants.TYPE_PARK);
        }
        boolean bookstoresAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_bookstore), false);
        if (bookstoresAreActivated) {
            builder.append(Constants.TYPE_BOOKSTORE);
        }
        boolean galleriesAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_gallery), false);
        if (galleriesAreActivated) {
            builder.append(Constants.TYPE_GALLERY);
        }
        boolean moviesAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_movie), false);
        if (moviesAreActivated) {
            builder.append(Constants.TYPE_MOVIE);
        }
        boolean lodgingsAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_lodging), false);
        if (lodgingsAreActivated) {
            builder.append(Constants.TYPE_LODGING);
        }
        boolean groceriesAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_grocery), false);
        if (groceriesAreActivated) {
            builder.append(Constants.TYPE_GROCERY);
        }
        boolean atmsAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_atm), false);
        if (atmsAreActivated) {
            builder.append(Constants.TYPE_ATM);
        }
        boolean pharmaciesAreActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_pharmacy), false);
        if (pharmaciesAreActivated) {
            builder.append(Constants.TYPE_PHARMACY);
        }
        boolean transitIsActivated = sharedPreferences.getBoolean(
                context.getString(R.string.preferences_type_transit), false);
        if (transitIsActivated) {
            builder.append(Constants.TYPE_TRANSIT);
        }
        return builder.toString();
    }


    public static ArrayList<NearbyPlace> processPlacesJson(Context context,
                                                           Location currentLocation,
                                                           JSONObject response,
                                                           String currentTypesString) {

        ArrayList<NearbyPlace> nearbyPlaces = new ArrayList<>();

        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject place = results.getJSONObject(i);
                JSONObject geometry = place.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                String lng = location.getString("lng");
                String name = place.getString("name");
                String place_id = place.getString("place_id");
                JSONArray types = place.getJSONArray("types");
                String type = getPlaceType(types, currentTypesString);
                name = formatName(name);

                int icon_id = getIconId(type);
                float[] bearingAndDistance = getBearingAndDistance(currentLocation, lat, lng, place_id);
                float bearingToPlace = bearingAndDistance[0];
                String distanceToPlace = formatDistance(context, bearingAndDistance[1]);

                nearbyPlaces.add(new NearbyPlace(name, place_id, icon_id, bearingToPlace, distanceToPlace));
            }
        } catch (Exception e) {

        }
        return nearbyPlaces;
    }

    private static float[] getBearingAndDistance(Location currentLocation,
                                                 String lat, String lng, String place_id) {
        Location placeLocation = new Location(place_id);
        placeLocation.setLatitude(Float.valueOf(lat));
        placeLocation.setLongitude(Float.valueOf(lng));

        float bearingToPlace = currentLocation.bearingTo(placeLocation);
        if (bearingToPlace < 0) {
            bearingToPlace += 360;
        }

        float distanceToPlace = currentLocation.distanceTo(placeLocation);

        return new float[]{bearingToPlace, distanceToPlace};
    }

    private static String getPlaceType(JSONArray types, String currentTypesString) {
        String typeString = "";
        for (int i = 0; i < types.length(); i++) {
            try {
                String type = types.getString(i);
                if (!type.equals("store")) {
                    if (currentTypesString.contains(type)) {
                        typeString = type;
                    }
                }
            } catch (Exception e) {

            }
        }
        return typeString;
    }

    public static int getIconId(String type) {
        int iconId;
        switch (type) {
            case Constants.RESTAURANT:
                iconId = Constants.RESTAURANT_ID;
                break;
            case Constants.CAFE:
                iconId = Constants.CAFE_ID;
                break;
            case Constants.BAR:
                iconId = Constants.BAR_ID;
                break;
            case Constants.PARK:
                iconId = Constants.PARK_ID;
                break;
            case Constants.BOOKSTORE:
                iconId = Constants.BOOKSTORE_ID;
                break;
            case Constants.GALLERY:
                iconId = Constants.GALLERY_ID;
                break;
            case Constants.MOVIE:
                iconId = Constants.MOVIE_ID;
                break;
            case Constants.LODGING:
                iconId = Constants.LODGING_ID;
                break;
            case Constants.GROCERY:
                iconId = Constants.GROCERY_ID;
                break;
            case Constants.ATM:
                iconId = Constants.ATM_ID;
                break;
            case Constants.PHARMACY:
                iconId = Constants.PHARMACY_ID;
                break;
            case Constants.BUS:
            case Constants.TRAIN:
            case Constants.SUBWAY:
            case Constants.TRANSIT:
                iconId = Constants.TRANSIT_ID;
                break;
            default:
                iconId = Constants.DEFAULT_ID;
                break;
        }
        return iconId;
    }

    private static String formatName(String name) {
        if (name.length() > 16) {
            name = name.substring(0, 15);
            if (name.contains(" ")) {
                if (name.endsWith(" ")) {
                    name = name.substring(0, 14);
                } else {
                    String[] nameArray = name.split(" ");
                    for (int i = 0; i < nameArray.length - 1; i++) {
                        if (i == 0) {
                            name = nameArray[i];
                        } else {
                            name = name + " " + nameArray[i];
                        }
                    }
                }
            }
        }
        return name;
    }

    private static String formatDistance(Context context, float distanceToPlace) {
        String formattedDistance = "";
        if (distanceToPlace > 1000) {
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            distanceToPlace = distanceToPlace / 1000;
            formattedDistance = decimalFormat.format(distanceToPlace) + context.getString(R.string.kilometers);
        } else {
            formattedDistance = String.valueOf((int) distanceToPlace) + context.getString(R.string.meters);
        }
        return formattedDistance;
    }
}
