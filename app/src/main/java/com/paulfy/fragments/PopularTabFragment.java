package com.paulfy.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.paulfy.HomeActivity;
import com.paulfy.NewsDetailsActivity;
import com.paulfy.R;
import com.paulfy.adpter.PopularTab_Adapter;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.application.SingleInstance;
import com.paulfy.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularTabFragment extends CustomFragment implements CustomFragment.ResponseCallback , SearchView.OnQueryTextListener{
    RecyclerView rv_home;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    PopularTab_Adapter popularTab_adapter;
    RequestParams p = new RequestParams();
    List<NewsModel.Data> dataList = new ArrayList<>();
    NewsModel newsModel = new NewsModel();
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
        View myView = inflater.inflate(R.layout.fragment_populartab, container, false);
        rv_home = myView.findViewById(R.id.rv_home);
        progressBar = myView.findViewById(R.id.loader);
        swipeRefreshLayout= myView.findViewById(R.id.swipeRefreshLayout);
        ((HomeActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        setResponseListener(this);

        p.put("categories_id[0]", 2);
//        p.put("categories_id[1]", 2);
//        p.put("categories_id[2]", 3);
//        p.put("categories_id[3]", 4);
//        p.put("categories_id[4]", 5);


        if (SingleInstance.getInstance().getNew_newsList().size()>0){
            popularTab_adapter = new PopularTab_Adapter(PopularTabFragment.this, SingleInstance.getInstance().getNew_newsList());
            rv_home.setAdapter(popularTab_adapter);
            postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "", 0);
        } else{
            startProgress();
            postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Loading Please Wait...", 0);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        startProgress();
                        postCall(getContext(), AppConstants.BASE_URL + "getnewsByCategoriesId", p, "Loading Please Wait...", 0);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });
        return myView;
    }

    public int likedPosition = 0;
    public boolean isLikeStatus = false;

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {

        if (callNumber == 0 && o.optInt("code") == 200) {
            dataList.clear();
            newsModel = new Gson().fromJson(o.toString(), NewsModel.class);
            dataList.addAll(newsModel.getData());
            progressBar.clearAnimation();
            progressBar.setVisibility(View.GONE);
            SingleInstance.getInstance().setNew_newsList(dataList);
            popularTab_adapter = new PopularTab_Adapter(PopularTabFragment.this, SingleInstance.getInstance().getNew_newsList());
            rv_home.setAdapter(popularTab_adapter);
            popularTab_adapter.notifyDataSetChanged();
        } else if (callNumber == 4 && o.optInt("code") == 200) {
            popularTab_adapter.notifyDataSetChanged();

        } else if (callNumber == 5 && o.optInt("code") == 200) {
            MyApp.showMassage(getActivity(), "Saved successfully");
        } else if (callNumber == 6 && o.optInt("code") == 200) {
            MyApp.showMassage(getActivity(), "Hide successfully");
        } else {
            MyApp.showMassage(getActivity(), o.optString("message"));
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
                        popularTab_adapter.setFilter(dataList);
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
        final List<NewsModel.Data> filteredModelList = filter(dataList, newText);

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


    public void startProgress(){

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 500); // see this max value coming back here, we animate towards that value
        animation.setDuration(5000); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}
