package com.paulfy.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulfy.HomeActivity;
import com.paulfy.R;
import com.paulfy.adpter.HotNewsAdapter;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class AboutFragment extends Fragment {
    private RecyclerView rv_home;
    private ProfileFragment profileFragment;

    private TextView txt_saved,txt_hidden;

    @SuppressLint("ValidFragment")
    public AboutFragment(ProfileFragment profileFragment) {
        // Required empty public constructor
        this.profileFragment = profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        txt_saved = view.findViewById(R.id.txt_saved);
        txt_hidden = view.findViewById(R.id.txt_hidden);
        txt_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment.navigateSaved();
            }
        });

        txt_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment.navigateHidden();
            }
        });


        return view;
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


}
