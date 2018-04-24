package com.example.possumunnki.locaaliapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is activity.
 * In this activity user is able to post products to the database.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2018-23-04
 */
public class PostActivity extends AppCompatActivity {
    private final String TAG = "PostActivity";
    /**Edit text box where user inputs title of the product*/
    private EditText title;
    /**Edit text box where user inputs description of the product*/
    private EditText description;
    /**Edit text box where user inputs price of the product*/
    private EditText price;
    /**Edit text box where user inputs amount of the product*/
    private EditText amount;
    /**latitude where the post will be added*/
    private double latitude;
    /**longitude where the post will be added*/
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        amount = (EditText) findViewById(R.id.amount);

        // gets longitude and latitude from main activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            latitude = extras.getDouble("currentLatitude");
            longitude = extras.getDouble("currentLongitude");
        }
    }

    /**
     * Called when user touches post button
     * @param v view of the button
     */
    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.post:
                if(checkInputs()){
                    postProduct();
                    finish();
                }
                break;
        }
    }

    /**
     * Checks that user has inputted all boxes.
     * @return whether inputs are correct or not.
     */
    public boolean checkInputs() {
        boolean result = true;

        if(title.getText().toString().trim().matches("")) {
            Toast.makeText(this, "Title must be filled!", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(description.getText().toString().trim().matches("")) {
            Toast.makeText(this, "´Description must be filled!", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(price.getText().toString().trim().matches("")) {
            Toast.makeText(this, "Price must be defined!", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(amount.getText().toString().matches("")) {
            Toast.makeText(this, "Amount must be defined!", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    /**
     * Posts product post to the database so that other users can see.
     */
    public void postProduct() {
        Map<String,Object> map = new HashMap<>();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("title", title.getText().toString());
        map.put("description", description.getText().toString());
        map.put("price", Double.parseDouble(price.getText().toString()));
        map.put("maxAmount", Integer.parseInt(amount.getText().toString()));
        map.put("timePosted", System.currentTimeMillis());

        Debug.print(TAG, "clicked", "longitude" + longitude + " latitude" + latitude, 2);

        HttpPostAsyncTask task = new HttpPostAsyncTask(map, this);

        task.execute("https://lokaali.herokuapp.com/products");
    }

    /**
     * This class connects to database and posts product post.
     */
    public class HttpPostAsyncTask extends AsyncTask<String, String, Void> {
        //JSON body of the post
        JSONObject data;
        AppCompatActivity host;
        public final String REQUEST_METHOD = "GET";
        public HttpPostAsyncTask(Map<String, Object> postData, AppCompatActivity host) {
            this.host = host;
            if (postData != null) {
                this.data = new JSONObject(postData);
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod(REQUEST_METHOD);

                urlConnection.setRequestProperty("Authorization", "someAuthString");
                Log.d(TAG, this.data.toString());
                // Send the post body
                if (this.data != null) {
                    //Post
                    postData(urlConnection, data);
                }

                int statusCode = urlConnection.getResponseCode();
                Log.d(TAG, "" + statusCode);

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... messages) {
            Toast.makeText(host, (messages[0]), Toast.LENGTH_SHORT).show();
        }

        /**
         * Posts the product post data to the database.
         * @param urlConnection url connection
         * @param data product post as JsonObject
         */
        private void postData(HttpURLConnection urlConnection, JSONObject data) {
            try {
                if (data != null) {
                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(data.toString());
                    writer.close();
                    outputStream.close();
                    publishProgress("Post Added!");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
