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


public class PostActivity extends AppCompatActivity {
    private final String TAG = "PostActivity";
    private EditText title;
    private EditText description;
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            latitude = extras.getDouble("currentLatitude");
            longitude = extras.getDouble("currentLongitude");
        }
    }

    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.post:
                Map<String,Object> map = new HashMap<>();
                map.put("longitude", longitude);
                map.put("latitude", latitude);
                map.put("title", title.getText().toString());
                map.put("description", description.getText().toString());

                Debug.print(TAG, "clicked", "longitude" + longitude + " latitude" + latitude, 2);

                HttpPostAsyncTask task = new HttpPostAsyncTask(map, this);

                task.execute("http://10.0.2.2:8080/products");
                break;
            case R.id.cancel:

                break;
        }
    }

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

        protected void onProgressUpdate(String... messages) {
            Toast.makeText(host, (messages[0]), Toast.LENGTH_SHORT).show();
        }

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
