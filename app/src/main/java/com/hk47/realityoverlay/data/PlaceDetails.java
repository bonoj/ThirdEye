package com.hk47.realityoverlay.data;

import java.util.ArrayList;

public class PlaceDetails {
    private String name = "";
    private String place_id = "";
    private int icon_id = 0;
    private double rating = 0;
    private String formatted_address = "";
    private String formatted_phone_number = "";
    private String international_phone_number = "";
    private boolean open_now;
    private String[] weekday_text;
    private ArrayList<CustomerReview> customerReviews;
    private String photo_reference;

    public PlaceDetails() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }

    public boolean getOpen_now() {
        return open_now;
    }

    public void setWeekday_text(String[] weekday_text) {
        this.weekday_text = weekday_text;
    }

    public String[] getWeekday_text() {
        return weekday_text;
    }

    public void setCustomerReviews(ArrayList<CustomerReview> customerReviews) {
        if (this.customerReviews != null) {
            this.customerReviews.clear();
        }
        this.customerReviews = customerReviews;
    }

    public ArrayList<CustomerReview> getCustomerReviews() {
        return customerReviews;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }
}
