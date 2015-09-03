package se.umu.cs.c11epm.geochalapp.model;

/**
 * Created by emil on 2015-09-03.
 */
public class Position {
    private double longitude;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
