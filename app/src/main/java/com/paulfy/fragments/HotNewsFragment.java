package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulfy.HomeActivity;
import com.paulfy.R;
import com.paulfy.adpter.HotNewsAdapter;
import com.paulfy.adpter.StringAdapter;

import java.util.ArrayList;


public class HotNewsFragment extends Fragment {
    private RecyclerView rv_home;

    public HotNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv_home = view.findViewById(R.id.rv_home);
        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        HotNewsAdapter adapter = new HotNewsAdapter(getContext(), new ArrayList<String>(10));
        rv_home.setAdapter(adapter);
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
