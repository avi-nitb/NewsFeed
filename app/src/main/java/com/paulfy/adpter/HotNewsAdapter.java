package com.paulfy.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulfy.CommentsActivity;
import com.paulfy.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Abhishek on 22-04-2017.
 */

public class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.MyViewHolder> {

    List<String> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public HotNewsAdapter(Context context, List<String> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_hot_news, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        String current = data.get(position);
//        holder.txt_value.setText(current);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_comments;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
