package com.example.possumunnki.locaaliapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is fragment of the menu bar.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2018-04-24
 */
public class MenuFragment extends Fragment {

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
