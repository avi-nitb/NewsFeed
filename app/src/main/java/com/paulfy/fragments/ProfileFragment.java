package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulfy.HomeActivity;
import com.paulfy.OptionLoginSignupActivity;
import com.paulfy.R;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.model.User;


public class ProfileFragment extends CustomFragment {

    private TextView tvUserName;
    private ImageView avatar;
    private TextView txt_follow_count;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        ((HomeActivity) getActivity()).toolbar.setVisibility(View.GONE);
        txt_follow_count = view.findViewById(R.id.txt_follow_count);

        User u = MyApp.getApplication().readUser();
        txt_follow_count.setText(u.getUser_name());
        if (u.getId() == 0) {
            txt_follow_count.setVisibility(View.VISIBLE);
            txt_follow_count.setText("Guest User");
//            btn_logout.setVisibility(View.GONE);
        }


        viewPager = view.findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void navigateSaved(){
        ((HomeActivity) getActivity()).navigateSaved();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void navigateHidden() {
        ((HomeActivity) getActivity()).navigateHidden();
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                return new CommentsFragment();
            } else if (position == 1) {
                return new AboutFragment(ProfileFragment.this);
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
                title = "Comments";
            } else if (position == 1) {
                title = "About";
            }
            return title;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                MyApp.setStatus(AppConstants.IS_LOGIN, false);
                startActivity(new Intent(getActivity(), OptionLoginSignupActivity.class));
                getActivity().finishAffinity();
                return true;
            case R.id.action_add:
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
