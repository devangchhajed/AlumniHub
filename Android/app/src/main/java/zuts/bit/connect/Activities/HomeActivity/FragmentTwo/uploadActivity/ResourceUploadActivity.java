package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.uploadActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.ImageCompressor;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class ResourceUploadActivity extends AppCompatActivity {

    Spinner abranch, asemester, aexam, ayear, asubject;
    String[] branchArray = {"BCA", "BBA", "BE-CSE", "BE-ECE", "BE-EEE", "MBA", "IMBA", "MCA", "IMCA", "BAM", "MSc"};
    String[] semesterArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] examArray = {"Mid Semester", "End Semester"};

    ImageButton close, gallery, camera;
    Button post;
    ImageView image;
    File file;
    Uri uri;
    Bitmap bitmap;
    String TAG = ResourceUploadActivity.class.getName();
    ProgressDialog pDialog;
    ArrayAdapter adapter4;
    List<String> sub=new ArrayList<String>();
    ImageCompressor imageCompressor;
    ArrayList<String> years;
    private int PICK_IMAGE_REQUEST = 1;

    private List<SubjectListModel> feedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_upload);

        close = (ImageButton) findViewById(R.id.ru_close);
        image = (ImageView) findViewById(R.id.ru_image);
        gallery = (ImageButton) findViewById(R.id.ru_gallery);
        camera = (ImageButton) findViewById(R.id.ru_camera);
        post = (Button) findViewById(R.id.ru_post_button);


        imageCompressor=new ImageCompressor(ResourceUploadActivity.this);
        years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 2000; i--) {
            years.add(Integer.toString(i));
        }

        pDialog= new ProgressDialog(ResourceUploadActivity.this);
        pDialog.setCancelable(true);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showAlertBox();    }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                file = new File(Environment.getExternalStorageDirectory(),
                        "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                uri = Uri.fromFile(file);

                CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

                CamIntent.putExtra("return-data", true);

                startActivityForResult(CamIntent, 0);

            }
        });


    }


    private void initiateFeedCall() {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.FETCH_SUBJECT_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    Log.e(TAG, response.toString());
                    try {
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
                params.put("branch", abranch.getSelectedItem().toString());
                params.put("semester", asemester.getSelectedItem().toString());

                return params;
            }
        };

        // Adding request to volley request queue
        VolleyController.getInstance().addToRequestQueue(strReq);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                SubjectListModel item = new SubjectListModel(feedObj.getInt("id"), feedObj.getString("subjectcode"), feedObj.getString("subjectname"));

                feedItems.add(item);
            }
            returnList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void returnList() {
        sub.clear();
        for (int i = 0; i < feedItems.size(); i++) {
            sub.add(feedItems.get(i).getSubcode() + " - " + feedItems.get(i).getName());
        }
        adapter4.notifyDataSetChanged();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {



            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Log.e(TAG,"pah : "+getRealPathFromURI(getImageUri(this,bitmap)));
                String cpath=imageCompressor.compressImage(Uri.parse(getRealPathFromURI(getImageUri(this,bitmap))));
                Log.e(TAG,"pah : "+cpath);
                File imgFile = new  File(cpath);
                if(imgFile.exists()){
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                //Setting the Bitmap to ImageView
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Log.e(TAG,"path : "+getRealPathFromURI(getImageUri(this,bitmap)));
                String cpath=imageCompressor.compressImage(Uri.parse(getRealPathFromURI(getImageUri(this,bitmap))));
                Log.e(TAG,"path : "+cpath);
                File imgFile = new  File(cpath);
                if(imgFile.exists()){
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    image.setImageBitmap(bitmap);
//                    statusImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void uploadImage() {
        Log.e(TAG,"Upload Image Called");
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.UPLOAD_PAPER, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.e(TAG,response.toString());
                loading.cancel();
                VolleyLog.d(TAG, "Response: " + response.toString());
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator = getLayoutInflater();
                        View layout = inflator.inflate(R.layout.custom_noification, null);
                        ((TextView) layout.findViewById(R.id.custom_noti_text)).setText("Paper Updated");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error : "+response, Toast.LENGTH_LONG).show();
                        Log.e(TAG,response.toString());
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG,e.toString());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.cancel();
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String exm = aexam.getSelectedItem().toString();
                String yer = ayear.getSelectedItem().toString();
                String uid = new SessionManager(ResourceUploadActivity.this).getUserDetails().get(new SessionManager(ResourceUploadActivity.this).KEY_USERID);
                String sid = feedItems.get(asubject.getSelectedItemPosition()).getSubcode();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("year", yer);
                params.put("exam", exm);
                params.put("uid", uid);
                params.put("subjectcode", String.valueOf(sid));
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        new VolleyController().getInstance().addToRequestQueue(stringRequest);
    }

    public void showDialog(){
        if(!pDialog.isShowing())
        {
            pDialog.show();
        }
    }

    public void  hideDialog(){
        if(pDialog.isShowing())
        {
            pDialog.hide();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void showAlertBox(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setPadding(16,16,16,16);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText("Upload Paper");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        final EditText et = new EditText(this);
        TextView tv1 = new TextView(this);
        tv1.setText("Reason to Report");


        ayear = new Spinner(this);
        abranch = new Spinner(this);
        asemester = new Spinner(this);
        aexam = new Spinner(this);
        asubject = new Spinner(this);

//        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        tv1Params.bottomMargin = 5;
//        layout.addView(tv1,tv1Params);
//        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(ayear, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(aexam, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(abranch, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(asemester, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(asubject, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, years);
        ayear.setAdapter(adapter);

        ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, branchArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        abranch.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, semesterArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asemester.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, examArray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aexam.setAdapter(adapter3);

        feedItems = new ArrayList<SubjectListModel>();

        adapter4=new ArrayAdapter(this,R.layout.spinner_item,sub);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asubject.setAdapter(adapter4);


        abranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initiateFeedCall();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        asemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initiateFeedCall();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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
        alertDialogBuilder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                uploadImage();

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

}
