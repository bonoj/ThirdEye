package com.hk47.realityoverlay.utils;

import android.content.Context;

import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;
import com.hk47.realityoverlay.data.CustomerReview;
import com.hk47.realityoverlay.data.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceDetailsUtilities {

    public static String getDetailsUrlString(String place_id) {
        return Constants.DETAILS_BASE_URL +
                place_id +
                Constants.PLACES_API_KEY;
    }

    public static String getPhotoUrlString(String photo_reference) {
        return Constants.PHOTO_BASE_URL +
                photo_reference +
                Constants.PLACES_API_KEY;
    }

    public static PlaceDetails processDetailsJson(JSONObject response) {

        PlaceDetails placeDetails = new PlaceDetails();
        try {
            JSONObject result = response.getJSONObject("result");
            String name = result.getString("name");
            placeDetails.setName(name);

            String place_id = result.getString("place_id");
            placeDetails.setPlace_id(place_id);

            double rating = result.getDouble("rating");
            placeDetails.setRating(rating);

            // Get contact information
            try {
                String formatted_address = result.getString("formatted_address");
                placeDetails.setFormatted_address(formatted_address);
            } catch (Exception contactInformationException) {

            }

            try {
                String formatted_phone_number = result.getString("formatted_phone_number");
                placeDetails.setFormatted_phone_number(formatted_phone_number);
            } catch (Exception contactInformationException) {

            }

            try {
                String international_phone_number = result.getString("international_phone_number");
                placeDetails.setInternational_phone_number(international_phone_number);
            } catch (Exception contactInformationException) {

            }

            // Get hours of operation
            try {
                JSONObject opening_hours = result.getJSONObject("opening_hours");
                boolean open_now = opening_hours.getBoolean("open_now");
                placeDetails.setOpen_now(open_now);

                JSONArray weekday_text_array = opening_hours.getJSONArray("weekday_text");
                String[] weekday_text = new String[weekday_text_array.length()];
                for (int i = 0; i < weekday_text_array.length(); i++) {
                    weekday_text[i] = weekday_text_array.getString(i);
                }
                placeDetails.setWeekday_text(weekday_text);
            } catch (Exception hoursException) {

            }

            // Get reviews
            try {
                JSONArray reviews_array = result.getJSONArray("reviews");
                ArrayList<CustomerReview> customerReviews = new ArrayList<>();
                for (int i = 0; i < reviews_array.length(); i++) {
                    JSONObject reviews_object = reviews_array.getJSONObject(i);
                    String author_name = reviews_object.getString("author_name");
                    int customer_rating = reviews_object.getInt("rating");
                    String relative_time_description = reviews_object.getString("relative_time_description");
                    String review_text = reviews_object.getString("text");
                    customerReviews.add(
                            new CustomerReview(
                                    author_name,
                                    customer_rating,
                                    relative_time_description,
                                    review_text));
                }
                placeDetails.setCustomerReviews(customerReviews);
            } catch (Exception reviewsExcption) {

            }

            // Get primary photo
            try {
                JSONArray photos_array = result.getJSONArray("photos");
                JSONObject photo_object = photos_array.getJSONObject(0);
                String photo_reference = photo_object.getString("photo_reference");

                placeDetails.setPhoto_reference(photo_reference);
            } catch (Exception photoException) {

            }
        } catch (Exception e) {

        }
        return placeDetails;
    }

    public static String getReviewsText(
            Context context,
            String restaurantName,
            ArrayList<CustomerReview> customerReviews) {
        StringBuilder reviewsBuilder = new StringBuilder();
        reviewsBuilder.append("\n");
        for (CustomerReview review : customerReviews) {
            String[] customerReview = review.getCustomerReview();
            reviewsBuilder.append(customerReview[0]);
            reviewsBuilder.append(" ");
            reviewsBuilder.append(context.getString(R.string.rated));
            reviewsBuilder.append(" ");
            reviewsBuilder.append(restaurantName);
            reviewsBuilder.append(" ");
            reviewsBuilder.append(customerReview[1]);
            reviewsBuilder.append(" ");
            if (customerReview[1].equals("1")) {
                reviewsBuilder.append(context.getString(R.string.star));
            } else {
                reviewsBuilder.append(context.getString(R.string.stars));
            }
            if (customerReview[2].equals("")) {
                reviewsBuilder.append(".\n");
            } else {
                reviewsBuilder.append(":\n");
                reviewsBuilder.append("\"");
                String customerReviewString = customerReview[2];
                if (customerReviewString.substring(customerReviewString.length() - 1).equals(" ")) {
                    customerReviewString = customerReviewString.substring(0, customerReviewString.length() - 1);
                }
                reviewsBuilder.append(customerReviewString);
                reviewsBuilder.append("\"\n");
            }
            reviewsBuilder.append(context.getString(R.string.reviewed));
            reviewsBuilder.append(" ");
            reviewsBuilder.append(customerReview[3]);
            reviewsBuilder.append(".\n\n");
        }
        String reviewsString = reviewsBuilder.toString();
        return reviewsString.substring(0,reviewsString.length() - 1);
    }
}