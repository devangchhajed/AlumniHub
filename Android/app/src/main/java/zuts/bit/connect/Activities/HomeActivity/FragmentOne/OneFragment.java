package zuts.bit.connect.Activities.HomeActivity.FragmentOne;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter.EndlessScrollListener;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter.FeedListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class OneFragment extends Fragment {
    View view;
    private static final String TAG = "OneFragment";
    private FloatingActionButton fab;
    private ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    ImageView fhicon;
    TextView fhtext;
   // private String URL_FEED ="http://192.168.1.11/BITConnect/demo2.json";
    //private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

    ProgressBar pBar;
    SessionManager sessionManager;

    private static final int STATUS_UPDATE_SUCCESS=1;

   private String URL_FEED =AppConfig.NEWS_FEED_URL;
    EndlessScrollListener scrollListener;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        sessionManager=new SessionManager(getActivity().getApplicationContext());

        feedItems = new ArrayList<FeedItem>();

        pBar= (ProgressBar) view.findViewById(R.id.postprogressbar);
        listAdapter = new FeedListAdapter(getActivity(), feedItems, true);
        listView.setAdapter(listAdapter);


        ViewGroup footer= (ViewGroup) inflater.inflate(R.layout.newsfeed_header,listView,false);

        fhicon= (ImageView) footer.findViewById(R.id.nh_icon);
        fhtext= (TextView) footer.findViewById(R.id.nh_textview);

        String name=sessionManager.getUserDetails().get(sessionManager.KEY_NAME);
        name=name.substring(0,name.indexOf(" "));
        fhtext.setText("Welcome "+name+" to BIT Connect");

        listView.addHeaderView(footer,null,false);


        scrollListener=new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.e(TAG,"Need More Posts : "+page+totalItemsCount);
                initiateFeedCall(page-1);
                return true;
            }
        };



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_one_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFeedRefresh();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getWeather();
                initiateFeedCall(0);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab_post_update);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostUpdate.class);
                startActivityForResult(intent,STATUS_UPDATE_SUCCESS);
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
        return view;

    }

    public void onFeedRefresh(){
        feedItems.clear();
        scrollListener.resetState();
        initiateFeedCall(0);
        listView.smoothScrollToPosition(0);
    }

    private void getWeather(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hours = df.format(c.getTime());
        // formattedDate have current date/time

        String name=sessionManager.getUserDetails().get(sessionManager.KEY_NAME);
        name=name.substring(0,name.indexOf(" "));

        if(Integer.parseInt(hours)>5 && Integer.parseInt(hours)<12)
            fhtext.setText("Good Morning "+name+", Welcome to BIT Connect");
        else
        if(Integer.parseInt(hours)>12 && Integer.parseInt(hours)<16)
            fhtext.setText("Good Afternoon "+name+", Welcome to BIT Connect");
        else
        if(Integer.parseInt(hours)>16 && Integer.parseInt(hours)<20)
            fhtext.setText("Good Evening "+name+", Welcome to BIT Connect");
        else
            fhtext.setText("Hello "+name+", Welcome to BIT Connect");


        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(AppConfig.WEATHER_AT_BIT);
        if (entry != null && !sessionManager.isNetworkAvailable()) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.WEATHER_AT_BIT, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e(TAG,response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("weather");
                            JSONObject jOb=jsonArray.getJSONObject(0);


                            JSONObject jOb1=jsonObject.getJSONObject("main");

                            String temp=jOb1.getString("temp").toString();

                            Calendar c = Calendar.getInstance();
                            System.out.println("Current time => "+c.getTime());

                            SimpleDateFormat df = new SimpleDateFormat("HH");
                            String hours = df.format(c.getTime());
                            // formattedDate have current date/time


                            String name=sessionManager.getUserDetails().get(sessionManager.KEY_NAME);
                            name=name.substring(0,name.indexOf(" "));

                            if(Integer.parseInt(hours)>5 && Integer.parseInt(hours)<12)
                                fhtext.setText("Good Morning "+name+", Its "+temp+"° at BIT");
                            else
                            if(Integer.parseInt(hours)>12 && Integer.parseInt(hours)<16)
                                fhtext.setText("Good Afternoon "+name+", Its "+temp+"° at BIT");
                            else
                            if(Integer.parseInt(hours)>16 && Integer.parseInt(hours)<20)
                                fhtext.setText("Good Evening "+name+", Its "+temp+"° at BIT");
                            else
                                fhtext.setText("Hello "+name+", Its "+temp+"° at BIT");



                            Glide.with(getActivity().getApplicationContext()).load(AppConfig.WEATHER_ICON+jOb.getString("icon")+".png")
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(fhicon);



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
            });

            // Adding request to volley request queue
            VolleyController.getInstance().addToRequestQueue(strReq);
        }

    }

    private void initiateFeedCall(int offset){


        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
//        getActionBar().setIcon(
//                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request
        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED+"/"+offset);
        if (entry != null && !sessionManager.isNetworkAvailable()) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest strReq = new StringRequest(Request.Method.POST, URL_FEED+"/"+offset, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e(TAG,response.toString());
                        try {
                            pBar.setVisibility(View.GONE);
                            parseJsonFeed(new JSONObject(response));
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
                    params.put("userid", sessionManager.getUserDetails().get(sessionManager.KEY_USERID));
                    params.put("branch", sessionManager.getUserDetails().get(sessionManager.KEY_BRANCH));
                    params.put("semester", sessionManager.getUserDetails().get(sessionManager.KEY_SEMESTER));
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
    private void parseJsonFeed(JSONObject response) {
        try {
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
                item.setLikes(feedObj.getInt("likes_count"));
                item.setComments(feedObj.getInt("comments_count"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));
                item.setUserLike(feedObj.getInt("userlike"));

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
        if(requestCode==STATUS_UPDATE_SUCCESS)
        {
            onFeedRefresh();
            Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
            cache.remove(URL_FEED+"/0");
            cache.clear();

            //        Toast.makeText(getActivity().getApplicationContext(),feedItems.get(0).getLikes()+"",Toast.LENGTH_SHORT).show();
        }
    }

}
