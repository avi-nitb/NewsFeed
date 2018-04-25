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

public class SignupActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextInputEditText edt_email, edt_username, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Create Account");
        setTouchNClick(R.id.txt_login);
        setTouchNClick(R.id.btn_create_account);

        edt_email = findViewById(R.id.edt_email);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.txt_login) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            finishAffinity();
        } else if (v.getId() == R.id.btn_create_account) {
            if (edt_email.getText().toString().isEmpty()) {
                edt_email.setError("Enter you email-id");
                return;
            }
            if (!MyApp.isEmailValid(edt_email.getText().toString())) {
                edt_email.setError("Enter a valid email id");
                return;
            }
            if (edt_username.getText().toString().isEmpty()) {
                edt_username.setError("Enter username");
                return;
            }
            if (edt_password.getText().toString().isEmpty()) {
                edt_password.setError("Enter password");
                return;
            }

            RequestParams p = new RequestParams();
            p.put("user_name", edt_username.getText().toString());
            p.put("email", edt_email.getText().toString());
            p.put("password", edt_password.getText().toString());
            p.put("device_type", "android");
            p.put("device_token", MyApp.getSharedPrefString(AppConstants.DEVICE_TOKEN));
            postCall(getContext(), AppConstants.BASE_URL + "register", p, "Registering you...", 1);
//            startActivity(new Intent(getContext(), HomeActivity.class));
//            finishAffinity();
        }
    }

    private Context getContext() {
        return SignupActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optInt("code") == 200 && o.optBoolean("status")) {
                MyApp.showMassage(getContext(), o.optString("message"));
                User u = new Gson().fromJson(o.optJSONObject("data").toString(), User.class);
                MyApp.getApplication().writeUser(u);
                MyApp.setStatus(AppConstants.IS_LOGIN,true);
                startActivity(new Intent(getContext(), HomeActivity.class));
                finishAffinity();
            } else {
                MyApp.popMessage("Error!", o.optString("message"), getContext());
            }
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
