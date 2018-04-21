package com.example.possumunnki.locaaliapp;

import android.os.AsyncTask;

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
 * !!!!!I TRIED TO SORT MY CODE BUT IT SOMEHOW DIDN'T WORK!!!!!
 * !!!!!SO THIS CLASS IS DOING NOTHING!!!!!!
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class HttpConnectorGet {
    private final String TAG = "HttpConnectorGet";
    private final String CONNECT_URL = "http://10.0.2.2:8080/products";
    private ArrayList<ProductPost> productPosts;

    public HttpConnectorGet() {
        productPosts = new ArrayList<>();
    }


    public ArrayList<ProductPost> getProductPosts() {
        return this.productPosts;
    }

    public void refreshProductPosts() {
        new HttpGetAsyncTask().execute(CONNECT_URL);
    }

    public void setProductPosts(ArrayList<ProductPost> post) {
        this.productPosts = post;
    }

    public class HttpGetAsyncTask extends AsyncTask<String, JSONArray, String> {
        public final String REQUEST_METHOD = "GET";
        public final int READ_TIMEOUT = 15000;
        public final int CONNECTION_TIMEOUT = 15000;
        public ArrayList<ProductPost> posts;

        public HttpGetAsyncTask() {
            posts = new ArrayList<>();
        }
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
            setProductPosts(posts);
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

                    posts.add(new ProductPost(id, title, description, latitude, longitude));

                    Debug.print(TAG, "convertToProductPosts", "long" + longitude + " lat" + latitude, 2);
                }
            } catch (JSONException e) {
                Debug.print(TAG, "convertToProductPosts", e.toString(), 1);
            }

        }
    }
}
