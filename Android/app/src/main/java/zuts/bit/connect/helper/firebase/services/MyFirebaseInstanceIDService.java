package zuts.bit.connect.helper.firebase.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

/**
 * Created by Devang on 1/8/2017.
 */

    public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
        private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    SessionManager sessionManager;
    HashMap<String, String> user;

        @Override
        public void onTokenRefresh() {
            super.onTokenRefresh();

            sessionManager=new SessionManager(getApplicationContext());
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            // Saving reg id to shared preferences
            storeRegIdInPref(refreshedToken);
            // sending reg id to your server
            sendRegistrationToServer(refreshedToken);
            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(AppConfig.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }

        private void sendRegistrationToServer(final String token) {

            user=sessionManager.getUserDetails();
            if(!(user.get(sessionManager.KEY_USERID) =="")) {
                StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_FCM_ID, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "FCM Update Response: " + response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "FCM Update Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", new SessionManager(getApplicationContext()).getUserDetails().get(new SessionManager(getApplicationContext()).KEY_USERID));
                        params.put("fcmid", token);

                        return params;
                    }
                };

                VolleyController.getInstance().addToRequestQueue(strReq);
            }
            // sending gcm token to server
            Log.e(TAG, "sendRegistrationToServer: " + token);
        }

        private void storeRegIdInPref(String token) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.PREF_NAME, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(sessionManager.KEY_FCM_ID, token);
            editor.commit();
        }
}
