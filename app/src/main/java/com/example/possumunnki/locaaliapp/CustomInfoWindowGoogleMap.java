package com.example.possumunnki.locaaliapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The HelloWorld program implements an application that
 * simply displays "Hello World!" to the standard output.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private final String TAG = "CustomInfoWindowGoogleMap";
    private Context context;
    private SimpleDateFormat simpleDateFormat;
    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm");
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name = view.findViewById(R.id.name);
        TextView details = view.findViewById(R.id.details);
        TextView postedDate = view.findViewById(R.id.postedDate);
        if(marker.getTag().toString().equals("post")) {
            name.setText(marker.getTitle());
            details.setText(marker.getSnippet());
        } else {
            ProductPost post = (ProductPost) marker.getTag();
            name.setText(post.getTitle());
            details.setText(post.getDescription());

            Debug.print(TAG, "getInfoContains", "Jotain", 2);
            Long postTime = post.getPostedTime();
            Date date = new Date(postTime);
            postedDate.setText(simpleDateFormat.format(date));
        }




        return view;
    }
}
