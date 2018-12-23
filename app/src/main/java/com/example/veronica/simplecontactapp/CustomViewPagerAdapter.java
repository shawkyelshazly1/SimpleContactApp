package com.example.veronica.simplecontactapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public CustomViewPagerAdapter(Context c, FragmentManager fm) {
        super(fm);
        this.context = c;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AllContactsFragment();

        } else {
            return new FavouriteContactsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "All Contacts";
        } else {
            return "Favourite Contacts";
        }
    }
}
