package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextInputEditText edt_username, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResponseListener(this);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Login");
        setTouchNClick(R.id.txt_forgot);
        setTouchNClick(R.id.txt_signup);
        setTouchNClick(R.id.btn_login);

        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_login) {
            if (edt_username.getText().toString().isEmpty()) {
                edt_username.setError("Enter username");
                return;
            }
            if (edt_password.getText().toString().isEmpty()) {
                edt_password.setError("Enter password");
                return;
            }

            RequestParams p = new RequestParams();
            p.put("value", edt_username.getText().toString());
            p.put("password", edt_password.getText().toString());
            p.put("device_type", "android");
            p.put("device_token", MyApp.getSharedPrefString(AppConstants.DEVICE_TOKEN));
            postCall(getContext(), AppConstants.BASE_URL + "login", p, "Loggig you in...", 1);
        } else if (v.getId() == R.id.txt_signup) {
            startActivity(new Intent(getContext(), SignupActivity.class));
        } else if (v.getId() == R.id.txt_forgot) {

        }
    }

    private Context getContext() {
        return LoginActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optInt("code") == 200 && o.optBoolean("status")) {
                MyApp.showMassage(getContext(), o.optString("message"));
                User u = new Gson().fromJson(o.optJSONObject("data").toString(), User.class);
                MyApp.getApplication().writeUser(u);
                MyApp.setStatus(AppConstants.IS_LOGIN, true);
                startActivity(new Intent(getContext(), HomeActivity.class));
                finishAffinity();
            } else {
                MyApp.popMessage("Error!", "Invalid username or password", getContext());
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onTimeOutRetry(int callNumber) {
        MyApp.popMessage("Error!", "Timeout occurred, please check your internet connection and try again.", getContext());
    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error!", error, getContext());
    }

    @Override
    public void onFeedReceived(String error) {

    }
}
