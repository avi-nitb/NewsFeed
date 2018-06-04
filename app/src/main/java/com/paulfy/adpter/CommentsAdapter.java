package com.paulfy.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulfy.R;
import com.paulfy.application.MyApp;
import com.paulfy.fragments.HomeTabFragment;
import com.paulfy.fragments.PopularTabFragment;
import com.paulfy.model.NewsModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_value, username;
        RelativeLayout rl_more, rl_reply, rl_upvote;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_value = itemView.findViewById(R.id.txt_comment);
            username = itemView.findViewById(R.id.username);
            rl_more = itemView.findViewById(R.id.rl_more);
            rl_reply = itemView.findViewById(R.id.rl_reply);
            rl_upvote = itemView.findViewById(R.id.rl_upvote);
            rl_more.setOnClickListener(this);
            rl_reply.setOnClickListener(this);
            rl_upvote.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == rl_more) {
                @SuppressLint("RestrictedApi") ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.CustomPopupTheme);

                PopupMenu popup = new PopupMenu(context, rl_more);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.comments_overflow, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.nav_save) {

                        } else if (item.getItemId() == R.id.nav_share) {

                        } else if (item.getItemId() == R.id.nav_report) {

                        }

                        return true;
                    }
                });

                popup.show();
            } else if (v == rl_upvote) {

            } else if (v == rl_reply) {

            }
        }
    }
}
