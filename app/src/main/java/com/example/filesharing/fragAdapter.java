package com.example.filesharing;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class fragAdapter extends FragmentPagerAdapter {
int tabCount;
    public fragAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new sentFragment();
            case 1:
                return new receiveFragment();

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
