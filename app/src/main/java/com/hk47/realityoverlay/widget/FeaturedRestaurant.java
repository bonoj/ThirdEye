package com.hk47.realityoverlay.widget;

public class FeaturedRestaurant {
    private String name;
    private String place_id;
    private long timestamp;

    public FeaturedRestaurant(String name, String place_id, long timestamp) {
        this.name = name;
        this.place_id = place_id;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public long getTimeInMillis() { return timestamp; }
}
