package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoDetailsActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private int callNumber;
    private TextView txt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText(getIntent().getStringExtra(AppConstants.EXTRA));

        callNumber = getIntent().getIntExtra("num", 0);
        txt_content = findViewById(R.id.txt_content);

        setupData();
    }

    private void setupData() {
        if (callNumber == 1) {
            txt_content.setText(Html.fromHtml(getString(R.string.privacy_policy)));
        } else if (callNumber == 2) {
            txt_content.setText(Html.fromHtml(getString(R.string.content_policy)));
        } else if (callNumber == 3) {
            txt_content.setText(Html.fromHtml(getString(R.string.user_agreement)));
        } else{
            txt_content.setText(Html.fromHtml(getString(R.string.about)));
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }


    private Context getContext() {
        return InfoDetailsActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {

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
