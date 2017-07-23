package zuts.bit.connect.Activities.HomeActivity.FragmentFour;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class FeedbackActivity extends AppCompatActivity {

    Button next, email;
    EditText textview;
    ImageButton close;
    ProgressDialog pDialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        next = (Button) findViewById(R.id.feedback_next);
        textview= (EditText) findViewById(R.id.feedback_editText);
        next= (Button) findViewById(R.id.feedback_next);
        close= (ImageButton) findViewById(R.id.feedback_close);
        email= (Button) findViewById(R.id.feedback_email);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        sessionManager=new SessionManager(getApplicationContext());

        final String review=textview.getText().toString();

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: bitconnect@outlook.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BIT Connect Feedback");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                startActivity(Intent.createChooser(emailIntent, "BIT Connect Feedback"));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUpdate(sessionManager.getUserDetails().get(sessionManager.KEY_USERID),textview.getText().toString());
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        finish();    }
        });

    }

    private void submitUpdate(final String id, final String review){

        pDialog.setMessage("Submitting Review ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.USER_FEEDBACK_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("FeedbackActivity", "Feedback Response: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String ssuccess = jObj.getString("success");
                        String smessage = jObj.getString("message");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator= getLayoutInflater();
                        View layout=inflator.inflate(R.layout.custom_noification, null);
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Feedback Submitted");
                        Toast toast= new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                        finish();
                        

                    } else {
                        // Error occurred in Feedback. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), "Error from server : "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"JSON Exception : "+e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Feedback", "Feedback Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("feedback", review);


                return params;
            }
        };

        // queue.add(strReq);
//        volleyController.getInstance().addToRequestQueue(strReq);
//        Volley.newRequestQueue(getApplicationContext()).add(strReq);
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(strReq);

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
