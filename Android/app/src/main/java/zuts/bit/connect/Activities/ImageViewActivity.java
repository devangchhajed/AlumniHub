package zuts.bit.connect.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.TouchImageView;
import zuts.bit.connect.helper.VolleyController;

public class ImageViewActivity extends AppCompatActivity {


    private TouchImageView image;
    private DecimalFormat df;
    TextView username, timestamp, status;
    ImageView close;
    String uriSting;
ProgressDialog pDialog;
    String imgurl = null;
    String uname = null, time = null, stat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Bundle extras = getIntent().getExtras();


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (extras != null) {
            imgurl = extras.getString("imgurl");
            uname = extras.getString("username");
            time = extras.getString("timestamp");
            stat = extras.getString("status");
            Log.e("ImageDisplay", imgurl + uname + time + stat);
            // and get whatever type user account id is
        }
        //
        // DecimalFormat rounds to 2 decimal places.
        //
        df = new DecimalFormat("#.##");
        image = (TouchImageView) findViewById(R.id.img);
        username = (TextView) findViewById(R.id.image_view_name);
        status = (TextView) findViewById(R.id.image_view_status);
        timestamp = (TextView) findViewById(R.id.image_view_timestamp);
        close = (ImageView) findViewById(R.id.image_view_close);

        username.setText(uname);
        status.setText(stat);
        timestamp.setText(time);


        try {
//        image.setImageURI(Uri.parse(String.valueOf(image)));
            Glide.with(getApplicationContext()).load(imgurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            image.setImageDrawable(resource);
                        }
                    });
            image.setScaleType(TouchImageView.ScaleType.FIT_CENTER);

        } catch (Exception e) {
            Log.e("Image Activity", e + "");
        }
        //
        // Set the OnTouchImageViewListener which updates edit texts
        // with zoom and scroll diagnostics.
        //

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                shareImage();
                return true;
            case R.id.save:
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(String.valueOf(1));
                return true;
            case R.id.report:
                reportImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareImage() {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(String.valueOf(2));

    }

    public void reportImage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setPadding(16,16,16,16);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText("Report");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        final EditText et = new EditText(this);
        TextView tv1 = new TextView(this);
        tv1.setText("Reason to Report");

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(tv1,tv1Params);
        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle("Report");
        // alertDialogBuilder.setMessage("Input Student ID");
        alertDialogBuilder.setCustomTitle(tv);

        // Setting Negative "Cancel" Button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // Setting Positive "OK" Button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                submitReport(imgurl,uname, stat, new SessionManager(getApplicationContext()).getUserDetails().get(new SessionManager(getApplicationContext()).KEY_USERID), et.getText().toString());


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }
    }


    private void submitReport(final String imgurl, final String uname, final String status, final String uid, final String ureport){

        pDialog.setMessage("Reporting Image...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.REGISTER_IMAGE_REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ImageViewActivity", "Reporting Response: " + response.toString());
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
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("We will get on it.");
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
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("imgurl", imgurl);
                params.put("uname", uname);
                params.put("status", status);
                params.put("uid", uid);
                params.put("ureport", ureport);

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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private int work;
        String filename;

        @Override
        protected String doInBackground(String... params) {

            work = Integer.parseInt(params[0]);

            Bitmap bitmap = null;
            OutputStream output;
            try {

                bitmap = Glide.
                        with(ImageViewActivity.this).
                        load(imgurl).
                        asBitmap().
                        into(-1, -1).
                        get();

                File file = new File(Environment.getExternalStorageDirectory().getPath(), "BITConnect/media/images/saved");
                if (!file.exists()) {
                    file.mkdirs();
                }
                filename = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

                output = new FileOutputStream(filename);

                // Compress into png format image from 0% - 100%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                Log.e("Image", filename);
                output.flush();
                output.close();

                resp = filename;
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {

            if (work == 1) {
                // execution of result of Long time consuming operation
                Toast.makeText(ImageViewActivity.this, "Photo Saved", Toast.LENGTH_SHORT).show();
            } else if (work == 2) {

                Uri imageUri = Uri.parse(filename);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, status.getText().toString() + " via BIT Connect");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));

            }


        }
    }
}
