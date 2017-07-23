package zuts.bit.connect.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.util.HashMap;

import zuts.bit.connect.Activities.LoginActivity;

/**
 * Created by Devang on 12/30/2016.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int MODE=0;

    // Sharedpref file name
    public static final String KEY_PHOTO_URL = "photourl";

    public static final String KEY_USERID = "userid";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_BRANCH = "branch";
    public static final String KEY_SEMESTER = "semester";

    public static final String KEY_FCM_ID="fcmid";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public SessionManager(Context context)
    {
        this._context=context;
        pref=_context.getSharedPreferences(AppConfig.PREF_NAME,MODE);
        editor=pref.edit();
    }

    public void createLoginSession(String name, String email, String photourl){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHOTO_URL, photourl);

        editor.commit();

    }
    public void updateLoginSession(int id,String name, String email, String branch, String semester, String photourl){
        editor.putBoolean(IS_LOGIN,true);
        editor.putInt(KEY_USERID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_BRANCH, branch);
        editor.putString(KEY_SEMESTER, semester);
        editor.putString(KEY_PHOTO_URL, photourl);

        editor.commit();

    }
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            // Staring Login Activity
            _context.startActivity(i);
            }
        
        }

    public void storeFcmId(String token) {
        editor.putString(KEY_FCM_ID, token);
        Log.e("SessionManager", "Token : "+token);
        editor.commit();
    }
            
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERID,String.valueOf(pref.getInt(KEY_USERID,0)));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_BRANCH, pref.getString(KEY_BRANCH, null));
        user.put(KEY_SEMESTER, pref.getString(KEY_SEMESTER, null));
        user.put(KEY_PHOTO_URL, pref.getString(KEY_PHOTO_URL, null));
        user.put(KEY_FCM_ID, pref.getString(KEY_FCM_ID, null));

        // return user
        return user;
        }

    /**
     * Quick check for login
     * **/
            // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(AppConfig.BASE_DOMAIN); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

}
