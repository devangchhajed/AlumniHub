package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.subjectmatterfragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.PaperResourceActivity;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.adapter.PaperListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data.PaperListItem;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.VolleyController;

/**
 * A simple {@link Fragment} subclass.
 */
public class Papers extends Fragment {

    String TAG="TwoFragment";
    private List<PaperListItem> feedItems;
    PaperListAdapter listAdapter;
    int sid;
    String scode, sname;
    ImageButton close;
    ListView listview;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    ProgressBar pBar;

    public Papers(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_papers, container, false);
        Bundle bundle=getActivity().getIntent().getExtras();
        if(bundle!=null)
        {
            sid=bundle.getInt("subjectid");
            scode=bundle.getString("subjectcode");
            sname=bundle.getString("subjectname");
        }


        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.pr_swipe_refresh);
        feedItems = new ArrayList<PaperListItem>();
        pBar= (ProgressBar) view.findViewById(R.id.paperProgressBar);

        listview= (ListView) view.findViewById(R.id.prlistview);
        listAdapter = new PaperListAdapter(getActivity(), feedItems,1);
        listview.setAdapter(listAdapter);

        Log.e(TAG,"Initiate calling");

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
        listview.smoothScrollToPosition(0);
    }


    private void initiateFeedCall(){



        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.FETCH_PAPER_LIST, new Response.Listener<String>() {

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
                params.put("scode", scode);
                return params;
            }
        };

        // Adding request to volley request queue
        VolleyController.getInstance().addToRequestQueue(strReq);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                PaperListItem item = new PaperListItem();
                item.setId(feedObj.getInt("p_id"));
                item.setYear(feedObj.getString("year"));
                item.setExam(feedObj.getString("exam"));
                item.setUrl(feedObj.getString("url"));
                item.setUName(feedObj.getString("u_name"));
                item.setSub(sname+" - "+scode);


                feedItems.add(item);
            }

            pBar.setVisibility(View.GONE);
            if (feedItems.size()<0){
                TextView tv= (TextView) view.findViewById(R.id.emptyList);
                tv.setVisibility(View.VISIBLE);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
