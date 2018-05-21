package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.paulfy.application.MyApp;

public class EnterCommentsActivity extends CustomActivity {

    private EditText edt_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_comment);
        setTouchNClick(R.id.img_close);
        setTouchNClick(R.id.btn_post_comment);

        edt_comment = findViewById(R.id.edt_comment);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.img_close) {
            finish();
        } else if (v.getId() == R.id.btn_post_comment) {
            String comment = edt_comment.getText().toString();
            if (comment.isEmpty()) {
                MyApp.popMessage("Alert!", "You cannot post a empty comment", getContext());
            } else {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", comment);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    private Context getContext() {
        return EnterCommentsActivity.this;
    }


}
