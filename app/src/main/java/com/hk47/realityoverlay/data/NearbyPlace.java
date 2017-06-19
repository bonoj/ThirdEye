package com.hk47.realityoverlay.data;

public class NearbyPlace {
    private String name;
    private String place_id;
    private int icon_id;
    private float bearingToPlace;
    private String distanceToPlace;

    private int quadrant = 0;
    private float iconX;
    private float iconY;

    public NearbyPlace(String name,
                       String place_id,
                       int icon_id,
                       float bearingToPlace,
                       String distanceToPlace) {
        this.name = name;
        this.place_id = place_id;
        this.icon_id = icon_id;
        this.bearingToPlace = bearingToPlace;
        this.distanceToPlace = distanceToPlace;
    }

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public float getBearingToPlace() {
        return bearingToPlace;
    }

    public String getDistanceToPlace() {
        return distanceToPlace;
    }

    public int getQuadrant() { return quadrant; }

    public void setQuadrant(int quadrant) {
        this.quadrant = quadrant;
    }

    public float getIconX() { return iconX; }

    public void setIconX(float iconX) { this.iconX = iconX; }

    public float getIconY() { return iconY; }

    public void setIconY(float iconY) { this.iconY = iconY; }
}
