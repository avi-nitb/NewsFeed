package com.paulfy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.paulfy.R;
import com.paulfy.adpter.PopularTab_Adapter;
import com.paulfy.application.AppConstants;
import com.paulfy.model.NewsModel;
import com.paulfy.model.RssModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularTabFragment extends CustomFragment implements CustomFragment.ResponseCallback
{
    RecyclerView rv_home;
    PopularTab_Adapter popularTab_adapter;
    List<NewsModel.Data> dataList = new ArrayList<>();
    NewsModel newsModel=new NewsModel();
    int caregories_id[]= {5};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View myView = inflater.inflate(R.layout.fragment_populartab, container, false);

        rv_home = myView.findViewById(R.id.rv_home);
        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        setResponseListener(this);
        RequestParams p= new RequestParams();
        p.put("categories_id[0]", 5 );
        postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Please Wait", 0);
        popularTab_adapter = new PopularTab_Adapter(getContext(), dataList);
        rv_home.setAdapter(popularTab_adapter);
        return myView;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber)
    {
        if(callNumber==0){

            newsModel= new Gson().fromJson(o.toString(),NewsModel.class);
            dataList.addAll(newsModel.getData());
            popularTab_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber)
    {

    }

    @Override
    public void onErrorReceived(String error)
    {

    }

    @Override
    public void onFeedReceived(String rssData)
    {

    }
}
