package com.example.possumunnki.locaaliapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                GoogleMap.OnMyLocationButtonClickListener,
                                                GoogleMap.OnMyLocationClickListener {
    private final String TAG = "MainActivity";
    private boolean locationPermission = false;
    private JSONArray productPosts;
    private GoogleMap mMap;
    private Location currentLocation;
    private UiSettings mUIsettings;
    private double currentLongitude;
    private double currentLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        askLocationPermission();
        if(locationPermission) {
            fetchLocation();
        }

    }

    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.newPost:
                Debug.print(TAG, "clicked()", "post CLICK", 2);
                Intent intent = new Intent(this, PostActivity.class);
                intent.putExtra("currentLongitude", currentLongitude);
                intent.putExtra("currentLatitude", currentLatitude);
                startActivity(intent);
                break;
        }

    }

    public void askLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            String[] listOfPermissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,listOfPermissions, 1);
        } else {
            locationPermission = true;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermission = true;

            }
        }
    }

    public void fetchLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    new Listener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zoomToTampere() {
        double latTampere = 61.497752;
        double longTampere = 23.760954;
        if (mMap != null) {
            mMap.clear();
            LatLng gps = new LatLng(latTampere, longTampere);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }
    }

    public void drawMarker(String title, double longitude, double latitude) {
        LatLng gps = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(gps)
                .title(title));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);


        mUIsettings = mMap.getUiSettings();
        // enables zoom buttons
        mUIsettings.setZoomControlsEnabled(true);

        new HttpGetAsyncTask().execute("http://10.0.2.2:8080/products");
        zoomToTampere();
        Debug.print(TAG, "onMapReady()", "WE are on map ready", 1);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Debug.print(TAG, "onLocationChanged", "we are onLocationChanged", 2);
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

    public class HttpGetAsyncTask extends AsyncTask<String, JSONArray, String> {
        public final String REQUEST_METHOD = "GET";
        public final int READ_TIMEOUT = 15000;
        public final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check whether the line we are reading
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                    Debug.print(TAG, "doInBackground",inputLine, 3);
                }
                productPosts = new JSONArray(stringBuilder.toString());
                publishProgress(productPosts);
                //Close InputStream and Buffered reader
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            } catch (JSONException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onProgressUpdate(JSONArray... jsonArrays) {
            drawPostsOnMap(jsonArrays[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        public void drawPostsOnMap(JSONArray jsonArray) {
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    double longitude = jsonObject.getDouble("longitude");
                    double latitude = jsonObject.getDouble("latitude");
                    drawMarker(title, longitude, latitude);
                    Debug.print(TAG, "drawPostsOnMap", "long" + longitude + " lat" + latitude, 2);
                }
            } catch (JSONException e) {
                Debug.print(TAG, "drawPostsOnMap", e.toString(), 1);
            }

        }
    }

}
