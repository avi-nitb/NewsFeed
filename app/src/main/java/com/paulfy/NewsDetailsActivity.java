package com.paulfy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.RequestParams;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;
import com.paulfy.application.SingleInstance;
import com.paulfy.model.NewsModel;
import com.paulfy.utils.AppTextView;
import com.paulfy.utils.DipPixelHelper;
import com.paulfy.utils.PicassoImageGetter;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

public class NewsDetailsActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private TextView txt_title, txt_cat_date;
    private AppTextView txt_description;
    private List<String> imgUrls = new ArrayList<>();
    private BannerSlider bannerSlider;
    private NewsModel.Data d;
    private TextView txt_visit;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("");


        d = SingleInstance.getInstance().getDataToLoad();

        imgUrls.addAll(extractUrls(d.getDescription()));
        txt_visit = findViewById(R.id.txt_visit);
        bannerSlider = findViewById(R.id.banner_slider1);
        txt_description = findViewById(R.id.txt_description);
        String htmlSpan = Html.fromHtml(d.getDescription()).toString();
        txt_description.setText(htmlSpan);

        setTouchNClick(R.id.txt_visit);

        if (d.getSource().isEmpty() || d.getSource().equalsIgnoreCase("No Source")) {
            txt_visit.setVisibility(View.GONE);
        }

        List<Banner> banners = new ArrayList<>();


        for (int i = 0; i < imgUrls.size(); i++) {
            String url = imgUrls.get(i);
            banners.add(new RemoteBanner(url));
        }
        bannerSlider.setBanners(banners);
        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        if (imgUrls.size() == 0) {
            bannerSlider.setVisibility(View.GONE);
        }


        txt_cat_date = findViewById(R.id.txt_cat_date);
        txt_title = findViewById(R.id.txt_title);

        txt_cat_date.setText(d.getNews_upload_time());
        txt_title.setText(d.getTitle());


//        25 April 2018 | 9:15 am
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:m a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = 0;
        try {
            time = sdf.parse(d.getNews_upload_time().replace("| ", "")).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            txt_cat_date.setText(ago.toString().replace("minutes","m")
                    .replace("ago","").replace("hours","h").replace("hour","h"));
            Log.d("my time to show", ago.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

//        PicassoImageGetter imageGetter = new PicassoImageGetter(txt_description);
//        Spannable html;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            html = (Spannable) Html.fromHtml(d.getDescription(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//        } else {
//            html = (Spannable) Html.fromHtml(d.getDescription(), imageGetter, null);
//        }
//        txt_description.setText(html);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark:
                RequestParams p = new RequestParams();
                p.put("news_id", d.getId());
                p.put("user_id", MyApp.getApplication().readUser().getId());
                postCall(this, AppConstants.BASE_URL + "bookmarkNews", p, "Saving...", 5);
                break;
            case R.id.action_twitter:
                shareTwitter(NewsDetailsActivity.this, d.getTitle(), d.getTitle_url(), "", "");
                break;
            case R.id.action_facebook:
                shareFacebook(NewsDetailsActivity.this, d.getTitle(), d.getTitle_url());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return (true);

    }

    public List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        try {
            Matcher urlMatcher = pattern.matcher(text);

            while (urlMatcher.find()) {
                String url = text.substring(urlMatcher.start(0),
                        urlMatcher.end(0));
                if (MyApp.isImage(url)) {
                    d.setDescription(d.getDescription().replace(url, ""));
                    containedUrls.add(url);
                }

            }
        } catch (Exception e) {
        }

        return containedUrls;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == txt_visit) {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(d.getTitle_url()));
//            startActivity(browserIntent);

                new FinestWebView.Builder(this)
                        .titleDefault(d.getTitle())
                        .toolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
                        .gradientDivider(false)
                        .dividerHeight(100)
                        .toolbarColorRes(R.color.colorPrimary)
                        .dividerColorRes(R.color.grey_hex_a3)
                        .dividerHeight(0)
                        .iconDefaultColorRes(R.color.white)
                        .titleColorRes(R.color.white)
                        .showIconClose(false)
                        .disableIconBack(false)
                        .iconDisabledColorRes(R.color.grey_hex_f0)
                        .iconPressedColorRes(R.color.white)
                        .progressBarHeight(DipPixelHelper.getPixel(this, 3))
                        .progressBarColorRes(R.color.colorAccent)
                        .backPressToClose(false)
                        .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                        .show(d.getTitle_url());
        }
    }




    private Context getContext() {
        return NewsDetailsActivity.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 5 && o.optInt("code") == 200) {
            MyApp.showMassage(this, "Saved successfully");
        } else {
            MyApp.showMassage(this, o.optString("message"));
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onTimeOutRetry(int callNumber) {
        MyApp.popMessage("Error", "Time out occur, please check your internet connection and try again.", getContext());
    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());
    }

    @Override
    public void onFeedReceived(String error) {

    }


    public void callSaveApi(int id) {
        RequestParams p = new RequestParams();
        p.put("news_id", id);
        p.put("user_id", MyApp.getApplication().readUser().getId());

        postCall(NewsDetailsActivity.this, AppConstants.BASE_URL + "bookmarkNews", p, "Saving...", 5);
    }

    public static void shareFacebook(Activity activity, String text, String url) {
        boolean facebookAppFound = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setComponent(name);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
    }

    /**
     * Share on Twitter. Using Twitter app if installed or web link otherwise.
     *
     * @param activity activity which launches the intent
     * @param text     text to share
     * @param url      url to share
     * @param via      twitter username without '@' who shares
     * @param hashtags hashtags for tweet without '#' and separated by ','
     */
    public static void shareTwitter(Activity activity, String text, String url, String via, String hashtags) {
        StringBuilder tweetUrl = new StringBuilder("https://twitter.com/intent/tweet?text=");
        tweetUrl.append(TextUtils.isEmpty(text) ? urlEncode(" ") : urlEncode(text));
        if (!TextUtils.isEmpty(url)) {
            tweetUrl.append("&url=");
            tweetUrl.append(urlEncode(url));
        }
        if (!TextUtils.isEmpty(via)) {
            tweetUrl.append("&via=");
            tweetUrl.append(urlEncode(via));
        }
        if (!TextUtils.isEmpty(hashtags)) {
            tweetUrl.append("&hastags=");
            tweetUrl.append(urlEncode(hashtags));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl.toString()));
        List<ResolveInfo> matches = activity.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }
        activity.startActivity(intent);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("wtf", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private static final String TAG = "";
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = txt_description.getText();
                txt_description.setText(t);
            }
        }
    }

//    @Override
//    public Drawable getDrawable(String source) {
//        LevelListDrawable d = new LevelListDrawable();
//        Drawable empty = getResources().getDrawable(R.drawable.logo_paulfy);
//        d.addLevel(0, 0, empty);
//        d.setBounds(0, 0, MyApp.getDisplayWidth(), empty.getIntrinsicHeight());
//
//        new LoadImage().execute(source, d);
//
//        return d;
//    }
}
