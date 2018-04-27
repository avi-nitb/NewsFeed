package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.paulfy.NewsDetailsActivity;
import com.paulfy.R;
import com.paulfy.adpter.HomeTabAdapter;
import com.paulfy.adpter.PopularTab_Adapter;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.application.SingleInstance;
import com.paulfy.model.CategoryModel;
import com.paulfy.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeTabFragment extends CustomFragment implements CustomFragment.ResponseCallback {
    private RecyclerView rv_home, rv_news;
    private TextView btn_load;
    private CategoryModel categoryModel;
    private NewsModel newsModel = new NewsModel();
    private PopularTab_Adapter popularTab_adapter;
    private List<CategoryModel.Data> dataList;
    private List<NewsModel.Data> newsdata;
    private HomeTabAdapter homeTabAdapter;
    private TextView text_cat_head;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.hometabfragment, container, false);
        setResponseListener(this);
        //Initialization

        rv_home = myView.findViewById(R.id.rv_home);
        rv_news = myView.findViewById(R.id.rv_news);
        btn_load = myView.findViewById(R.id.btn_load);
        text_cat_head = myView.findViewById(R.id.text_cat_head);
        dataList = new ArrayList<>();
        newsdata = new ArrayList<>();
        categoryModel = new CategoryModel();
        homeTabAdapter = new HomeTabAdapter(getContext(), dataList, btn_load, HomeTabFragment.this);


        setTouchNClick(btn_load);

        if (MyApp.getApplication().readRequestMap().keySet().size() == 0) {
            if (!MyApp.getApplication().isConnectingToInternet(getActivity())) {
                MyApp.popFinishableMessage("Alert", "Please connect to a working internet connection", getActivity());

            } else {
                //            Get Data
                RequestParams p = new RequestParams();
                postCall(getContext(), AppConstants.BASE_URL + "getAllCategories", p, "", 1);
            }

        } else {
            if (!MyApp.getApplication().isConnectingToInternet(getActivity())) {
                MyApp.popFinishableMessage("Alert", "Please connect to a working internet connection", getActivity());

            } else {
                RequestParams p = new RequestParams();

                HashMap<String, Integer> map = MyApp.getApplication().readRequestMap();
                for (String s : map.keySet()) {
                    p.put(s, map.get(s));
                }

                p.put("user_id", MyApp.getApplication().readUser().getId());
                postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Please Wait", 2);
            }

        }


        //setUp Recycler View

        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home.setAdapter(homeTabAdapter);

        rv_news.setLayoutManager(new LinearLayoutManager(getContext()));


        return myView;
    }

    public void getfeeds(List<Integer> categories) {
        RequestParams p = new RequestParams();
        int i = 0;
        HashMap<String, Integer> requestMap = new HashMap<>();
        if (categories.size() == 1) {
            p.put("categories_id[0]", categories.get(i));
            requestMap.put("categories_id[0]", categories.get(i));

        } else if (categories.size() == 2) {
            p.put("categories_id[0]", categories.get(i));
            requestMap.put("categories_id[0]", categories.get(i));
            p.put("categories_id[1]", categories.get(++i));
            requestMap.put("categories_id[1]", categories.get(i));
        } else if (categories.size() == 3) {
            p.put("categories_id[0]", categories.get(i));
            requestMap.put("categories_id[0]", categories.get(i));

            p.put("categories_id[1]", categories.get(++i));
            requestMap.put("categories_id[1]", categories.get(i));

            p.put("categories_id[2]", categories.get(++i));
            requestMap.put("categories_id[2]", categories.get(i));
        } else if (categories.size() == 4) {
            p.put("categories_id[0]", categories.get(i));
            requestMap.put("categories_id[0]", categories.get(i));

            p.put("categories_id[1]", categories.get(++i));
            requestMap.put("categories_id[1]", categories.get(i));

            p.put("categories_id[2]", categories.get(++i));
            requestMap.put("categories_id[2]", categories.get(i));

            p.put("categories_id[3]", categories.get(++i));
            requestMap.put("categories_id[3]", categories.get(i));

        } else if (categories.size() == 5) {
            p.put("categories_id[0]", categories.get(i));
            requestMap.put("categories_id[0]", categories.get(i));

            p.put("categories_id[1]", categories.get(++i));
            requestMap.put("categories_id[1]", categories.get(i));

            p.put("categories_id[2]", categories.get(++i));
            requestMap.put("categories_id[2]", categories.get(i));

            p.put("categories_id[3]", categories.get(++i));
            requestMap.put("categories_id[3]", categories.get(i));

            p.put("categories_id[4]", categories.get(++i));
            requestMap.put("categories_id[4]", categories.get(i));

        } else {
            MyApp.showMassage(getContext(), "Please select a category");
            return;
        }

        MyApp.getApplication().writeRequestMap(requestMap);

        p.put("user_id", MyApp.getApplication().readUser().getId());

        postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Please Wait", 2);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1 && o.optInt("code") == 200) {

            categoryModel = new Gson().fromJson(o.toString(), CategoryModel.class);
            dataList.addAll(categoryModel.getData());
            homeTabAdapter.notifyDataSetChanged();

        } else if (callNumber == 2 && o.optInt("code") == 200) {
            newsModel = new Gson().fromJson(o.toString(), NewsModel.class);
            newsdata.addAll(newsModel.getData());
            rv_home.setVisibility(View.GONE);
            text_cat_head.setVisibility(View.GONE);
            rv_news.setVisibility(View.VISIBLE);
            btn_load.setVisibility(View.GONE);
            popularTab_adapter = new PopularTab_Adapter(HomeTabFragment.this, newsdata);
            rv_news.setAdapter(popularTab_adapter);
            popularTab_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {

    }

    @Override
    public void onFeedReceived(String rssData) {

    }

    public void clickNews(NewsModel.Data data) {
        SingleInstance.getInstance().setDataToLoad(data);
        startActivity(new Intent(getActivity(), NewsDetailsActivity.class));
    }
}
