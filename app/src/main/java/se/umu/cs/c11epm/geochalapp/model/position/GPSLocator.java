package se.umu.cs.c11epm.geochalapp.model.position;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * GPSLocator
 * Implements the LocationListener used to get the GPS position.
 * Also keeps track of whether or not a valid GPS-position is retrieved.
 *
 * Created by emil on 2015-08-22.
 */
public class GPSLocator implements LocationListener {
    private Location lastPos;
    private boolean validPosition = false;

    public GPSLocator() {}

    public boolean gotPosition() {
        return validPosition;
    }

    /**
     * Returns the GPS position object if retrieved, else null.
     * @return location object
     */
    public Location getPosition() {
        if(validPosition)
            return lastPos;
        else
            return null;
    }

    /**
     * Called by the Location service, updates the GPS position and sets validPosition
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        validPosition = true;
        lastPos = location;
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
}
