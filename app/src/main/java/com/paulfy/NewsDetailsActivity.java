package com.paulfy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.paulfy.application.MyApp;
import com.paulfy.application.SingleInstance;
import com.paulfy.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

public class NewsDetailsActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextView txt_description;
    private List<String> imgUrls = new ArrayList<>();
    private BannerSlider bannerSlider;
    private NewsModel.Data d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("");


        d = SingleInstance.getInstance().getDataToLoad();

        imgUrls.addAll(extractUrls(d.getDescription()));
        bannerSlider = findViewById(R.id.banner_slider1);
        txt_description = findViewById(R.id.txt_description);
        String htmlSpan = Html.fromHtml(d.getDescription()).toString();
        txt_description.setText(htmlSpan);


        List<Banner> banners = new ArrayList<>();


        for (int i = 0; i < imgUrls.size(); i++) {
            String url = imgUrls.get(i);
            banners.add(new RemoteBanner(url));
        }
        bannerSlider.setBanners(banners);
        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        if (imgUrls.size() == 0) {
            bannerSlider.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        return (true);
    }

    public List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            String url = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
            if (MyApp.isImage(url)) {
                d.setDescription(d.getDescription().replace(url, ""));
                containedUrls.add(url);
            }

        }

        return containedUrls;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    private Context getContext() {
        return NewsDetailsActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {

        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onTimeOutRetry(int callNumber) {
        MyApp.popMessage("Error", "Time out occur, please check your internet connection and try again.", getContext());
    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());
    }

    @Override
    public void onFeedReceived(String error) {

    }


}
