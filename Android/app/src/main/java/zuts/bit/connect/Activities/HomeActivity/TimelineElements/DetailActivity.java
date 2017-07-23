package zuts.bit.connect.Activities.HomeActivity.TimelineElements;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.Activities.Browser.BrowserActivity;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.FeedImageView.FeedImageView;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter.FeedListAdapter;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem;
import zuts.bit.connect.Activities.HomeActivity.TimelineElements.commentadapter.CommentListAdapter;
import zuts.bit.connect.Activities.HomeActivity.TimelineElements.commentitem.CommentItem;
import zuts.bit.connect.Activities.ImageViewActivity;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.BuildConfig;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class DetailActivity extends AppCompatActivity {


    ImageLoader imageLoader;
    Boolean postlike;
    int postid;
    SessionManager sessionManager;
    String TAG = DetailActivity.class.getSimpleName();
    TextView name;
    TextView timestamp;
    TextView statusMsg;
    TextView url;
    NetworkImageView profilePic;
    FeedImageView feedImageView;
    TextView likecount;
    TextView commentcount;
    RelativeLayout countlayout;
    Button like;
    ProgressBar pBar;
    SwipeRefreshLayout swipeRefreshLayout;
    zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem item;
    ListView commentlist;
    ImageButton close;
    TextView newcomment;
    Button postcomment;
    private List<CommentItem> feedItems;
    private CommentListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_notification);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            postid = extras.getInt("feed");
            // and get whatever type user account id is
        }
        Log.e(TAG, "Post id : " + postid);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fdn_swipe);
        sessionManager = new SessionManager(getApplicationContext());
        pBar = (ProgressBar) findViewById(R.id.fdn_progressbar);
        commentlist = (ListView) findViewById(R.id.dn_comments_list);
        close = (ImageButton) findViewById(R.id.detailactivityclose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newcomment= (TextView) findViewById(R.id.newcomment);
        postcomment= (Button) findViewById(R.id.postcomment);

        postcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
commentpost();

                Log.e(TAG, "postid : "+String.valueOf(postid));
                Log.e(TAG, "userid : "+ sessionManager.getUserDetails().get(sessionManager.KEY_USERID));
                Log.e(TAG, "comment : "+ newcomment.getText().toString());

            }
        });


        imageLoader = VolleyController.getInstance().getImageLoader();


        feedItems = new ArrayList<CommentItem>();
        listAdapter = new CommentListAdapter(DetailActivity.this, feedItems, postid);
        commentlist.setAdapter(listAdapter);

        LayoutInflater listinflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) listinflater.inflate(R.layout.detail_activity_header, commentlist, false);

        name = (TextView) footer.findViewById(R.id.name);
        timestamp = (TextView) footer.findViewById(R.id.timestamp);
        statusMsg = (TextView) footer.findViewById(R.id.txtStatusMsg);
        url = (TextView) footer.findViewById(R.id.txtUrl);
        profilePic = (NetworkImageView) footer.findViewById(R.id.profilePic);
        feedImageView = (FeedImageView) footer.findViewById(R.id.feedImage1);
        likecount = (TextView) footer.findViewById(R.id.dn_like_count);
        commentcount = (TextView) footer.findViewById(R.id.dn_comment_count);
        countlayout = (RelativeLayout) footer.findViewById(R.id.dn_countlayout);
        like = (Button) footer.findViewById(R.id.dn_like);


        commentlist.addHeaderView(footer, null, false);


        item = new zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFeedRefresh();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initiateFeedCall(postid);

            }
        });


    }

    public void onFeedRefresh() {
        feedItems.clear();
        initiateFeedCall(postid);
        commentlist.smoothScrollToPosition(0);
        listAdapter.notifyDataSetChanged();
    }

    public void likePost(final int postid, final int userid) {
        String tag_string_req = "likepost";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.LIKE_POST_FEED_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Feed", "Delete : " + response);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String ssuccess = jObj.getString("success");
                        String smessage = jObj.getString("message");

                        postlike = true;


                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("postid", String.valueOf(postid));
                params.put("userid", String.valueOf(userid));
                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    public void unlikePost(final int postid, final int userid) {
        String tag_string_req = "unlikepost";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UNLIKE_FEED_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Feed", "Delete : " + response);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String ssuccess = jObj.getString("success");
                        String smessage = jObj.getString("message");

                        postlike = false;


                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("postid", String.valueOf(postid));
                params.put("userid", String.valueOf(userid));
                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    private void initiateFeedCall(final int id) {

        // We first check for cached request
        Cache cache = VolleyController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(AppConfig.FULL_POST_FEED_URL);
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
            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.FULL_POST_FEED_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e(TAG, id + response.toString());
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
                    params.put("postid", String.valueOf(id));
                    return params;
                }
            };

            // Adding request to volley request queue
            VolleyController.getInstance().addToRequestQueue(strReq);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedObj = response.getJSONArray("post");
            JSONObject jfeedObj = feedObj.getJSONObject(0);
            item.setId(jfeedObj.getInt("id"));
            item.setUserId(jfeedObj.getInt("userid"));
            item.setName(jfeedObj.getString("name"));

            // Image might be null sometimes
            String image = jfeedObj.isNull("image") ? null : jfeedObj.getString("image");
            item.setImge(image);
            item.setStatus(jfeedObj.getString("status"));
            item.setLikes(jfeedObj.getInt("likes_count"));
            item.setComments(jfeedObj.getInt("comments_count"));
            item.setProfilePic(jfeedObj.getString("profilePic"));
            item.setTimeStamp(jfeedObj.getString("timeStamp"));
            item.setUserLike(jfeedObj.getInt("userlike"));

            // url might be null sometimes
            String feedUrl = jfeedObj.isNull("url") ? null : jfeedObj
                    .getString("url");

            item.setUrl(feedUrl);


            try {
                JSONArray feedArray = response.getJSONArray("comment");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj1 = (JSONObject) feedArray.get(i);

                    CommentItem item = new CommentItem();
                    item.setId(feedObj1.getInt("id"));
                    item.setUserid(feedObj1.getInt("userid"));
                    item.setUsername(feedObj1.getString("username"));
                    item.setUserimg(feedObj1.getString("userimg"));
                    item.setTimestamp(feedObj1.getString("timeStamp"));
                    item.setComment(feedObj1.getString("comment"));

                    // url might be null sometimes

                    feedItems.add(item);
                }

                listAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);

            if (item.getLikes() > 0 || item.getComments() > 0) {
                countlayout.setVisibility(View.VISIBLE);
                likecount.setText(item.getLikes() + " Likes");
                commentcount.setText(item.getComments() + " Comments");

            }

            if (item.getUserLike() == 1) {
                postlike = true;
                like.setText("Unlike");
            } else {
                postlike = false;
                like.setText("Like");
            }


            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!postlike) {
                        likecount.setText(item.getLikes() + 1 + " Likes");
                        like.setText("Unlike");
                        item.setLikes(item.getLikes() + 1);
                        likePost(item.getId(), Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID)));

                    } else if (postlike) {
                        likecount.setText(item.getLikes() - 1 + " Likes");
                        item.setLikes(item.getLikes() - 1);
                        like.setText("Like");
                        unlikePost(item.getId(), Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID)));

                    }

                }
            });


            name.setText(item.getName());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailActivity.this, ProfileTimelineActivity.class);
                    i.putExtra("userid", item.getUserId());
                    startActivity(i);
                }
            });

            Log.e("DetailActivity", "TIme : " + item.getTimeStamp());
