package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgetPasswordActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextInputEditText edt_username, edt_password;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra("userId");
        setResponseListener(this);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Forget Password");
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
            if (!edt_username.getText().toString().equals(edt_password.getText().toString())) {
                MyApp.popMessage("Error", "Password does not match", getContext());
                return;
            }

            RequestParams p = new RequestParams();
            p.put("new_password", edt_password.getText().toString());
            p.put("user_id", userId);
            postCall(getContext(), AppConstants.BASE_URL + "forgetPassword", p, "", 1);
        } else if (v.getId() == R.id.txt_signup) {
            startActivity(new Intent(getContext(), SignupActivity.class));
        } else if (v.getId() == R.id.txt_forgot) {

        }
    }

    private Context getContext() {
        return ForgetPasswordActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optInt("code") == 200 && o.optBoolean("status")) {
                MyApp.popFinishableMessage("Password changed", "Your password has been changed successfully." +
                        " Now please login with your new password", ForgetPasswordActivity.this);
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
