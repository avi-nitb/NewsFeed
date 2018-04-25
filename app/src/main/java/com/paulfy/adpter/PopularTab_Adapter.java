package com.paulfy.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulfy.R;
import com.paulfy.application.MyApp;
import com.paulfy.fragments.PopularTabFragment;
import com.paulfy.model.NewsModel;
import com.paulfy.model.RssModel;

import java.util.List;

public class PopularTab_Adapter extends RecyclerView.Adapter<PopularTab_Adapter.MyViewHolder>
{
    Context context;
    List<NewsModel.Data> data;

    public PopularTab_Adapter(Context context, List<NewsModel.Data> data)
    {
        this.context = context;
        this.data=data;
    }

    @Override
    public PopularTab_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_log, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularTab_Adapter.MyViewHolder holder, int position)
    {

        NewsModel.Data current=data.get(position);
        holder.txt_date.setText(current.getCreated_at());
        holder.txt_cat.setText(String.valueOf(current.getLikes().size()));
        holder.txt_comments.setText(""+current.getComment().size());
        holder.txt_description.setText(current.getTitle());


    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txt_comments, txt_date, txt_cat, txt_description;
        ImageButton overflow;
        ImageView img;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            txt_comments = itemView.findViewById(R.id.comments_counts);
            txt_cat = itemView.findViewById(R.id.likescount);
            txt_date = itemView.findViewById(R.id.txt_date);
            overflow = itemView.findViewById(R.id.overflow);
            txt_description = itemView.findViewById(R.id.txt_description);
            img = itemView.findViewById(R.id.img);
            txt_comments.setOnClickListener(this);
            overflow.setOnClickListener(this);
            txt_description.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if(v==txt_description){
               MyApp.popMessage("Details", data.get(getLayoutPosition()).getDescription(), context);
            }

        }
    }
}
