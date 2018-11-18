package com.sanitcode.cataloguemoviedatabase.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sanitcode.cataloguemoviedatabase.fragment.NowPlayingFragment;
import com.sanitcode.cataloguemoviedatabase.fragment.PopularFragment;
import com.sanitcode.cataloguemoviedatabase.fragment.UpcomingFragment;

public class TabPager extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;

    public TabPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PopularFragment();

            case 1:
                return new NowPlayingFragment();

            case 2:
                return new UpcomingFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
