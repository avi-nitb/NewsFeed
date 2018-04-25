package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;

public class OptionLoginSignupActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        if (MyApp.getStatus(AppConstants.IS_LOGIN)) {
            startActivity(new Intent(getContext(), HomeActivity.class));
            finish();
            return;
        }
        setTouchNClick(R.id.btn_login);
        setTouchNClick(R.id.btn_signup);
        setTouchNClick(R.id.txt_skip);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.txt_skip) {
            startActivity(new Intent(getContext(), HomeActivity.class));
            finish();
        } else if (v.getId() == R.id.btn_login) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            finish();
        } else if (v.getId() == R.id.btn_signup) {
            startActivity(new Intent(getContext(), SignupActivity.class));
            finish();
        }
    }

    private Context getContext() {
        return OptionLoginSignupActivity.this;
    }
}
