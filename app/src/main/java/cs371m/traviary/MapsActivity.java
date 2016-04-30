package cs371m.traviary;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
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

import com.google.android.gms.common.api.GoogleApiClient;

import cs371m.traviary.database.SQLiteHelper;

public class MapsActivity extends ActionBarActivity implements OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback
        ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    // Progress Dialog
    private ProgressDialog pDialog;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M){
            //Check permissions for marshmallow and above versions

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                        PERMISSION_ACCESS_COARSE_LOCATION);
            } else {
                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        } else{
            // do something for phones running an SDK before marshmallow
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }
        googleApiClient.connect();

        /* Display the state name on the toolbar */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log My Location");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        mMap.setOnMyLocationButtonClickListener(this);
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

    /*
     * save the current state to database
     */
    public long logCountry(String currentCountry) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        long success = sqLiteHelper.insertCountry(currentCountry);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                    if (googleApiClient == null) {
                        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
                    }
                } else {
                    Toast.makeText(this, "Traviary needs your location", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)||(currentapiVersion < 23)) {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            String currentCity = "";
            String currentState = "";
            String currentCountry = "";
            System.out.println("LATITUDE: " + latitude); // ***** DEBUG *****
            System.out.println("LONGITUDE: " + longitude); // ***** DEBUG *****

            LatLng currentLocation = new LatLng(latitude, longitude);
            if (mMap != null) {
            /* move the camera to current location */
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 6.0f));
            }

            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
            /* get the city, state (if applicable), country */
                List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    currentCity += addresses.get(0).getLocality(); // city
                    currentState += addresses.get(0).getAdminArea(); // state
                    currentCountry += addresses.get(0).getCountryName(); // country
                    System.out.println("CURRENT CITY: " + currentCity); // ***** DEBUG *****
                    System.out.println("CURRENT STATE: " + currentState); // ***** DEBUG *****
                    System.out.println("CURRENT COUNTRY: " + currentCountry); // ***** DEBUG *****
                    new LogStateTask(MapsActivity.this).execute(currentCity, currentState, currentCountry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (currentState != null) // location has an "admin area" (state)
                mMap.addMarker(new MarkerOptions().position(currentLocation).title
                        ("Marker in " + currentCity + ", " + currentState + ", " + currentCountry));
            else // location does not have an "admin area" (state)
                mMap.addMarker(new MarkerOptions().position(currentLocation).title
                        ("Marker in " + currentCity + ", " + currentCountry));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    private class LogStateTask extends AsyncTask<String, String, Long> {

        private String country;
        private String state;
        private String city;
        String location;
        Context context;

        public LogStateTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            StringBuilder message = new StringBuilder("Attempting to log your location...");
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Long doInBackground(String... params) {
            this.city = params[0];
            this.state = params[1];
            this.country = params[2];
            if (state == null) { // not United States
                location = city + ", " + country;
            }
            else { // user in United States
                location = state + ", " + country;
            }
            System.out.println(location);
            long success;
            if (logState(state) == -1 || logCountry(country) == -1)
                success = -1;
            else
                success = 1;
            return success;
        }

        @Override
        protected void onPostExecute(Long success) {
            super.onPostExecute(success);
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success == -1) { // failure to log... already visited!
                System.out.println(location);
                new AlertDialog.Builder(this.context)
                        .setTitle("")
                        .setMessage("You have already logged " + location + ".")
                        .setNeutralButton("Close", null)
                        .show();
            }
            else {
                new AlertDialog.Builder(this.context)
                        .setTitle("SUCCESS")
                        .setMessage("You have successfully logged " + location + ".")
                        .setNeutralButton("Close", null)
                        .show();
            }
        }

    }

}
