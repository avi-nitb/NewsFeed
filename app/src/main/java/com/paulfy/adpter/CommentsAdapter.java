package com.paulfy.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulfy.R;
import com.paulfy.model.NewsModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by Abhishek on 22-04-2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    List<NewsModel.Data.Comments> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public CommentsAdapter(Context context, List<NewsModel.Data.Comments> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comments, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsModel.Data.Comments current = data.get(position);
        holder.txt_value.setText(current.getComment());
        holder.username.setText(current.getCreated_at());
//        holder.username.setText(current.getUser_id());
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_value, username;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_value = (TextView) itemView.findViewById(R.id.txt_comment);
            username = (TextView) itemView.findViewById(R.id.username);
        }
    }
}
