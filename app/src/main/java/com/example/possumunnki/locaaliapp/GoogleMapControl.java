package com.example.possumunnki.locaaliapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

/**
 * This class Implements Google map.
 *
 * @author Akio Ide
 * @version 1.4
 * @since 2018-23-04
 */

public class GoogleMapControl extends AppCompatActivity implements OnMapReadyCallback,
                    GoogleMap.OnMyLocationButtonClickListener,
                    GoogleMap.OnMyLocationClickListener,
                    GoogleMap.OnInfoWindowClickListener {
    /**This is used for Debug.print to make easier to debug*/
    private final String TAG = "MainActivity";
    /**Whether user has given permission of location or not*/
    private boolean locationPermission = false;
    /**google map*/
    private GoogleMap mMap;
    /**zoom button is able to add by using this*/
    private UiSettings mUIsettings;
    /**longitude of post marker*/
    private double currentLongitude;
    /**latitude of post marker*/
    private double currentLatitude;

    /**data of product posts*/
    private static ArrayList<ProductPost> productPosts;
    /**List of markers*/
    private ArrayList<Marker> markers;

    /**User is able to add product posts by tapping this marker*/
    private Marker postHere;

    /**search box*/
    private EditText search;

    private final String URL = "https://lokaali.herokuapp.com/products";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markers = new ArrayList<>();
        productPosts = new ArrayList<>();
        // asks permission of location
        askLocationPermission();

        if(locationPermission) {
            search = (EditText) findViewById(R.id.search);
            //do something when user modifies search box
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
    }

    @Override
    public void onResume() {
        super.onResume();
        new HttpGetAsyncTask().execute(URL);
        if(postHere != null) {
            postHere.remove();
        }
    }

    /**
     * Checks whether user has already given permission of location or not.
     * If user hasn't, it asks permission of location by using dialog.
     */
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

    /**
     * Zooms map to Tampere.
     */
    public void zoomToTampere() {
        double latTampere = 61.497752;
        double longTampere = 23.760954;
        if (mMap != null) {
            mMap.clear();
            LatLng gps = new LatLng(latTampere, longTampere);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }
    }

    /**
     * Finds the markers that contains search characters
     * and hides those that doesn't contain.
     *
     * @param filter search word
     */
    public void filterMarkers(CharSequence filter) {
        //converts to lowercase
        String filterLow = filter.toString().toLowerCase();
        for(Marker marker: markers) {
            // converts also title so that user don't have to mind low and upper letter
            String titleLow = marker.getTitle().toLowerCase();
            Debug.print(TAG, "filterMarkers", "" + titleLow.contains(filterLow), 2);
            if(titleLow.contains(filterLow)) {
                marker.setVisible(true);
            } else {
                marker.setVisible(false);
            }
        }
    }

    /**
     * Draws product posts on google map.
     *
     * @param posts product post dates
     */
    public void drawMarkers(ArrayList<ProductPost> posts) {
        //Debug.print(TAG, "drawMarkers", "" + posts.get(0).getTitle(), 2);
        for(int i = 0; i < posts.size(); i++) {
            drawMarker(posts.get(i));
        }
    }

    /**
     * Draws the marker on google map.
     * @param post
     */
    public void drawMarker(ProductPost post) {
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
        post.setMarker(marker);
        markers.add(marker);
        marker.setTag(post);
    }

    /**
     * Draws post marker.
     * @param gps
     */
    public void drawPostPlaceMarker(LatLng gps) {
        postHere = mMap.addMarker(new MarkerOptions()
                .position(gps)
                .title("Post Here!")
                .snippet("Your post will be posted here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        postHere.setTag("post");
        postHere.showInfoWindow();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(locationPermission) {
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

            new HttpGetAsyncTask().execute(URL);
            zoomToTampere();
            Debug.print(TAG, "onMapReady()", "WE are on map ready", 1);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTag() != null) {
            if(marker.getTag().toString().equals("post")) {
                Intent intent = new Intent(this, PostActivity.class);
                intent.putExtra("currentLongitude", currentLongitude);
                intent.putExtra("currentLatitude", currentLatitude);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ProductInformationActivity.class);
                ProductPost post = (ProductPost)marker.getTag();
                intent.putExtra("productTitle", post.getTitle());
                intent.putExtra("productDescription", post.getDescription());
                intent.putExtra("amount", post.getAmount());
                intent.putExtra("price", post.getPrice());
                intent.putExtra("postedDate", post.getPostedTime());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /**
     * This class fetches product post dates from the data base.
     */
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
                    //Debug.print(TAG, "doInBackground",inputLine, 3);
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

        @Override
        protected void onProgressUpdate(JSONArray... jsonArrays) {
            convertToProductPosts(jsonArrays[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        /**
         * Converts jsonArray to ProductPosts.
         * @param jsonArray json array which includes fetched data
         */
        public void convertToProductPosts(JSONArray jsonArray) {
            ArrayList<ProductPost> posts = new ArrayList<>();
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String description = jsonObject.getString("description");
                    double longitude = jsonObject.getDouble("longitude");
                    double latitude = jsonObject.getDouble("latitude");
                    long postedTime = jsonObject.getLong("timePosted");
                    double price = jsonObject.getDouble("price");
                    int amount = jsonObject.getInt("maxAmount");

                    posts.add(new ProductPost(id, title, description, latitude, longitude,postedTime, price, amount));
                    //Debug.print(TAG, "convertToProductPosts", "long" + longitude + " lat" + latitude, 2);
                }
                productPosts = posts;
                drawMarkers(productPosts);
            } catch (JSONException e) {
                Debug.print(TAG, "convertToProductPosts", e.toString(), 1);
            }
        }
    }
}

