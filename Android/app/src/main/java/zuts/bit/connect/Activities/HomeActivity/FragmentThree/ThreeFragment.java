package zuts.bit.connect.Activities.HomeActivity.FragmentThree;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.Activities.HomeActivity.FragmentThree.adapter.FeedListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentThree.data.FeedItem;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class ThreeFragment extends Fragment{

    private ListView notilist;
    private List<FeedItem> feedItems;
    FeedListAdapter listAdapter;
    String TAG="ThreeFragment";
    SwipeRefreshLayout swipeRefreshLayout;
SessionManager sessionManager;
    HashMap<String,String> user;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_three, container, false);
        notilist= (ListView) view.findViewById(R.id.notificationlist);

        sessionManager=new SessionManager(getActivity());
        user=sessionManager.getUserDetails();
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.fragment_three_swipe_refresh);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        notilist.setAdapter(listAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFeedRefresh();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initiateFeedCall();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    public void onFeedRefresh(){
        feedItems.clear();
        listAdapter.notifyDataSetChanged();
        initiateFeedCall();
        notilist.smoothScrollToPosition(0);
    }
    private void initiateFeedCall(){

        final SessionManager sessionManager=new SessionManager(getActivity().getApplicationContext());
        // We first check for cached request
        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(AppConfig.GET_NOTIFICATION_LIST);
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
            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.GET_NOTIFICATION_LIST, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e(TAG,response.toString());
                        try {
                            parseJsonFeed(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", user.get(sessionManager.KEY_USERID).toString());
                    params.put("branch", user.get(sessionManager.KEY_BRANCH).toString());
                    params.put("semester", user.get(sessionManager.KEY_SEMESTER).toString());


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
                item.setNid(feedObj.getInt("n_id"));
                item.setUserId(feedObj.getInt("userid"));
                item.setScope(feedObj.getInt("scope"));
                item.setTitle(feedObj.getString("title"));
                item.setBody(feedObj.getString("body"));
                item.setBranch(feedObj.getString("branch"));
                item.setSemester(feedObj.getString("semester"));
                item.setTime(feedObj.getString("created_at"));
                item.setUserImage(feedObj.getString("userpic"));
                item.setUserName(feedObj.getString("username"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImage(image);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

//            swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
  //          swipeRefreshLayout.setRefreshing(false);
        }
    }



}
