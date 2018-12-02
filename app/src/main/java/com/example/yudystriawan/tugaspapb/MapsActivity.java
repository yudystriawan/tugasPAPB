package com.example.yudystriawan.tugaspapb;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions origin, place2;
    private static final LatLng bunderanUB= new LatLng(-7.952656, 112.614330);
    private static LatLng locationNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            locationNow = new LatLng(extras.getDouble("lat"),extras.getDouble("lon"));
            origin = new MarkerOptions().position(locationNow).title("Your Here");
            place2 = new MarkerOptions().position(bunderanUB).title("Place1");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(origin);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationNow, 15));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
        mMap.addMarker(place2);
    }
}
