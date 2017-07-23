package zuts.bit.connect.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.ImageCompressor;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class EditProfilePic extends AppCompatActivity {
    ImageView imageView;
    ImageButton buttonCamera, buttonGallery , buttonClose;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent ;
    Bitmap bitmap;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    private ProgressDialog pDialog;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ImageCompressor imageCompressor;

    private static final String TAG = EditProfilePic.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_pic);
        imageView = (ImageView)findViewById(R.id.edit_profile_pic_imageview);
        buttonCamera = (ImageButton) findViewById(R.id.edit_profile_pic_button2);
        buttonGallery = (ImageButton) findViewById(R.id.edit_profile_pic_button1);
        buttonClose = (ImageButton) findViewById(R.id.edit_profile_close);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imageCompressor=new ImageCompressor(this);

        sessionManager=new SessionManager(getApplicationContext());
        user=sessionManager.getUserDetails();

        EnableRuntimePermission();

        Glide.with(getApplicationContext()).load(user.get(sessionManager.KEY_PHOTO_URL))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        buttonClose.getDrawable().setAlpha(255);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finishAct();

            }
        });
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClickImageFromCamera() ;

            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromGallery();

            }
        });

    }



    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }


    public void uploadImage(){
        String tag_string_req = "req_register";

        final String imagestring=getStringImage(bitmap);

        pDialog.setMessage("Saving ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_USER_IMG_URL, new Response.Listener<String>() {

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
                        String ssuccess = jObj.getString("success");
                        String smessage = jObj.getString("message");
                        String imgurl = jObj.getString("imgurl");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        LayoutInflater inflator= getLayoutInflater();
                        View layout=inflator.inflate(R.layout.custom_noification, null);
                        ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Photo Updated");
                        Toast toast= new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();

                        sessionManager.updateLoginSession(Integer.parseInt(user.get(sessionManager.KEY_USERID)),user.get(sessionManager.KEY_NAME),user.get(sessionManager.KEY_EMAIL),user.get(sessionManager.KEY_BRANCH),user.get(sessionManager.KEY_SEMESTER),imgurl);


                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");

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
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", user.get(sessionManager.KEY_USERID));
                params.put("img", imagestring);


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

    public void onBackPressed(){

    finishAct();
        super.onBackPressed();

    }

    public void finishAct(){
        Intent intent=new Intent();
        setResult(1,intent);
        finish();//finishing activity
    }


    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
//            CropIntent.putExtra("outputX", 512);
//            CropIntent.putExtra("outputY", 512);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                bitmap = bundle.getParcelable("data");

                Log.e(TAG,"pah : "+getRealPathFromURI(getImageUri(this,bitmap)));
                String cpath=imageCompressor.compressImage(Uri.parse(getRealPathFromURI(getImageUri(this,bitmap))));
                Log.e(TAG,"pah : "+cpath);
                File imgFile = new  File(cpath);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    imageView.setImageBitmap(myBitmap);
                }

                uploadImage();

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


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfilePic.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(EditProfilePic.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditProfilePic.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfilePic.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {

            Toast.makeText(EditProfilePic.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditProfilePic.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfilePic.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(EditProfilePic.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditProfilePic.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e(TAG,"Permission Granted, Now your application can access CAMERA.");

                } else {

                    Toast.makeText(EditProfilePic.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}