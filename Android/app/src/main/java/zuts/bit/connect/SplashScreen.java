package zuts.bit.connect;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zuts.bit.connect.Activities.LoginActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.SessionManager;

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        final ImageView bgrotate= (ImageView) findViewById(R.id.splash_bg_rotate);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotation_animation);
        bgrotate.startAnimation(animation1);


        sessionManager=new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }else if (sessionManager.getUserDetails().get(sessionManager.KEY_BRANCH) == null || sessionManager.getUserDetails().get(sessionManager.KEY_SEMESTER) == null) {
            Toast.makeText(getApplicationContext(), "Please Complete Profile to continue.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }else{


            if(isAppIsInBackground(this))
            {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
            else{

            final Timer timer = new Timer();

                if(!sessionManager.isNetworkAvailable())
                    Toast.makeText(getApplicationContext(),"Not Connected to any Network.",Toast.LENGTH_LONG).show();


                timer.scheduleAtFixedRate(new TimerTask() {
                int i = 3;

                public void run() {
                    Log.e("", "run: " + i--);
                    if (i < 0) {
                        timer.cancel();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, 0, 1000);

            }

        }


    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            }
                        }
                    }
                }
            } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
                }
            }
        
        return isInBackground;
        }
}
