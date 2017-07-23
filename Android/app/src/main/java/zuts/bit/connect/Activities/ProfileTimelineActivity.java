package zuts.bit.connect.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import zuts.bit.connect.Activities.HomeActivity.FragmentOne.PostUpdate;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter.EndlessScrollListener;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter.FeedListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class ProfileTimelineActivity extends AppCompatActivity {
    private static final String TAG = "ProfileFragment";
    private ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private static final int PROFILE_UPDATE_SUCCESS=1;

    private String dname, dimage, ddetail;
    int duserid;

    EndlessScrollListener scrollListener;
Toolbar toolbar;
    SessionManager sessionManager;
    HashMap<String, String> userdetails;
    ImageView close;
    TextView details;
    TextView username;
    ImageView userimg;

    private Boolean isPrivateUser=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_timeline);

        sessionManager=new SessionManager(this);
        userdetails=sessionManager.getUserDetails();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            duserid = extras.getInt("userid");
            if((Integer.parseInt(userdetails.get(sessionManager.KEY_USERID))) != duserid)
                isPrivateUser=false;
            //The key argument here must match that used in the other activity
        }else{
            isPrivateUser=true;
            duserid = Integer.parseInt(userdetails.get(sessionManager.KEY_USERID));
            dname = userdetails.get(sessionManager.KEY_NAME);
            ddetail=userdetails.get(sessionManager.KEY_BRANCH)+" - "+userdetails.get(sessionManager.KEY_BRANCH);
            dimage=userdetails.get(sessionManager.KEY_PHOTO_URL);
        }
        Log.e(TAG,"du : "+duserid);


        close= (ImageView) findViewById(R.id.profile_timeline_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.profile_timeline_list);


        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems,false);
        listView.setAdapter(listAdapter);

        LayoutInflater inflater=getLayoutInflater();
        ViewGroup footer= (ViewGroup) inflater.inflate(R.layout.profile_post_header,listView,false);
        userimg= (ImageView) footer.findViewById(R.id.profile_header_image);
        username= (TextView) footer.findViewById(R.id.profile_head_name);
        details = (TextView) footer.findViewById(R.id.profile_head_details);
        Button detail= (Button) footer.findViewById(R.id.profile_head_editdetails);


        Glide.with(getApplicationContext()).load(dimage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userimg);
        username.setText(dname);
        details.setText(ddetail);

        if(isPrivateUser) {
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileTimelineActivity.this, ProfileActivity.class);
                    startActivityForResult(intent, PROFILE_UPDATE_SUCCESS);

                }
            });
            userimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileTimelineActivity.this, EditProfilePic.class);
                    startActivityForResult(intent, PROFILE_UPDATE_SUCCESS);
                }
            });
        }else{
            detail.setVisibility(View.INVISIBLE);
            userimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(ProfileTimelineActivity.this,ImageViewActivity.class);
                    i.putExtra("imgurl", dimage+"");
                    i.putExtra("status", ddetail+"");
                    i.putExtra("username", dname+"");
                    i.putExtra("timestamp", "");
                    startActivity(i);
                }
            });
        }

        listView.addHeaderView(footer,null,false);


        scrollListener=new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.e(TAG,"Need More Posts : "+page+totalItemsCount);
                initiateFeedCall(page-1);
                return true;
            }
        };



        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.profile_timeline_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFeedRefresh();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initiateFeedCall(0);
            }
        });


//        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
//        Cache.Entry entry = cache.get(URL_FEED);
//        if (entry != null) {
//            // fetch the data from cache
//            try {
//                String data = new String(entry.data, "UTF-8");
//                try {
//                    parseJsonFeed(new JSONObject(data));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        }

        listView.setOnScrollListener(scrollListener);

        // Inflate the layout for this fragment

    }

    public void updateListHeader(){
        Glide.with(getApplicationContext()).load(userdetails.get(sessionManager.KEY_PHOTO_URL))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userimg);
        username.setText(userdetails.get(sessionManager.KEY_NAME));
        details.setText(userdetails.get(sessionManager.KEY_BRANCH)+" - "+userdetails.get(sessionManager.KEY_SEMESTER));
    }

    public void onFeedRefresh(){
        feedItems.clear();
        listAdapter.notifyDataSetChanged();
        scrollListener.resetState();
        initiateFeedCall(0);
        listView.smoothScrollToPosition(0);
    }
    private void initiateFeedCall(final int offset){


        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
//        getActionBar().setIcon(
//                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        final SessionManager sessionManager=new SessionManager(this);
        // We first check for cached request
        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(AppConfig.USER_FEED_URL+"/"+offset);
        if (entry != null && !sessionManager.isNetworkAvailable()) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data),offset);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.USER_FEED_URL+"/"+offset, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    int oopage=offset;
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e(TAG,response.toString());
                        try {
                            parseJsonFeed(new JSONObject(response), oopage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(duserid));


                    return params;
                }
            };

            // Adding request to volley request queue
            VolleyController.getInstance().addToRequestQueue(strReq);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response, int offset) {
        try {

            if(offset==0)
            {
                JSONArray feedArray = response.getJSONArray("user");
                JSONObject feedObj = (JSONObject) feedArray.get(0);
                dimage=feedObj.getString("image");
                dname=feedObj.getString("name");
                ddetail=feedObj.getString("branch")+" - "+feedObj.getString("semester");
                username.setText(dname);
                details.setText(ddetail);
                Glide.with(getApplicationContext()).load(dimage)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(userimg);

                Log.e(TAG,feedObj.getString("name"));
                Log.e(TAG,feedObj.getString("semester"));
                Log.e(TAG,feedObj.getString("branch"));
                Log.e(TAG,feedObj.getString("image"));


            }

            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setUserId(feedObj.getInt("userid"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);


                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }
    }



    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==PROFILE_UPDATE_SUCCESS)
        {
            updateListHeader();
        }
    }

}
