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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                GoogleMap.OnMyLocationButtonClickListener,
                                                GoogleMap.OnMyLocationClickListener,
                                                GoogleMap.OnInfoWindowClickListener {
    private final String TAG = "MainActivity";
    private boolean locationPermission = false;
    private GoogleMap mMap;
    private Location currentLocation;
    private UiSettings mUIsettings;
    private double currentLongitude;
    private double currentLatitude;
    private ArrayList<Marker> markers;
    private static ArrayList<ProductPost> productPosts;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
        productPosts = new ArrayList<>();

        askLocationPermission();
        if(locationPermission) {
            fetchLocation();
        }

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMarkers(s);
                Debug.print(TAG, "onTextChanged", s.toString(), 2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        new HttpGetAsyncTask().execute("http://10.0.2.2:8080/products");
        if(postHere != null) {
            postHere.remove();
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

    public void filterMarkers(CharSequence filter) {
        //converts to lowercase
        String filterLow = filter.toString().toLowerCase();
        for(Marker marker: markers) {
            // converts also title so that user don't have to mind low and upper letter
            String titleLow = marker.getTitle().toLowerCase();
            if(titleLow.contains(filterLow)) {
                marker.setVisible(true);
            } else {
                marker.setVisible(false);
            }

        }
    }


    public void drawMarkers(ArrayList<ProductPost> posts) {
        //Debug.print(TAG, "drawMarkers", "" + posts.get(0).getTitle(), 2);
        for(int i = 0; i < posts.size(); i++) {
            Debug.print(TAG, "drawMarkers", "" + posts.get(i).getTitle(), 2);
            drawMarker(posts.get(i));
        }
    }

    public void drawMarker(ProductPost post) {
        int id = post.getId();
        String title = post.getTitle();
        String description = post.getDescription();
        double longitude = post.getLongitude();
        double latitude = post.getLatitude();

        Debug.print(TAG, "drawMarker", "Title: " + title, 2);
        LatLng gps = new LatLng(latitude, longitude);

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(gps)
                .title(title)
                .snippet(description)
                .draggable(true));
        markers.add(marker);
        post.setMarker(marker);
        marker.setTag(id);


    }

    public void drawPostPlaceMarker(LatLng gps) {
        postHere = mMap.addMarker(new MarkerOptions()
                .position(gps)
                .title("Post Here!")
                .snippet("Your post will be posted here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        postHere.setTag("post");
        postHere.showInfoWindow();
    }

    private Marker postHere;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(postHere != null) {
                    postHere.remove();
                }
                currentLongitude = latLng.longitude;
                currentLatitude = latLng.latitude;
                drawPostPlaceMarker(latLng);

            }
        });

        mUIsettings = mMap.getUiSettings();
        // enables zoom buttons
        mUIsettings.setZoomControlsEnabled(true);

        new HttpGetAsyncTask().execute("http://10.0.2.2:8080/products");
        zoomToTampere();
        Debug.print(TAG, "onMapReady()", "WE are on map ready", 1);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();

        if(marker.getTag() != null) {
            if(marker.getTag().toString().equals("post")) {
                Intent intent = new Intent(this, PostActivity.class);
                intent.putExtra("currentLongitude", currentLongitude);
                intent.putExtra("currentLatitude", currentLatitude);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ProductInformationActivity.class);
                ProductPost post = productPosts.get((int)marker.getTag());
                String productTitle = post.getTitle();
                String productDescription = post.getDescription();
                intent.putExtra("productTitle", productTitle);
                intent.putExtra("productDescription", productDescription);
                startActivity(intent);
            }

        }

    }

    public int findFromArray(Marker m) {
        int result = -1; // return this is not found
        for(int i = 0; i < markers.size(); i++) {
            if(markers.get(i).equals(m)) {
                result = i;
                break;
            }
        }
        return result;
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
                JSONArray posts = new JSONArray(stringBuilder.toString());
                publishProgress(posts);
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
            convertToProductPosts(jsonArrays[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        public void convertToProductPosts(JSONArray jsonArray) {
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String description = jsonObject.getString("description");
                    double longitude = jsonObject.getDouble("longitude");
                    double latitude = jsonObject.getDouble("latitude");

                    productPosts.add(new ProductPost(id, title, description, latitude, longitude));
                    Debug.print(TAG, "convertToProductPosts", "long" + longitude + " lat" + latitude, 2);
                }
                drawMarkers(productPosts);
            } catch (JSONException e) {
                Debug.print(TAG, "convertToProductPosts", e.toString(), 1);
            }

        }
    }
}
