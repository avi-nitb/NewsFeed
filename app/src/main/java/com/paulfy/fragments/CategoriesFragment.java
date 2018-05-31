package com.paulfy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
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

public class CategoriesFragment extends CustomFragment implements CustomFragment.ResponseCallback {

    RecyclerView rv_home;
    RecyclerView rv_news;
    private TextView btn_load;
    private CategoryModel categoryModel;
    private List<CategoryModel.Data> dataList;
    private TextView text_cat_head;
    private NewsModel newsModel = new NewsModel();
    private PopularTab_Adapter popularTab_adapter;
    //    private TextView txt_choose_cat;
    private HomeTabAdapter homeTabAdapter;
    private ImageButton btn_back;
    private List<NewsModel.Data> newsdata = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btn_back) {
            btn_back.setVisibility(View.GONE);
            rv_home.setVisibility(View.VISIBLE);
            rv_news.setVisibility(View.GONE);
        }
//        if (v == txt_choose_cat) {
//            txt_choose_cat.setVisibility(View.GONE);
//            rv_home.setVisibility(View.VISIBLE);
//            btn_load.setVisibility(View.VISIBLE);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_categories, container, false);
//        txt_choose_cat = myView.findViewById(R.id.txt_choose_cat);
        rv_home = myView.findViewById(R.id.rv_home);
        rv_news = myView.findViewById(R.id.rv_news);
        btn_back = myView.findViewById(R.id.btn_back);
        btn_load = myView.findViewById(R.id.btn_load);
        text_cat_head = myView.findViewById(R.id.text_cat_head);
        dataList = new ArrayList<>();
        categoryModel = new CategoryModel();
        homeTabAdapter = new HomeTabAdapter(getActivity(), dataList, btn_load, CategoriesFragment.this);
        setResponseListener(this);
        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home.setAdapter(homeTabAdapter);
        rv_news.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_news.setAdapter(homeTabAdapter);

        setTouchNClick(btn_load);
        setTouchNClick(btn_back);
        btn_back.setVisibility(View.GONE);
        if (!MyApp.getApplication().isConnectingToInternet(getActivity())) {
            MyApp.popFinishableMessage("Alert", "Please connect to a working internet connection", getActivity());
        } else {
            RequestParams p = new RequestParams();
            postCall(getContext(), AppConstants.BASE_URL + "getAllCategories", p, "", 1);
        }
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

//        if (SingleInstance.getInstance().getNews().size()>0) {
//            popularTab_adapter = new PopularTab_Adapter(HomeTabFragment.this,SingleInstance.getInstance().getNews());
//            rv_news.setAdapter(popularTab_adapter);
//            postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "", 2);
//        } else {
//            startProgress();
//            postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Loading Please Wait...", 2);
//        }
    }

    public void getfeeds(int id, List<Integer> categories) {
        RequestParams p = new RequestParams();
        if (id == -1) {
            p.put("categories_id[0]", "1");
            p.put("categories_id[1]", "2");
            p.put("categories_id[2]", "3");
            p.put("categories_id[3]", "4");
            p.put("categories_id[4]", "5");
        } else
            p.put("categories_id[0]", id);
        p.put("user_id", MyApp.getApplication().readUser().getId());
        showLoadingDialog("");
        postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Loading Please Wait...", 2);
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1 && o.optInt("code") == 200) {

            dataList.clear();
            CategoryModel.Data d = new CategoryModel().new Data();
            d.setId(-1);
            d.setName("All");
            dataList.add(d);
            categoryModel = new Gson().fromJson(o.toString(), CategoryModel.class);
            dataList.addAll(categoryModel.getData());
            homeTabAdapter.notifyDataSetChanged();

        } else if (callNumber == 2 && o.optInt("code") == 200) {
            dismissDialog();
            newsdata.clear();
            btn_back.setVisibility(View.VISIBLE);
            newsModel = new Gson().fromJson(o.toString(), NewsModel.class);
            newsdata.addAll(newsModel.getData());
            SingleInstance.getInstance().setNews(newsdata);
            rv_news.setVisibility(View.VISIBLE);
            rv_home.setVisibility(View.GONE);
            popularTab_adapter = new PopularTab_Adapter(CategoriesFragment.this, SingleInstance.getInstance().getNews());
            rv_news.setAdapter(popularTab_adapter);
//            progressBar.clearAnimation();
//            progressBar.setVisibility(View.GONE);
            popularTab_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getActivity());
    }

    @Override
    public void onFeedReceived(String rssData) {

    }


}
