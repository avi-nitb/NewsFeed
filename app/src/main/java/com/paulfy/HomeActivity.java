package com.paulfy;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.paulfy.adpter.ViewPagerAdapter;
import com.paulfy.application.MyApp;
import com.paulfy.fragments.HomeFragment;
import com.paulfy.fragments.HotNewsFragment;
import com.paulfy.fragments.ProfileFragment;
import com.paulfy.model.RssModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class HomeActivity extends CustomActivity
        implements NavigationView.OnNavigationItemSelectedListener, CustomActivity.ResponseCallback {


    private ImageButton btn_home;
    private ImageButton btn_profile;
    private ImageButton btn_hot;
    private ImageButton btn_new;
    private ImageButton btn_chat;

    FragmentManager fragmentManager;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setResponseListener(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setupUiElements();
        Fragment homefragment=new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, homefragment)
                .commit();

        Bundle bundle=new Bundle();
        bundle.putString("selectedSearchItem","News");
        homefragment.setArguments(bundle);

//        new FetchFeedTask().execute((Void) null);
//        getCall(getContext(), "https://www.vanguardngr.com/news/feed/", "Loading...", 1);

    }

    private void setupUiElements() {
        btn_chat = findViewById(R.id.btn_chat);
        btn_home = findViewById(R.id.btn_home);
        btn_hot = findViewById(R.id.btn_hot);
        btn_new = findViewById(R.id.btn_new);
        btn_profile = findViewById(R.id.btn_profile);

        setTouchNClick(R.id.btn_chat);
        setTouchNClick(R.id.btn_home);
        setTouchNClick(R.id.btn_hot);
        setTouchNClick(R.id.btn_new);
        setTouchNClick(R.id.btn_profile);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btn_chat) {
            toolbar.setVisibility(View.VISIBLE);
            btn_chat.setImageResource(R.drawable.chat_black);
            btn_profile.setImageResource(R.drawable.profile_inactive);
            btn_new.setImageResource(R.drawable.new_inactive);
            btn_home.setImageResource(R.drawable.home_inactive);
            btn_hot.setImageResource(R.drawable.hot_inactive);
        } else if (v == btn_home) {
            Fragment homefragment=new HomeFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, homefragment)
                    .commit();
            btn_chat.setImageResource(R.drawable.chat_gray);
            btn_profile.setImageResource(R.drawable.profile_inactive);
            btn_new.setImageResource(R.drawable.new_inactive);
            btn_home.setImageResource(R.drawable.home_active);
            btn_hot.setImageResource(R.drawable.hot_inactive);

            Bundle bundle=new Bundle();
            bundle.putString("selectedSearchItem","News");
            homefragment.setArguments(bundle);

        } else if (v == btn_hot) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new HotNewsFragment())
                    .commit();
            btn_chat.setImageResource(R.drawable.chat_gray);
            btn_profile.setImageResource(R.drawable.profile_inactive);
            btn_new.setImageResource(R.drawable.new_inactive);
            btn_home.setImageResource(R.drawable.home_inactive);
            btn_hot.setImageResource(R.drawable.hot_active);
        } else if (v == btn_new) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
            btn_chat.setImageResource(R.drawable.chat_gray);
            btn_profile.setImageResource(R.drawable.profile_inactive);
            btn_new.setImageResource(R.drawable.new_active);
            btn_home.setImageResource(R.drawable.home_inactive);
            btn_hot.setImageResource(R.drawable.hot_inactive);
        } else if (v == btn_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new ProfileFragment())
                    .commit();
            btn_chat.setImageResource(R.drawable.chat_gray);
            btn_profile.setImageResource(R.drawable.profile_active);
            btn_new.setImageResource(R.drawable.new_inactive);
            btn_home.setImageResource(R.drawable.home_inactive);
            btn_hot.setImageResource(R.drawable.hot_inactive);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Category");

        final AutoCompleteTextView searchAutoCompleteTextView = searchView
                .findViewById(R.id.search_src_text);
        searchAutoCompleteTextView.setThreshold(0);
        searchAutoCompleteTextView.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                searchAutoCompleteTextView.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        searchAutoCompleteTextView.setLayoutParams(params);
        searchAutoCompleteTextView.setPadding(0, 0, 0, 0);

        searchAutoCompleteTextView.setDropDownHorizontalOffset(-100);
        searchAutoCompleteTextView.setPadding(0, 0, 0, 0);
        searchAutoCompleteTextView.setLeft(0);
        final String[] array = {"News", "Tech", "Business", "Sports", "Entertainment", "Startup", "Education"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.search_item, array);
        searchAutoCompleteTextView.setAdapter(adapter);
        searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==5 || position==6){
                    MyApp.showMassage(getContext(),"Coming Soon");
                }else {

                    String selectedItem = parent.getAdapter().getItem(position).toString();

                    searchAutoCompleteTextView.setText(selectedItem);

                    Fragment homefragment = new HomeFragment();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, homefragment)
                            .commit();
                    btn_chat.setImageResource(R.drawable.chat_gray);
                    btn_profile.setImageResource(R.drawable.profile_inactive);
                    btn_new.setImageResource(R.drawable.new_inactive);
                    btn_home.setImageResource(R.drawable.home_active);
                    btn_hot.setImageResource(R.drawable.hot_inactive);

                    Bundle bundle = new Bundle();
                    bundle.putString("selectedSearchItem", selectedItem);
                    homefragment.setArguments(bundle);
                }

                  //  MyApp.showMassage(getContext(), "Coming soon...");

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String s) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business) {
            Fragment homefragment=new HomeFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, homefragment)
                    .commit();
            Bundle bundle=new Bundle();
            bundle.putString("selectedSearchItem", "Business");
            homefragment.setArguments(bundle);
        } else if (id == R.id.nav_finance) {

        } else if (id == R.id.nav_sports) {

            Fragment homefragment=new HomeFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, homefragment)
                    .commit();
            Bundle bundle=new Bundle();
            bundle.putString("selectedSearchItem", "Sports");
            homefragment.setArguments(bundle);

        } else if (id == R.id.nav_entertainment) {

            Fragment homefragment=new HomeFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, homefragment)
                    .commit();
            Bundle bundle=new Bundle();
            bundle.putString("selectedSearchItem", "Entertainment");
            homefragment.setArguments(bundle);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Context getContext() {
        return HomeActivity.this;
    }

    private void rssToJson(String sampleXml) {
        JSONObject jsonObj = null;
        try {
            String xmlString;  // some XML String previously created
            XmlToJson xmlToJson = new XmlToJson.Builder(sampleXml).build();
            jsonObj = new JSONObject(xmlToJson.toJson().toString());
            JSONObject o = jsonObj.getJSONObject("rss");
            JSONObject channel = o.getJSONObject("channel");
            JSONArray arr = channel.getJSONArray("item");
            List<RssModel> dataList = new ArrayList<>();
            for (int i = 0; i <arr.length() ; i++) {
                JSONObject d = arr.getJSONObject(i);
                RssModel r = new RssModel();
                r.setCategoryTags("News");
                r.setCreater(d.optString("dc:creator"));
                r.setDescription(d.optString("description"));
                r.setHasImage(false);
                r.setImageLink("");
                r.setLink(d.optString("link"));
                r.setPubDate(d.optString("pubDate"));
                r.setTitle(d.optString("title"));
                dataList.add(r);
            }


        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

        Log.d("XML", sampleXml);

    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {

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
        rssToJson(error);
    }
}
