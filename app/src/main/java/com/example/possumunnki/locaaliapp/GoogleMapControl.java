package com.example.possumunnki.locaaliapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The HelloWorld program implements an application that
 * simply displays "Hello World!" to the standard output.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */

public class GoogleMapControl implements OnMapReadyCallback {
    private boolean locationPermission = false;
    private GoogleMap mMap;
    private AppCompatActivity host;
    public GoogleMapControl(AppCompatActivity host) {
        this.host = host;
    }

    public void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) host.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        askLocationPermission();
        if(locationPermission) {
            fetchLocation();
        }
    }

    public void askLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(host, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            String[] listOfPermissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(host,listOfPermissions, 1);
        } else {
            locationPermission = true;
        }
    }



    public void fetchLocation() {
        LocationManager locationManager = (LocationManager) host.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    new Listener());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void drawMarker(Location location) {
        if (mMap != null) {

            mMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }
    }

    public boolean isLocationPermission() {
        return locationPermission;
    }

    public void setLocationPermission(boolean locationPermission) {
        this.locationPermission = locationPermission;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new Listener();
    }
    class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            drawMarker(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
