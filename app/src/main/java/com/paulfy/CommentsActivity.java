package com.paulfy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.paulfy.adpter.CommentsAdapter;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.model.NewsModel;
import com.paulfy.utils.SpinnerTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private RecyclerView rv_comments;
    private EditText edt_comment;
    private TextView btn_postComment;
    private List<NewsModel.Data.Comments> commentList = new ArrayList<>();
    private int news_id;
    CommentsAdapter adapter;
    private Button btn_post;
    private SpinnerTextView spin_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent i = new Intent();
        Bundle extras = getIntent().getExtras();
        news_id = extras.getInt("news_id", 1);
        commentList = (List<NewsModel.Data.Comments>) extras.getSerializable("comments");
        setTouchNClick(R.id.btn_close);
        edt_comment = findViewById(R.id.edt_comment);
        spin_filter = findViewById(R.id.spin_filter);
        spin_filter.setSelection(0);
        btn_post = findViewById(R.id.btn_post);
        setResponseListener(this);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentsAdapter(getContext(), commentList);
        rv_comments.setAdapter(adapter);
        edt_comment.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
        edt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (edt_comment.getText().toString().isEmpty()) {
                        return false;
                    }
//                    MyApp.showMassage(getContext(), "Send Pressed...");
                    RequestParams p = new RequestParams();
                    p.put("news_id", news_id);
                    p.put("user_id", MyApp.getApplication().readUser().getId());
                    p.put("comment", edt_comment.getText().toString());

                    postCall(getContext(), AppConstants.BASE_URL + "commentNews", p, "", 1);
                    handled = true;
                }
                return handled;
            }
        });

        setTouchNClick(R.id.btn_post);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_close) {
            finish();
        } else if (v.getId() == R.id.btn_post) {
            startActivityForResult(new Intent(getContext(), EnterCommentsActivity.class), 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                String string = data.getStringExtra("RESULT_STRING");
                if (string.isEmpty()) {
                    return;
                }
//                    MyApp.showMassage(getContext(), "Send Pressed...");
                RequestParams p = new RequestParams();
                p.put("news_id", news_id);
                p.put("user_id", MyApp.getApplication().readUser().getId());
                p.put("comment", string);

                postCall(getContext(), AppConstants.BASE_URL + "commentNews", p, "", 1);
            }
        }
    }

    private Context getContext() {
        return CommentsActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1 && o.optInt("code") == 200) {
//            MyApp.showMassage(getContext(), "Comment Posted Successfully");
            edt_comment.setText("");
            NewsModel.Data.Comments c = new NewsModel().new Data().new Comments();
            c.setCreated_at(o.optJSONObject("data").optString("created_at"));
            c.setComment(o.optJSONObject("data").optString("comment"));
            commentList.add(c);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onTimeOutRetry(int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {

    }

    @Override
    public void onFeedReceived(String error) {

    }
}
