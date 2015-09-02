package se.umu.cs.c11epm.geochalapp.model.position;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by emil on 2015-08-22.
 */
public class GPSLocator implements LocationListener {
    private Location lastPos;
    private boolean validPosition = false;

    public GPSLocator() {}

    public boolean gotPosition() {
        return validPosition;
    }

    public Location getPosition() {
        if(validPosition)
            return lastPos;
        else
            return null;
    }

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