//             Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(item.getTimeStamp()) * 1000L,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            timestamp.setText(timeAgo);

            // Chcek for empty status message
            if (!TextUtils.isEmpty(item.getStatus())) {
                statusMsg.setText(item.getStatus());
                statusMsg.setVisibility(View.VISIBLE);
            } else {
                // status is empty, remove from view
                statusMsg.setVisibility(View.GONE);
            }

            // Checking for null feed url
            if (item.getUrl() != null) {
                final String furl = item.getUrl();
                url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                        + item.getUrl() + "</a> "));

                // Making url clickable
                url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailActivity.this, BrowserActivity.class);
                        intent.putExtra("url", furl);
                        startActivity(intent);
                    }
                });
                url.setMovementMethod(LinkMovementMethod.getInstance());
                url.setVisibility(View.VISIBLE);
            } else {
                // url is null, remove from the view
                url.setVisibility(View.GONE);
            }


            // user profile pic
            profilePic.setImageUrl(item.getProfilePic(), imageLoader);
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailActivity.this, ProfileTimelineActivity.class);
                    i.putExtra("userid", item.getUserId());
                    startActivity(i);
                }
            });

            // Feed image
            if (item.getImge() != null) {
                feedImageView.setImageUrl(item.getImge(), imageLoader);
                feedImageView.setVisibility(View.VISIBLE);
                feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                    @Override
                    public void onError() {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
                feedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DetailActivity.this, ImageViewActivity.class);
                        i.putExtra("imgurl", item.getImge() + "");
                        i.putExtra("status", item.getStatus() + "");
                        i.putExtra("username", item.getName() + "");
                        i.putExtra("timestamp", DateUtils.getRelativeTimeSpanString(Long.parseLong(item.getTimeStamp()) * 1000L, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
                        startActivity(i);
                    }
                });

            } else {
                feedImageView.setVisibility(View.GONE);
            }


            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void commentpost()
    {
        final String tag_string_req = "deletepost";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMENT_POST_FEED_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Feed","Delete : "+response);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator= getLayoutInflater();
                        View layout=inflator.inflate(R.layout.custom_noification, null);
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Comment Posted");
                        Toast toast= new Toast(DetailActivity.this);
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                        newcomment.setText("");
                        // Check if no view has focus:
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        onFeedRefresh();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(DetailActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag_string_req,error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("postid", String.valueOf(postid));
                params.put("userid", sessionManager.getUserDetails().get(sessionManager.KEY_USERID));
                params.put("comment", newcomment.getText().toString());
                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
