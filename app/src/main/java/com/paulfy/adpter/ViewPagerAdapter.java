package com.paulfy.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.paulfy.fragments.HomeTabFragment;
import com.paulfy.fragments.PopularTabFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    HomeTabFragment homeTabFragment;
    PopularTabFragment popularTabFragment;

    public ViewPagerAdapter(FragmentManager fm, HomeTabFragment homeTabFragment, PopularTabFragment popularTabFragment) {
        super(fm);
        this.homeTabFragment = homeTabFragment;
        this.popularTabFragment = popularTabFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            return homeTabFragment;
        } else if (position == 1) {
            return popularTabFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "New";
        } else if (position == 1) {
            title = "Trending";
        }
        return title;
    }
}
