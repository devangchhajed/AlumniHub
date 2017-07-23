package zuts.bit.connect.Activities.HomeActivity.FragmentOne;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.ImageCompressor;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class PostUpdate extends AppCompatActivity {

    ImageButton close, gallery, camera;
    ImageView userdp, statusImage;
    SessionManager sessionManager;
    HashMap<String, String> userdetails;
    VolleyController volleyController;
    ProgressDialog pDialog;
    EditText status;
    Button post;
    File file;
    Uri uri;


    ImageCompressor imageCompressor;

    private Bitmap bitmap;

    public boolean imageSelected;

    private int PICK_IMAGE_REQUEST = 1;


    private static final String TAG = PostUpdate.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);

        imageSelected=false;

        imageCompressor=new ImageCompressor(this);

        close= (ImageButton) findViewById(R.id.post_update_close);
        gallery= (ImageButton) findViewById(R.id.post_update_gallery);
        userdp= (ImageView) findViewById(R.id.post_update_user_dp);
        statusImage= (ImageView) findViewById(R.id.post_update_image);
        camera= (ImageButton) findViewById(R.id.post_update_camera);
        post= (Button) findViewById(R.id.postcomment);
        status= (EditText) findViewById(R.id.post_update_status);
        sessionManager=new SessionManager(getApplicationContext());
        userdetails=sessionManager.getUserDetails();
        volleyController=new VolleyController();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Glide.with(getApplicationContext()).load(userdetails.get(sessionManager.KEY_PHOTO_URL))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(userdp);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status.getText().toString().isEmpty() || statusImage.getDrawable()!=null)
                    postUpdate(status.getText().toString());
                    else
                    Toast.makeText(PostUpdate.this,"Can't post a blank update",Toast.LENGTH_SHORT).show();

            }
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


    private void postUpdate(final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Saving ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.POST_FEED_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Post Update Response: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("success")) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String smessage = jObj.getString("message");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator= getLayoutInflater();
                        View layout=inflator.inflate(R.layout.custom_noification, null);
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Status Updated");
                        Toast toast= new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent=new Intent();
                        setResult(1,intent);
                        finish();//finishing activity


                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Log.e(TAG,jObj.getString("message"));
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"PostUpdateonErrorResponse"+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Converting Bitmap to String
                String image = "null";
                if(imageSelected)
                {
                    image = getStringImage(bitmap);
                }

                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userdetails.get(sessionManager.KEY_USERID));
                params.put("status", status);
                params.put("image", image);
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

    @Override
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
                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    statusImage.setImageBitmap(bitmap);
                    statusImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageSelected=true;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                //Setting the Bitmap to ImageView
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Log.e(TAG,"pah : "+getRealPathFromURI(getImageUri(this,bitmap)));
                String cpath=imageCompressor.compressImage(Uri.parse(getRealPathFromURI(getImageUri(this,bitmap))));
                Log.e(TAG,"pah : "+cpath);
                File imgFile = new  File(cpath);
                if(imgFile.exists()){
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    statusImage.setImageBitmap(bitmap);
//                    statusImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageSelected=true;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
