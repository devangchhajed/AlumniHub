package zuts.bit.connect;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zuts.bit.connect.Activities.EditProfilePic;
import zuts.bit.connect.Activities.HomeActivity.FragmentFour.FourFragment;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.OneFragment;
import zuts.bit.connect.Activities.HomeActivity.FragmentThree.ThreeFragment;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.TwoFragment;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;
import zuts.bit.connect.helper.firebase.utils.NotificationUtils;

import static zuts.bit.connect.Activities.EditProfilePic.RequestPermissionCode;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OneFragment oneFragment;
    Boolean forceupdate = false;
    private HashMap<String, String> userdetails;

    SessionManager sessionManager;
    private String TAG=MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


        setContentView(R.layout.activity_main);


        sessionManager = new SessionManager(getApplicationContext());
        userdetails=sessionManager.getUserDetails();

        checkUpdate();

        oneFragment = new OneFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        displayFirebaseRegId();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);
//                    FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_BRANCH).toString());
//                    FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_SEMESTER).toString());
//                    FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_BRANCH).toString()+""+userdetails.get(sessionManager.KEY_SEMESTER).toString());
                    Log.d(TAG, "Subscribed to news topic");
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };


        EnableRuntimePermission();

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.PREF_NAME, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + FirebaseInstanceId.getInstance().getToken());

        if (!TextUtils.isEmpty(regId))
            Log.e(TAG,"Firebase Reg Id: " + regId);
        else
            Log.e(TAG,"Firebase Reg Id is not received yet!");
    }

    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_news,
                R.drawable.ic_tab_search,
                R.drawable.ic_tab_favourite,
                R.drawable.ic_tab_contacts
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(oneFragment, "ONE");
        adapter.addFrag(new TwoFragment(), "TWO");
        adapter.addFrag(new ThreeFragment(), "THREE");
        adapter.addFrag(new FourFragment(), "FOUR");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA}, RequestPermissionCode);

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(this, "We don't collect any Personal Data without your permission. Please grant permissions from settings.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void checkUpdate() {
        final String TAG = "App Updater";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.UPDATE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                int versioncode = BuildConfig.VERSION_CODE;
                if (response != null) {
                    Log.e(TAG, response.toString() + versioncode);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int version = jsonObject.getInt("version");
                        String newfeature = jsonObject.getString("newfeatures");
                        forceupdate = jsonObject.getBoolean("forceupdate");
                        Log.e(TAG, response.toString() + versioncode + version);

                        if (versioncode < version) {

                            if (forceupdate) {
                             Intent i=new Intent(MainActivity.this,ForceUpdate.class);
                                i.putExtra("response",response);
                                startActivity(i);
                                finish();
                            } else {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("App Update");

                            // Setting Dialog Message
                            alertDialog.setMessage("New App Update Found\n" + newfeature);

                            // Setting Icon to Dialog
                            //alertDialog.setIcon(R.drawable.delete);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id=zuts.bit.connect"));
                                    startActivity(intent);
                                }
                            });


                                alertDialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                            alertDialog.show();
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("App Updater", "" + e);
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
