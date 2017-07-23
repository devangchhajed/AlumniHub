package zuts.bit.connect.Activities.HomeActivity.FragmentTwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.adapter.ListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data.ListItem;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.uploadActivity.ResourceUploadActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;


public class TwoFragment extends Fragment{

    String TAG="TwoFragment";
    private List<ListItem> feedItems;
    ListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    Button upload;
    TextView tv;
    ProgressBar pBar;
    ListView listview;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_two, container, false);

        feedItems = new ArrayList<ListItem>();

        pBar= (ProgressBar) view.findViewById(R.id.f2progress);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.fragment_two_swipe_refresh);
        tv= (TextView) view.findViewById(R.id.emptyList);

        listview= (ListView) view.findViewById(R.id.subjectlistview);
        listAdapter = new ListAdapter(getActivity(), feedItems,0);
        listview.setAdapter(listAdapter);
        upload= (Button) view.findViewById(R.id.paperupload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), ResourceUploadActivity.class);
                getActivity().startActivity(i);
            }
        });

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


        final SessionManager sessionManager=new SessionManager(getActivity().getApplicationContext());
        final HashMap<String, String> user=sessionManager.getUserDetails();

            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.FETCH_SUBJECT_LIST, new Response.Listener<String>() {

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
                    params.put("branch", user.get(sessionManager.KEY_BRANCH).toString());
                    params.put("semester", user.get(sessionManager.KEY_SEMESTER).toString());


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

                ListItem item = new ListItem();
                item.setId(feedObj.getInt("id"));
                item.setSubjectcode(feedObj.getString("subjectcode"));
                item.setSubjectName(feedObj.getString("subjectname"));

                feedItems.add(item);
            }

            if (feedItems.size()<=0){
                tv.setVisibility(View.VISIBLE);
            }
            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
