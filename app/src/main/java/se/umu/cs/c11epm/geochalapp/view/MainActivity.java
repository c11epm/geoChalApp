package se.umu.cs.c11epm.geochalapp.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Position;
import se.umu.cs.c11epm.geochalapp.model.UserInfo;
import se.umu.cs.c11epm.geochalapp.model.position.GPSLocator;

/**
 * MainActivity
 * The main activity of the application. Holds a log of support methods to
 * steer the flow of the application.
 */
public class MainActivity extends AppCompatActivity {
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private Menu menu;
    private UserInfo userInfo;
    private boolean loggedIn = false;

    /**
     * Runs at logout, stops gps and resets attributes.
     */
    public void logout() {
        stopGPS();
        userInfo = null;
        loggedIn = false;
    }

    /**
     * Runs at login and sets login attribute
     */
    public void login() {
        loggedIn = true;
    }

    /**
     * Enum for different views.
     */
    public enum views {
        LOGIN, MAIN, CHALLENGELIST, CREATECHALLENGE, USERINFO, ABOUT, MAP
    }

    /**
     * Enum for challenge list type
     */
    public enum list {
        ME, OTHER, NONE
    }

    /**
     * Hides the user info button in action bar.
     */
    protected void hideUserInfo() {
        MenuItem item = menu.findItem(R.id.menu_user);
        item.setVisible(false);
    }

    /**
     * Displays the user info button in action bar.
     */
    protected void showUserInfo() {
        MenuItem item = menu.findItem(R.id.menu_user);
        item.setVisible(true);
    }

    /**
     * Show the map.
     * @param pos challenge position
     * @param loc phone location
     */
    public void showMap(Position pos, Location loc) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("lat", pos.getLatitude());
        intent.putExtra("lon", pos.getLongitude());
        intent.putExtra("myLat", loc.getLatitude());
        intent.putExtra("myLon", loc.getLongitude());
        startActivity(intent);
    }

    /**
     * Changes the view in the application. Replaces the fragments and adds them in the back-stack.
     * @param view view type
     * @param type list type
     */
    public void changeView(views view, list type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int id = getID(view);
        Fragment f = getSupportFragmentManager().findFragmentById(id);

        if(f != null) {
            if(!type.equals(list.NONE)) {
                ((ChallengeListFragment)f).setType(type);
            }
            ft.replace(R.id.mainActivity, f);
            ft.addToBackStack(null);

            // Commit the transaction
            ft.commit();
        }
        else {
            if (view.equals(views.LOGIN)) {
                f = new LoginFragment();
            } else if(view.equals(views.MAIN)) {
                f = new MainFragment();
            } else if(view.equals(views.CHALLENGELIST)) {
                f = new ChallengeListFragment();
                if(type.equals(list.ME)) {
                    ((ChallengeListFragment)f).setType(type);
                } else {
                    ((ChallengeListFragment)f).setType(type);
                }

            } else if(view.equals(views.CREATECHALLENGE)) {
                f = new CreateChallengeFragment();
            } else if(view.equals(views.USERINFO)) {
                f = new UserInfoFragment();
            } else if(view.equals(views.ABOUT)) {
                f = new AboutFragment();
            }

            //Add to back stack if not login fragment is to be shown.
            if(!view.equals(views.LOGIN)) {
                ft.addToBackStack(null);
                if(loggedIn) {
                    showUserInfo();
                }
            } else {
                //Empty fragments
                hideUserInfo();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            ft.replace(R.id.mainActivity, f);
            ft.commit();
        }
    }

    public void setUser(UserInfo user) {
        this.userInfo = user;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * Gets the R.id for the different fragments.
     * @param view view type
     * @return R.id
     */
    private int getID(views view) {
        if(view.equals(views.LOGIN)) {
            return R.id.loginFragment;
        } else if(view.equals(views.MAIN)) {
            return R.id.mainFragment;
        } else if (view.equals(views.CHALLENGELIST)) {
            return R.id.challengeListFragment;
        } else if (view.equals(views.CREATECHALLENGE)) {
            return R.id.createChallengeFragment;
        } else if(view.equals(views.USERINFO)) {
            return R.id.userViewFragment;
        } else if(view.equals(views.ABOUT)) {
            return R.id.aboutFragment;
        } else {
            throw new RuntimeException("This should not be possible");
        }

    }

    /**
     * Creates and displays the login fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.mainActivity);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.mainActivity, fragment)
                    .commit();
        }
        //startGPS();
    }

    @Override
    protected void onPause() {
        stopGPS();
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem i = menu.findItem(R.id.menu_user);
        i.setVisible(false);
        return true;
    }

    @Override
    protected void onResume() {
        startGPS();
        super.onResume();
    }

    /**
     * Get position from GPSLocator
     * @return
     */
    protected Location getPosition() {
        return ((GPSLocator)mLocationListener).getPosition();
    }

    /**
     * Get GPSLocator object
     * @return
     */
    protected GPSLocator getGPS() {
        return (GPSLocator) mLocationListener;
    }

    /**
     * Starts the GPS listener
     */
    protected void startGPS() {
        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new GPSLocator();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, mLocationListener);
    }

    /**
     * Removes the GPS listener
     */
    protected void stopGPS() {
        if(mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
        }
        mLocationListener = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_user) {
            changeView(views.USERINFO, list.NONE);
            return true;
        } else if(id == R.id.menu_about) {
            changeView(views.ABOUT, list.NONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment createFragment() {
        return new LoginFragment();
    }
}


