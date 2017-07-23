package zuts.bit.connect.Activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zuts.bit.connect.MainActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class ProfileActivity extends AppCompatActivity {

    TextView name, email;
    Spinner branch, semester;
    SessionManager sessionManager;
    HashMap<String, String> userdetails;
    ImageView imgProfilePic;
    Button save;
    private ProgressDialog pDialog;
    VolleyController volleyController;

    RequestQueue queue;

    Toolbar toolbar;

    private ArrayAdapter<String> adapter, adapter1;


    String[] branchArray = {"BCA", "BBA", "BE-CSE", "BE-ECE", "BE-EEE", "MBA", "IMBA", "MCA", "IMCA", "BAM", "MSc"};
    String[] semesterArray = {"1", "2", "3","4", "5","6", "7","8", "9","10"};

    private static final String TAG = ProfileActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Profile");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#A92815"));
        }

        setContentView(R.layout.activity_profile);
        name= (TextView) findViewById(R.id.txtName);
        email= (TextView) findViewById(R.id.txtEmail);
        branch= (Spinner) findViewById(R.id.txtBranch);
        semester= (Spinner) findViewById(R.id.txtSemester);
        imgProfilePic= (ImageView) findViewById(R.id.imgProfilePicDisp);
        save= (Button) findViewById(R.id.profile_save);

        volleyController=new VolleyController();

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, branchArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);

        adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, semesterArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter1);

        sessionManager=new SessionManager(getApplicationContext());
        userdetails=sessionManager.getUserDetails();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final HashMap<String,String> userdetails=sessionManager.getUserDetails();
        name.setText(userdetails.get(sessionManager.KEY_NAME));
        email.setText(userdetails.get(sessionManager.KEY_EMAIL));
        if (userdetails.get(sessionManager.KEY_BRANCH) != null) {
            String a = userdetails.get(sessionManager.KEY_BRANCH);
            int spinnerposition = adapter.getPosition(a);
            branch.setSelection(spinnerposition);
        }

        if (userdetails.get(sessionManager.KEY_SEMESTER) != null) {
            String a = userdetails.get(sessionManager.KEY_SEMESTER);
            int spinnerposition = adapter1.getPosition(a);
            semester.setSelection(spinnerposition);
        }

        Log.e(TAG,"FCM ID - - ");
        Log.e(TAG,"FCM ID : "+FirebaseInstanceId.getInstance().getToken());
        Log.e(TAG,"FCM Session ID : "+sessionManager.getUserDetails().get(sessionManager.KEY_FCM_ID));

        Glide.with(getApplicationContext()).load(userdetails.get(sessionManager.KEY_PHOTO_URL))
                .thumbnail(0.5f)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(userdetails.get(sessionManager.KEY_NAME),userdetails.get(sessionManager.KEY_EMAIL),branch.getSelectedItem().toString(),semester.getSelectedItem().toString(),userdetails.get(sessionManager.KEY_PHOTO_URL));
            }
        });

//        Picasso.with(ProfileActivity.this).load(userdetails.get(sessionManager.KEY_PHOTO_URL)).into(imgProfilePic);
    }

    private void registerUser(final String mname, final String memail, final String mbranch, final String msemester, final String mimgurl) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Saving ...");
        showDialog();
        queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.REGISER_NEW_USER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String smessage = jObj.getString("message");
                        String suserid = jObj.getString("user_id");
                        String suserimg = jObj.getString("user_img");
                        String sfcmid = jObj.getString("fcm_id");

                        Log.e("Login", suserimg+" - "+sfcmid);

//                        if(sessionManager.isLoggedIn() && userdetails.get(sessionManager.KEY_BRANCH)!=null){
//
//                            try {
//                                FirebaseMessaging.getInstance().unsubscribeFromTopic(userdetails.get(sessionManager.KEY_BRANCH).toString());
//                                FirebaseMessaging.getInstance().unsubscribeFromTopic(userdetails.get(sessionManager.KEY_SEMESTER).toString());
//                                FirebaseMessaging.getInstance().unsubscribeFromTopic(userdetails.get(sessionManager.KEY_BRANCH).toString() + "" + userdetails.get(sessionManager.KEY_SEMESTER).toString());
//                                Log.d(TAG, "Unsubscribed to topic " + FirebaseInstanceId.getInstance().getToken());
//                            }catch (Exception e)
//                            {
//                                Log.e(TAG, e.toString());
//                            }
//                        }
//
                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator= getLayoutInflater();
                        View layout=inflator.inflate(R.layout.custom_noification, null);
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Records Updated");
                        Toast toast= new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                            sessionManager.updateLoginSession(Integer.parseInt(suserid),mname,memail,branch.getSelectedItem().toString(),semester.getSelectedItem().toString(),suserimg);
//                        FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_BRANCH).toString());
//                        FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_SEMESTER).toString());
//                        FirebaseMessaging.getInstance().subscribeToTopic(userdetails.get(sessionManager.KEY_BRANCH).toString()+""+userdetails.get(sessionManager.KEY_SEMESTER).toString());
//                        Log.d(TAG, "Subscribed to news topic "+ FirebaseInstanceId.getInstance().getToken());


                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage()+" - "+sessionManager.getUserDetails().get(sessionManager.KEY_FCM_ID));
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mname);
                params.put("email", memail);
                params.put("branch", branch.getSelectedItem().toString());
                params.put("semester", semester.getSelectedItem().toString());
                params.put("fcmid", FirebaseInstanceId.getInstance().getToken());

                return params;
            }
        };

       // queue.add(strReq);
//        volleyController.getInstance().addToRequestQueue(strReq);
//        Volley.newRequestQueue(getApplicationContext()).add(strReq);
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
        pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
        pDialog.dismiss();
    }



}
