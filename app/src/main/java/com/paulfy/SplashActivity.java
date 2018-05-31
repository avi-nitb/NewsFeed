package com.paulfy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paulfy.utils.ExceptionHandler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String crash = getIntent()
                .getStringExtra(ExceptionHandler.CRASH_REPORT);
        if (crash == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, OptionLoginSignupActivity.class));
                    finish();
                }
            }, 2000);
        } else {
            showCrashDialog(crash);
        }

    }

    public void showCrashDialog(final String report) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("App Crashed");
        b.setMessage("Oops! The app crashed due to below reason:\n\n" + report);

        DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/html");
                    i.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"bhavesh.soni@aquadsoft.com"});
                    i.putExtra(Intent.EXTRA_TEXT, report);
                    i.putExtra(Intent.EXTRA_SUBJECT, "App Crashed");
                    startActivity(Intent.createChooser(i, "Send Mail via:"));
                    finishAffinity();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashActivity.this, OptionLoginSignupActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }, 2000);
                }

            }
        };
        b.setCancelable(false);
        b.setPositiveButton("Send Report", ocl);
        b.setNegativeButton("Restart", ocl);
        b.create().show();
    }
}
