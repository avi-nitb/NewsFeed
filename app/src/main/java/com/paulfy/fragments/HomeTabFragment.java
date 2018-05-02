package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class HomeTabFragment extends CustomFragment implements CustomFragment.ResponseCallback, SearchView.OnQueryTextListener {
    private RecyclerView rv_home, rv_news;
    private TextView btn_load;
    private CategoryModel categoryModel;
    private NewsModel newsModel = new NewsModel();
    private PopularTab_Adapter popularTab_adapter;
    private List<CategoryModel.Data> dataList;
    private List<NewsModel.Data> newsdata;
    private HomeTabAdapter homeTabAdapter;
    private TextView text_cat_head;
    private TextView txt_choose_cat;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.hometabfragment, container, false);
        setResponseListener(this);
        //Initialization

        txt_choose_cat = myView.findViewById(R.id.txt_choose_cat);
        rv_home = myView.findViewById(R.id.rv_home);
        rv_news = myView.findViewById(R.id.rv_news);
        btn_load = myView.findViewById(R.id.btn_load);
        text_cat_head = myView.findViewById(R.id.text_cat_head);
        dataList = new ArrayList<>();
        newsdata = new ArrayList<>();
        categoryModel = new CategoryModel();
        homeTabAdapter = new HomeTabAdapter(getContext(), dataList, btn_load, HomeTabFragment.this);


        setTouchNClick(btn_load);
        setTouchNClick(txt_choose_cat);


        if (!MyApp.getApplication().isConnectingToInternet(getActivity())) {
            MyApp.popFinishableMessage("Alert", "Please connect to a working internet connection", getActivity());

        } else {
            //            Get Data
            RequestParams p = new RequestParams();
            postCall(getContext(), AppConstants.BASE_URL + "getAllCategories", p, "", 1);
        }

        if (MyApp.getApplication().readRequestMap().keySet().size() > 0) {

            RequestParams p = new RequestParams();

            HashMap<String, Integer> map = MyApp.getApplication().readRequestMap();
            for (String s : map.keySet()) {
                p.put(s, map.get(s));
            }
            rv_home.setVisibility(View.GONE);
//                txt_choose_cat.setVisibility(View.VISIBLE);
            text_cat_head.setVisibility(View.GONE);
            rv_news.setVisibility(View.VISIBLE);
            btn_load.setVisibility(View.GONE);
            p.put("user_id", MyApp.getApplication().readUser().getId());
            postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Please Wait", 2);


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
        if (v == txt_choose_cat) {

//            RequestParams p = new RequestParams();
//            postCall(getContext(), AppConstants.BASE_URL + "getAllCategories", p, "", 1);
            txt_choose_cat.setVisibility(View.GONE);
            rv_home.setVisibility(View.VISIBLE);
//            text_cat_head.setVisibility(View.VISIBLE);
            rv_news.setVisibility(View.GONE);
            btn_load.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1 && o.optInt("code") == 200) {

            categoryModel = new Gson().fromJson(o.toString(), CategoryModel.class);
            dataList.addAll(categoryModel.getData());
            homeTabAdapter.notifyDataSetChanged();

        } else if (callNumber == 2 && o.optInt("code") == 200) {

            newsdata.clear();

            newsModel = new Gson().fromJson(o.toString(), NewsModel.class);
            newsdata.addAll(newsModel.getData());
            rv_home.setVisibility(View.GONE);
            txt_choose_cat.setVisibility(View.VISIBLE);
            text_cat_head.setVisibility(View.GONE);
            rv_news.setVisibility(View.VISIBLE);
            btn_load.setVisibility(View.GONE);
            popularTab_adapter = new PopularTab_Adapter(HomeTabFragment.this, newsdata);
            rv_news.setAdapter(popularTab_adapter);
            popularTab_adapter.notifyDataSetChanged();
        } else if (callNumber == 4 && o.optInt("code") == 200) {
//            if (isLikeStatus)
//                newsdata.get(likedPosition).setLikeCount(newsdata.get(likedPosition).getLikeCount() + 1);
//            else
//                newsdata.get(likedPosition).setLikeCount(newsdata.get(likedPosition).getLikeCount() - 1);
//            popularTab_adapter.notifyDataSetChanged();
        } else if (callNumber == 5 && o.optInt("code") == 200) {
            MyApp.showMassage(getActivity(), "Saved successfully");
        } else if (callNumber == 6 && o.optInt("code") == 200) {
            MyApp.showMassage(getActivity(), "Hide successfully");
        } else {
            MyApp.showMassage(getActivity(), o.optString("message"));
        }

    }

    public int likedPosition = 0;
    public boolean isLikeStatus = false;

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

    public void clickNews(NewsModel.Data data) {
        SingleInstance.getInstance().setDataToLoad(data);
        startActivity(new Intent(getActivity(), NewsDetailsActivity.class));
    }

    public void callSaveApi(int id) {
        RequestParams p = new RequestParams();
        p.put("news_id", id);
        p.put("user_id", MyApp.getApplication().readUser().getId());

        postCall(getActivity(), AppConstants.BASE_URL + "bookmarkNews", p, "Saving...", 5);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        popularTab_adapter.setFilter(newsdata);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<NewsModel.Data> filteredModelList = filter(newsdata, newText);

        popularTab_adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<NewsModel.Data> filter(List<NewsModel.Data> models, String query) {
        query = query.toLowerCase();
        final List<NewsModel.Data> filteredModelList = new ArrayList<>();
        for (NewsModel.Data model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void callHideApi(int id) {
        RequestParams p = new RequestParams();
        p.put("news_id", id);
        p.put("user_id", MyApp.getApplication().readUser().getId());

        postCall(getActivity(), AppConstants.BASE_URL + "hideunhideNews", p, "Saving...", 6);
    }
}
