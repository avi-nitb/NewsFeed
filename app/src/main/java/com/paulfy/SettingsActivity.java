package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class SettingsActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextView txt_build_info, txt_ack, txt_user_agreement, txt_privacy_policy, txt_content_policy, txt_logout, txt_switch_user, txt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Settings");

        txt_build_info = findViewById(R.id.txt_build_info);
        txt_ack = findViewById(R.id.txt_ack);
        txt_user_agreement = findViewById(R.id.txt_user_agreement);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);
        txt_content_policy = findViewById(R.id.txt_content_policy);
        txt_logout = findViewById(R.id.txt_logout);
        txt_switch_user = findViewById(R.id.txt_switch_user);
        txt_about = findViewById(R.id.txt_about);

        setTouchNClick(R.id.txt_switch_user);
        setTouchNClick(R.id.txt_build_info);
        setTouchNClick(R.id.txt_ack);
        setTouchNClick(R.id.txt_privacy_policy);
        setTouchNClick(R.id.txt_content_policy);
        setTouchNClick(R.id.txt_user_agreement);
        setTouchNClick(R.id.txt_logout);
        setTouchNClick(R.id.txt_about);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txt_build_info.setText("V-" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == txt_logout) {
            MyApp.setStatus(AppConstants.IS_LOGIN, false);
            startActivity(new Intent(getContext(), LoginActivity.class));
            finishAffinity();
        } else if (v == txt_switch_user) {
            MyApp.showMassage(getContext(), "Out of the scope as per initial doc");
        } else if (v == txt_ack) {
            MyApp.showMassage(getContext(), "Out of the scope as per initial doc");
        } else if (v == txt_privacy_policy) {
            startActivity(new Intent(getContext(), InfoDetailsActivity.class).putExtra(AppConstants.EXTRA, "Privacy Policy").putExtra("num", 1));
        } else if (v == txt_content_policy) {
            startActivity(new Intent(getContext(), InfoDetailsActivity.class).putExtra(AppConstants.EXTRA, "Content Policy").putExtra("num", 2));
        } else if (v == txt_user_agreement) {
            startActivity(new Intent(getContext(), InfoDetailsActivity.class).putExtra(AppConstants.EXTRA, "User Agreement").putExtra("num", 3));
        } else if (v == txt_about) {
            startActivity(new Intent(getContext(), InfoDetailsActivity.class).putExtra(AppConstants.EXTRA, "About").putExtra("num", 4));
        }
    }


    private Context getContext() {
        return SettingsActivity.this;
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
