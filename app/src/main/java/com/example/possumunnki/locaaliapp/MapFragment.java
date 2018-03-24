package com.example.possumunnki.locaaliapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The HelloWorld program implements an application that
 * simply displays "Hello World!" to the standard output.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class MapFragment extends Fragment {
    private final String TAG = "MapFragment";
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        return view;
    }
}
