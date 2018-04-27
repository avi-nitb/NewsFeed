package com.paulfy.adpter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.paulfy.R;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.fragments.HomeTabFragment;
import com.paulfy.fragments.PopularTabFragment;
import com.paulfy.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.paulfy.application.MyApp.getApplication;
import static com.paulfy.application.MyApp.isImage;

public class PopularTab_Adapter extends RecyclerView.Adapter<PopularTab_Adapter.MyViewHolder> {
    Fragment context;
    List<NewsModel.Data> data;
    float displayMatrix;
    int imgWidth;
    int imgHeight;

    public PopularTab_Adapter(Fragment context, List<NewsModel.Data> data) {
        this.context = context;
        this.data = data;

        this.displayMatrix = MyApp.getDisplayMatrix(context.getActivity());
        imgWidth = (int) (100 * displayMatrix);
        imgHeight = (int) (100 * displayMatrix);
    }

    @Override
    public PopularTab_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getActivity()).inflate(R.layout.item_log, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularTab_Adapter.MyViewHolder holder, int position) {

        NewsModel.Data current = data.get(position);
        holder.txt_date.setText(current.getCreated_at());
        holder.txt_likes.setText(String.valueOf(current.getLikes().size()));
        holder.txt_comments.setText("" + current.getComment().size());
        holder.txt_description.setText(current.getTitle());

        String url = extractUrl(current.getDescription());
        if(url.isEmpty()){
            holder.rl_img.setVisibility(View.GONE);
        }else{
            holder.rl_img.setVisibility(View.VISIBLE);
            Picasso.with(context.getActivity())
                    .load(url)
                    .resize(imgWidth,imgHeight)
                    .error(R.drawable.road_bg)
                    .placeholder(R.drawable.road_bg).centerCrop()
                    .into(holder.img);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_comments, txt_date, txt_likes, txt_description;
        ImageButton overflow;
        ImageView img;
        RelativeLayout rl_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_comments = itemView.findViewById(R.id.comments_counts);
            txt_likes = itemView.findViewById(R.id.likescount);
            txt_date = itemView.findViewById(R.id.txt_date);
            overflow = itemView.findViewById(R.id.overflow);
            txt_description = itemView.findViewById(R.id.txt_description);
            img = itemView.findViewById(R.id.img);
            rl_img = itemView.findViewById(R.id.rl_img);
            txt_comments.setOnClickListener(this);
            overflow.setOnClickListener(this);
            itemView.setOnClickListener(this);
            txt_likes.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                try {
                    ((PopularTabFragment) context).clickNews(data.get(getLayoutPosition()));
                } catch (Exception e){

                    ((HomeTabFragment) context).clickNews(data.get(getLayoutPosition()));
                }
                //MyApp.popMessage("Details", data.get(getLayoutPosition()).getDescription(), context);
            } else if (v== txt_likes) {
                int j = data.get(getLayoutPosition()).getLikes().size();
                List<Integer> likedUser = new ArrayList<>();
                for (int i = 0; i < j; i++) {
                    if (data.get(getLayoutPosition()).getLikes().get(i).getId() == MyApp.getApplication().readUser().getId()) {
                        likedUser.add((Integer) data.get(getLayoutPosition()).getLikes().get(i).getId());
                    }
                }
                if (likedUser.contains(MyApp.getApplication().readUser().getId())) {
                    RequestParams p = new RequestParams();
                    p.put("news_id", data.get(getLayoutPosition()).getId());
                    p.put("user_id", MyApp.getApplication().readUser().getId());
                    p.put("like", 0);
                    ((PopularTabFragment) context).postCall(getApplication(), AppConstants.BASE_URL + "likeNews", p, "", 4);
                } else {
                    RequestParams p = new RequestParams();
                    p.put("news_id", data.get(getLayoutPosition()).getId());
                    p.put("user_id", MyApp.getApplication().readUser().getId());
                    p.put("like", 1);
                    ((PopularTabFragment) context).postCall(getApplication(), AppConstants.BASE_URL + "likeNews", p, "", 4);
                    notifyDataSetChanged();
                }
            }

        }
    }

    public String extractUrl(String text) {
        String containedUrls = "";
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            String url = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
            if (isImage(url)) {
                return url;
            }

        }

        return containedUrls;
    }
}
