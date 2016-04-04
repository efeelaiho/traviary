package cs371m.traviary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.*;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cs371m.traviary.temp.SQLiteHelper;

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    String currentCity;
    String currentState;
    String currentCountry;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentCity = "";
                currentState = "";
                currentCountry = "";
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    /* get the city, state (if applicable), country */
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        currentCity += addresses.get(0).getLocality(); // city
                        currentState += addresses.get(0).getAdminArea(); // state
                        currentCountry += addresses.get(0).getCountryName(); // country
                        /* log the current state to location */
                        if (logState(currentState) == -1)
                            System.out.println("Not logged.");
                        else
                            System.out.println(currentState + " successfully logged.");
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                LatLng currentLocation = new LatLng(latitude, longitude);
                if (currentState != null) // location has an "admin area" (state)
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title
                            ("Marker in " + currentCity + ", " + currentState + ", " + currentCountry));
                else // location does not have an "admin area" (state)
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title
                            ("Marker in " + currentCity + ", " + currentCountry));
                if(mMap != null){
                    /* move the camera to current location */
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 6.0f));
                }
            }
        });
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }

    /*
     * save the current state to database
     */
    public long logState(String currentState) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        long success = sqLiteHelper.insertState(currentState);
        sqLiteHelper.close();
        return success;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
    */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }



}
