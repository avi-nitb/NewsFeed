package com.paulfy.adpter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.paulfy.CommentsActivity;
import com.paulfy.R;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.fragments.HomeTabFragment;
import com.paulfy.fragments.PopularTabFragment;
import com.paulfy.model.CategoryModel;
import com.paulfy.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.paulfy.application.MyApp.isImage;

public class PopularTab_Adapter extends RecyclerView.Adapter<PopularTab_Adapter.MyViewHolder> {
    Fragment context;
    List<NewsModel.Data> data;
    List<NewsModel.Data.Comments> commentsList;
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
        holder.txt_date.setText(current.getNews_upload_time());
        holder.txt_likes.setText(String.valueOf(current.getLikeCount()));
        holder.txt_comments.setText("" + current.getComment().size());
        holder.txt_description.setText(current.getTitle());

        String url = extractUrl(current.getDescription());
        if (url.isEmpty()) {
            holder.rl_img.setVisibility(View.GONE);
        } else {
            holder.rl_img.setVisibility(View.VISIBLE);
            Picasso.with(context.getActivity())
                    .load(url)
                    .resize(imgWidth, imgHeight)
                    .error(R.drawable.road_bg)
                    .placeholder(R.drawable.road_bg).centerCrop()
                    .into(holder.img);

        }

//        25 April 2018 | 9:15 am
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = 0;
        try {
            time = sdf.parse(current.getNews_upload_time().replace("| ", "")).getTime();


            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            holder.txt_date.setText(ago.toString());
            Log.d("my time to show", ago.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            txt_comments.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                try {
                    ((PopularTabFragment) context).clickNews(data.get(getLayoutPosition()));
                } catch (Exception e) {

                    ((HomeTabFragment) context).clickNews(data.get(getLayoutPosition()));
                }
                //MyApp.popMessage("Details", data.get(getLayoutPosition()).getDescription(), context);
            } else if (v == txt_likes) {

                if (data.get(getLayoutPosition()).isLike()) {
                    if (MyApp.getApplication().readUser().getId() == 0) {
                        MyApp.popMessage("Alert!", "You are not valid user, please register with Paulfy app first then try again.\nThank you.", context.getActivity());
                        return;
                    }
//                    MyApp.showMassage(context.getActivity(),"Already liked");
                    data.get(getLayoutPosition()).setLikeCount(data.get(getLayoutPosition()).getLikeCount() - 1);
                    data.get(getLayoutPosition()).setLike(false);
                    notifyDataSetChanged();
                    try {
                        ((PopularTabFragment) context).likedPosition = getLayoutPosition();
                        ((PopularTabFragment) context).isLikeStatus = false;
                        RequestParams p = new RequestParams();
                        p.put("news_id", data.get(getLayoutPosition()).getId());
                        p.put("user_id", MyApp.getApplication().readUser().getId());
                        p.put("like", 0);
                        ((PopularTabFragment) context).postCall(context.getActivity(), AppConstants.BASE_URL + "likeNews", p, "Loading...", 4);
                    } catch (Exception e) {
                        ((HomeTabFragment) context).likedPosition = getLayoutPosition();
                        ((HomeTabFragment) context).isLikeStatus = false;
                        RequestParams p = new RequestParams();
                        p.put("news_id", data.get(getLayoutPosition()).getId());
                        p.put("user_id", MyApp.getApplication().readUser().getId());
                        p.put("like", 0);
                        ((HomeTabFragment) context).postCall(context.getActivity(), AppConstants.BASE_URL + "likeNews", p, "Loading...", 4);
                    }
                } else {

                    if (MyApp.getApplication().readUser().getId() == 0) {
                        MyApp.popMessage("Alert!", "You are not valid user, please register with Paulfy app first then try again.\nThank you.", context.getActivity());
                        return;
                    }

                    data.get(getLayoutPosition()).setLikeCount(data.get(getLayoutPosition()).getLikeCount() + 1);
                    data.get(getLayoutPosition()).setLike(true);
                    notifyDataSetChanged();
                    try {
                        ((HomeTabFragment) context).likedPosition = getLayoutPosition();
                        ((HomeTabFragment) context).isLikeStatus = true;
                        RequestParams p = new RequestParams();
                        p.put("news_id", data.get(getLayoutPosition()).getId());
                        p.put("user_id", MyApp.getApplication().readUser().getId());
                        p.put("like", 1);
                        ((HomeTabFragment) context).postCall(context.getActivity(), AppConstants.BASE_URL + "likeNews", p, "Loading...", 4);
                    } catch (Exception w) {
                        ((PopularTabFragment) context).likedPosition = getLayoutPosition();
                        ((PopularTabFragment) context).isLikeStatus = true;
                        RequestParams p = new RequestParams();
                        p.put("news_id", data.get(getLayoutPosition()).getId());
                        p.put("user_id", MyApp.getApplication().readUser().getId());
                        p.put("like", 1);
                        ((PopularTabFragment) context).postCall(context.getActivity(), AppConstants.BASE_URL + "likeNews", p, "Loading...", 4);
                    }

                    notifyDataSetChanged();
                }
            } else if (v == overflow) {
                @SuppressLint("RestrictedApi") ContextThemeWrapper ctw = new ContextThemeWrapper(context.getActivity(), R.style.CustomPopupTheme);

                PopupMenu popup = new PopupMenu(context.getActivity(), overflow);
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

                        if (item.getItemId() == R.id.nav_save) {
                            try {
                                ((HomeTabFragment) context).callSaveApi(data.get(getLayoutPosition()).getId());
                            } catch (Exception e) {
                                ((PopularTabFragment) context).callSaveApi(data.get(getLayoutPosition()).getId());
                            }
                        } else if (item.getItemId() == R.id.nav_hide) {
                            MyApp.showMassage(context.getActivity(), "Will update in next build, backend work is under process");
                        } else if (item.getItemId() == R.id.nav_report) {
                            MyApp.showMassage(context.getActivity(), "Will update in next build, backend work is under process");
                        }

                        return true;
                    }
                });

                popup.show();
            } else if (v == txt_comments) {
                if (MyApp.getApplication().readUser().getId() == 0) {
                    MyApp.popMessage("Alert!", "You are not valid user, please register with Paulfy app first then try again.\nThank you.", context.getActivity());
                    return;
                }
                commentsList = new ArrayList<>();
                commentsList.addAll(data.get(getLayoutPosition()).getComment());
                Intent i = new Intent(context.getActivity(), CommentsActivity.class);
                i.putExtra("news_id", data.get(getLayoutPosition()).getId());
                i.putExtra("comments", (Serializable) commentsList);
                context.startActivity(i);
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

    public void setFilter(List<NewsModel.Data> countryModels) {
        data = new ArrayList<>();
        data.addAll(countryModels);
        notifyDataSetChanged();
    }
}
