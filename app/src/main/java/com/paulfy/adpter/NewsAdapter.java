package com.paulfy.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulfy.CommentsActivity;
import com.paulfy.R;
import com.paulfy.application.MyApp;
import com.paulfy.model.RssModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Created by Abhishek on 22-04-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    List<RssModel> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NewsAdapter(Context context, List<RssModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_log, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (data.size()>0) {
            RssModel current = data.get(position);
            holder.txt_date.setText(current.getPubDate().replace("+0000", ""));
            holder.txt_cat.setText(current.getCategoryTags());
            holder.txt_description.setText(current.getTitle());
        }
//        if (current.isHasImage()) {
//            holder.img.setVisibility(View.VISIBLE);
//        } else {
//            holder.img.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {

            return data.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_comments, txt_date, txt_cat, txt_description;
        ImageButton overflow;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_comments = itemView.findViewById(R.id.comments_counts);
//            txt_likes = itemView.findViewById(R.id.txt_likes);
            txt_date = itemView.findViewById(R.id.txt_date);
            overflow = itemView.findViewById(R.id.overflow);
            txt_description = itemView.findViewById(R.id.txt_description);
            img = itemView.findViewById(R.id.img);
            txt_comments.setOnClickListener(this);
            overflow.setOnClickListener(this);
            txt_description.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == txt_comments) {
                context.startActivity(new Intent(context, CommentsActivity.class));
            } else if (v == overflow) {
                //Creating the instance of PopupMenu
                @SuppressLint("RestrictedApi") ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.CustomPopupTheme);

                PopupMenu popup = new PopupMenu(context, overflow);
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
                        .inflate(R.menu.news_overflow, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        return true;
                    }
                });

                popup.show();
            } else if (v == txt_description) {
                MyApp.popMessage("Details", data.get(getLayoutPosition()).getDescription(), context);
            }
        }
    }
}
