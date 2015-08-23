package se.umu.cs.c11epm.geochalapp.view;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.position.GPSLocator;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //actionBar = getSupportActionBar();

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.mainActivity);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.mainActivity, fragment)
                    .commit();
        }

        startGPS();
    }

    @Override
    protected void onPause() {
        stopGPS();
        super.onPause();

        Log.d("MAIN", "ON PAUSE!!!!!");
    }



    @Override
    protected void onResume() {
        startGPS();
        super.onResume();

        Log.d("MAIN", "ON RESUME!!!!");
    }

    public Location getPosition() {
        return ((GPSLocator)mLocationListener).getPosition();
    }

    private void startGPS() {
        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new GPSLocator();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, mLocationListener);
    }

    private void stopGPS() {
        mLocationManager.removeUpdates(mLocationListener);
        mLocationManager = null;
        mLocationListener = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment createFragment() {
        return new MainFragment();
    }
}


