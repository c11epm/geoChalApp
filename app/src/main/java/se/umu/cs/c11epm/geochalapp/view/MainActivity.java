package se.umu.cs.c11epm.geochalapp.view;

import android.content.Context;
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
import se.umu.cs.c11epm.geochalapp.model.UserInfo;
import se.umu.cs.c11epm.geochalapp.model.position.GPSLocator;

public class MainActivity extends AppCompatActivity {
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    private UserInfo userInfo;

    public void logout() {
        stopGPS();
        userInfo = null;
    }

    public enum views {
        LOGIN, MAIN, CHALLENGELIST, CREATECHALLENGE, USERINFO, ABOUT
    }
    public enum list {
        ME, OTHER, NONE
    }

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
            } else {
                //Empty fragments
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
    protected void onResume() {
        startGPS();
        super.onResume();
    }

    protected Location getPosition() {
        return ((GPSLocator)mLocationListener).getPosition();
    }

    protected GPSLocator getGPS() {
        return (GPSLocator) mLocationListener;
    }

    protected void startGPS() {
        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new GPSLocator();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, mLocationListener);
    }

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


