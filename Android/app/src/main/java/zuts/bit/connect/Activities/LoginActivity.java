package zuts.bit.connect.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    SessionManager sessionManager;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    SignInButton signInButton;

    public String userid, username, useremail, userphotourl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_login);


        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        sessionManager=new SessionManager(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    
            //This function will option signing intent
            private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        
        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    
            @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            Log.e(TAG,result+"");
            handleSignInResult(result);
            }
        }
    
            
            //After the signing we are calling this function
            private void handleSignInResult(GoogleSignInResult result) {
                Log.e(TAG,"Result : "+ result.toString());
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            
            //Displaying name and email
            sessionManager.createLoginSession(acct.getDisplayName(),acct.getEmail(), AppConfig.DEFAULT_USER_PIC);
            Log.d(TAG,"Account Created");
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
            finish();
            //Initializing image loader

            } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    
            @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
            }
        }

    @Override
    public void onConnectionFailed(@NonNull com.google.android.gms.common.ConnectionResult connectionResult) {

    }

//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//        }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//
//
//
//            Log.e(TAG, "display url: " + acct.getPhotoUrl());
//
//
//            String personName = acct.getDisplayName();
//            String personPhotoUrl;
//
////            if(acct.getPhotoUrl()==null)
////                personPhotoUrl= AppConfig.DEFAULT_USER_PIC;
////            else
////                personPhotoUrl = acct.getPhotoUrl().toString();
//
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email);
//
////            userphotourl=personPhotoUrl;
//            username=personName;
//            useremail=email;
//
//            updateUI(true);
//            } else {
//            // Signed out, show unauthenticated UI.
//            updateUI(false);
//            }
//        }
//
//    @Override
//    public void onClick(View v) {
//        int id=v.getId();
//        switch(id){
//            case R.id.sign_in_button:
//                signIn();
//                break;
//        }
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//            @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//            } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently. Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }
//
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Loading..");
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
//
//    private void updateUI(boolean isSignedIn) {
//        if (isSignedIn) {
//            sessionManager.createLoginSession(username,useremail, AppConfig.DEFAULT_USER_PIC);
//            Log.d(TAG,"Account Created");
//            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
//            startActivity(i);
//            finish();
//            } else {
//            Log.e(TAG,"Account Not Created");
//            }
//    }
}
