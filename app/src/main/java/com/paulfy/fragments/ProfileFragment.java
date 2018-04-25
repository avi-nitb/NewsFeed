package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
    private TextView btn_logout;
    private TextView txt_name;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((HomeActivity) getActivity()).toolbar.setVisibility(View.GONE);
        btn_logout = view.findViewById(R.id.btn_logout);
        txt_name = view.findViewById(R.id.txt_name);
        setTouchNClick(btn_logout);

        User u = MyApp.getApplication().readUser();
        txt_name.setText(u.getUser_name());
        if (u.getId() == 0) {
            txt_name.setVisibility(View.VISIBLE);
            txt_name.setText("Guest User");
            btn_logout.setVisibility(View.GONE);
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
        if (v == btn_logout) {
            MyApp.setStatus(AppConstants.IS_LOGIN, false);
            startActivity(new Intent(getActivity(), OptionLoginSignupActivity.class));
            getActivity().finishAffinity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                return new AboutFragment();
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

}
