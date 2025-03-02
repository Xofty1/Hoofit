package com.tvoyhod.hoofit.data;

import java.io.Serializable;

/**
 * The type Coordinate.
 */
public class Coordinate implements Serializable {
    private double latitude;
    private double longitude;

    /**
     * Instantiates a new Coordinate.
     */
    public Coordinate() {}

    /**
     * Instantiates a new Coordinate.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
