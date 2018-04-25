package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.paulfy.adpter.CommentsAdapter;

import java.util.ArrayList;

public class CommentsActivity extends CustomActivity {

    private RecyclerView rv_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        TextView toolbar_text = findViewById(R.id.toolbar_text);
//        toolbar_text.setText("Login");
        setTouchNClick(R.id.btn_close);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentsAdapter adapter = new CommentsAdapter(getContext(), new ArrayList<String>(10));
        rv_comments.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_close) {
            finish();
        }
    }

    private Context getContext() {
        return CommentsActivity.this;
    }
}
