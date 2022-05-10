package com.dwiky.sigpertanian.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.databinding.ActivityLocationPickerBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPicker extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private ActivityLocationPickerBinding binding;
    SupportMapFragment mapFragment;
    GoogleMap myMap;
    Location myLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationPickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getSupportActionBar().hide();

        if (myMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(googleMap -> {
                myMap = googleMap;
                myMap.setOnMapClickListener(LocationPicker.this);
            });
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationPicker.this);
        getLocation();

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            LatLng myCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            myMap.addMarker(new MarkerOptions().position(myCurrentLocation).title("My Now Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation, 15f));
        });


    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        if(ActivityCompat.checkSelfPermission(
                LocationPicker.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                LocationPicker.this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ){
            return;
        }

        myMap.isMyLocationEnabled();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}