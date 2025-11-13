package org.me.gcu.lab7_google_maps;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.me.gcu.lab7_google_maps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ViewSwitcher avw;
    private Button s1Button;
    private Button s2Button;
    private RadioGroup mapTypeGroup;
    private RadioButton normalViewButton;
    private RadioButton terrainViewButton;
    private RadioButton hybridViewButton;
    private RadioButton satelliteViewButton;
    private CheckBox panZoom;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapTypeGroup = (RadioGroup)findViewById(R.id.mapTypeGroup);
        normalViewButton = (RadioButton)findViewById(R.id.normalViewRadio);
        terrainViewButton = (RadioButton)findViewById(R.id.terrainViewRadio);
        hybridViewButton = (RadioButton)findViewById(R.id.hybridViewRadio);
        satelliteViewButton = (RadioButton)findViewById(R.id.satelliteViewRadio);
        panZoom = (CheckBox)findViewById(R.id.panZoom);

        Log.e(getPackageName(), "just before avw");
        avw = (ViewSwitcher) findViewById(R.id.vwSwitch);
        if (avw == null)
        {
            Toast.makeText(getApplicationContext(), "Null ViewSwicther", Toast.LENGTH_LONG);
            Log.e(getPackageName(), "null pointer");
        }
        s1Button = (Button) findViewById(R.id.screen1Button);
        s2Button = (Button) findViewById(R.id.screen2Button);
        s1Button.setOnClickListener(this);
        s2Button.setOnClickListener(this);
        normalViewButton.setOnClickListener(this);
        terrainViewButton.setOnClickListener(this);
        hybridViewButton.setOnClickListener(this);
        satelliteViewButton.setOnClickListener(this);
        normalViewButton.toggle();
        panZoom.setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * Gets the user's current location and centers the map on it.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //task 1
        // Add a marker in Sydney and move the camera
        //        LatLng auckLand = new LatLng(-36.9, 174.8);
        //        mMap.addMarker(new MarkerOptions().position(auckLand).title("Marker in AuckLand"));
        //        mMap.moveCamera(CameraUpdateFactory.newLatLng(auckLand));

        //task 2
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, enable location layer and get current location
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void getCurrentLocation() {
        // Check permission again before accessing location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Got current location
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title("My Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        } else {
                            Toast.makeText(MapsActivity.this,
                                    "Unable to get current location. Please ensure location services are enabled.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location features
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required to show your location on the map",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == s1Button)
        {
            avw.showNext();
        }
        else
        if (arg0 == s2Button)
        {
            avw.showPrevious();
        }
        else
        if (arg0 == normalViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else
        if (arg0 == terrainViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else
        if (arg0 == hybridViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else
        if (arg0 == satelliteViewButton)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        if (panZoom.isChecked())
        {
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        else
        {
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }

    }
}