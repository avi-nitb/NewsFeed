package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.paulfy.adpter.CommentsAdapter;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends CustomActivity implements CustomActivity.ResponseCallback{

    private RecyclerView rv_comments;
    private EditText edt_comment;
    private TextView btn_postComment;
    private List<NewsModel.Data.Comments> commentList= new ArrayList<>();
    private int news_id;
    CommentsAdapter adapter;

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
        Intent i = new Intent();
        Bundle extras = getIntent().getExtras();
        news_id = extras.getInt("news_id", 1);
        commentList= (List<NewsModel.Data.Comments>) extras.getSerializable("comments");
        setTouchNClick(R.id.btn_close);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        btn_postComment = (TextView) findViewById(R.id.btn_postComment);
        setTouchNClick(R.id.btn_postComment);
        setResponseListener(this);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentsAdapter(getContext(), commentList);
        rv_comments.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_close) {
            finish();
        } else if (v==btn_postComment){
            RequestParams p = new RequestParams();
            p.put("news_id", news_id);
            p.put("user_id", MyApp.getApplication().readUser().getId());
            p.put("comment", edt_comment.getText().toString());

            postCall(getContext(), AppConstants.BASE_URL + "commentNews", p, "Posting", 1);
        }
    }

    private Context getContext() {
        return CommentsActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber)
    {
        if (callNumber==1 && o.optInt("code")==200){
            MyApp.showMassage(getContext(), "Comment Posted Successfully");
            edt_comment.setText("");
            NewsModel.Data.Comments c= new NewsModel().new Data().new Comments();
            c.setCreated_at(o.optJSONObject("data").optString("created_at"));
            c.setComment(o.optJSONObject("data").optString("comment"));
            commentList.add(c);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber)
    {

    }

    @Override
    public void onTimeOutRetry(int callNumber)
    {

    }

    @Override
    public void onErrorReceived(String error)
    {

    }

    @Override
    public void onFeedReceived(String error)
    {

    }
}
