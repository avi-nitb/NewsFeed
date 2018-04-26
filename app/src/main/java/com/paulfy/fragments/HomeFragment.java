package com.paulfy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulfy.HomeActivity;
import com.paulfy.R;
import com.paulfy.adpter.NewsAdapter;
import com.paulfy.adpter.ViewPagerAdapter;
import com.paulfy.model.RssModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;


public class HomeFragment extends CustomFragment implements CustomFragment.ResponseCallback {
    private RecyclerView rv_home;
    List<RssModel> dataList = new ArrayList<>();
    NewsAdapter adapter;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public HomeTabFragment homeTabFragment;
    public PopularTabFragment popularTabFragment;
    List<String> urls = new ArrayList<>();
    String selectedItem;
    final String[] array = {"News", "Tech", "Business", "Sports", "Entertainment", "Startup", "Education"};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        setResponseListener(this);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        popularTabFragment = new PopularTabFragment();
        homeTabFragment = new HomeTabFragment();
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), homeTabFragment, popularTabFragment);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Bundle bundle = getArguments();
        selectedItem = bundle.getString("selectedSearchItem");

//        addUrlsToList();

        rv_home = view.findViewById(R.id.rv_home);
//        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
       /* StringAdapter adapter = new StringAdapter(getContext(), new ArrayList<String>(10));
        rv_home.setAdapter(adapter);*/

        for (int i = 0; i < urls.size(); i++) {
//            new FetchFeedTask().execute(urls.get(i));
        }
      /*  adapter = new NewsAdapter(getActivity(), dataList);
        rv_home.setAdapter(adapter);*/
        return view;
    }

    private void addUrlsToList() {
        if (selectedItem.equalsIgnoreCase(array[0])) {
            urls.clear();
            urls.add("https://www.vanguardngr.com/news/feed");
//            urls.add("http://punchng.com/topics/news/feed/");
        } else if (selectedItem.equalsIgnoreCase(array[3])) {
            urls.clear();
            urls.add("https://www.completesportsnigeria.com/feed/");
            urls.add("https://www.thesportreview.com/feed/");
            urls.add("http://www.goal.com/en/feeds/news?fmt=rss&ICID=HP");
        } else if (selectedItem.equalsIgnoreCase(array[2])) {
            urls.clear();
            urls.add("https://www.cnbcafrica.com/feed/");
            urls.add("http://markets.businessinsider.com/rss/news");
            urls.add("https://nairametrics.com/feed/");
        } else if (selectedItem.equalsIgnoreCase(array[1])) {
            urls.clear();
            urls.add("https://techcrunch.com/feed/");
            urls.add("https://techmoran.com/feed/");
            urls.add("https://thenextweb.com/feed/");
            urls.add("https://www.techinasia.com/feed");
            urls.add("https://techpoint.ng/feed/");
            urls.add("http://disrupt-africa.com/feed/");
        } else if (selectedItem.equalsIgnoreCase(array[4])) {
            urls.clear();
            urls.add("http://www.gossipmill.com/feed/");
            urls.add("http://misspetitenaijablog.com/feed/");
            urls.add("https://www.kemifilani.com/feed");
            urls.add("https://lailasnews.com/feed/");
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


    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {

    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {

    }

    @Override
    public void onFeedReceived(String rssData) {
        rssToJson(rssData);
    }

    private void rssToJson(String sampleXml) {
        JSONObject jsonObj = null;
        try {
            String xmlString;  // some XML String previously created
            XmlToJson xmlToJson = new XmlToJson.Builder(sampleXml).build();
            jsonObj = new JSONObject(xmlToJson.toJson().toString());
            JSONObject o = jsonObj.getJSONObject("rss");
            JSONObject channel = o.getJSONObject("channel");
            JSONArray arr = channel.getJSONArray("item");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject d = arr.getJSONObject(i);
                RssModel r = new RssModel();
                r.setCategoryTags("News");
                r.setCreater(d.optString("dc:creator"));
                if (d.has("content:encoded")) {
                    r.setDescription(d.optString("content:encoded"));//description
                } else {
                    r.setDescription(d.optString("description"));//description
                }
                r.setHasImage(false);
                r.setImageLink("");
                r.setLink(d.optString("link"));
                r.setPubDate(d.optString("pubDate"));
                r.setTitle(d.optString("title"));
                dataList.add(r);
            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

        Log.d("XML", sampleXml);

    }
}
