package com.example.possumunnki.locaaliapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.*;

/**
 * The HelloWorld program implements an application that
 * simply displays "Hello World!" to the standard output.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2017-05-12
 */
public class LokaaliFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_NUM = 3;
    public LokaaliFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new ProductPostListFragment();
            case 2:
                return new ProductPostListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }
}
