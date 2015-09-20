package se.umu.cs.c11epm.geochalapp.view;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import se.umu.cs.c11epm.geochalapp.R;
import se.umu.cs.c11epm.geochalapp.model.Position;

/**
 * MapActivity
 * Displays the map with challenge position and phone position
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Position pos;
    private Position myPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar ab = getActionBar();

        if(ab != null) {
            ab.setTitle("GeoChal - Challenge");
        }

        getPos();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Collect the positions from intent
     */
    private void getPos() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        pos = new Position();
        myPos = new Position();
        pos.setLatitude((double) extras.get("lat"));
        pos.setLongitude((double) extras.get("lon"));
        myPos.setLatitude((double) extras.get("myLat"));
        myPos.setLongitude((double) extras.get("myLon"));

    }

    /**
     * When map is ready, display the positions
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng chal = new LatLng(pos.getLatitude(), pos.getLongitude());
        LatLng pos = new LatLng(myPos.getLatitude(), myPos.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(chal).title("Challenge"));
        googleMap.addMarker(new MarkerOptions().position(pos).title("Position"));
        CameraPosition cpos = new CameraPosition.Builder().target(chal).zoom(17).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cpos));
    }
}
