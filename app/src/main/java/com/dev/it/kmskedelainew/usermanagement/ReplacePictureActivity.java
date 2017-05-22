package com.dev.it.kmskedelainew.usermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.TouchImageView;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

public class ReplacePictureActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_ID = "id";
    TouchImageView touchImageView;
    SessionManager sessionManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        VolleySingleton mVolleySingleton = VolleySingleton.getInstance();
        ImageLoader mImageLoader = mVolleySingleton.getProfileImageLoader();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeEdit);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        touchImageView = (TouchImageView) findViewById(R.id.imgDisplay);
        sessionManager = new SessionManager(getApplicationContext());
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream outStream = null;
        String filename = "avatar_user";

        File file = new File(extStorageDirectory,filename + ".png");
        if (file.exists()) {
            //ada file ?
            //Toast.makeText(getApplicationContext(),"Exist", Toast.LENGTH_LONG).show();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            touchImageView.setImageBitmap(myBitmap);
        }else{
            //cek di server
            //Toast.makeText(getApplicationContext(),"Not Exist", Toast.LENGTH_LONG).show();
            loadImages(mImageLoader,"http://kmskedelaiapp.web.id/kms-admin/assets/avataruser/avatar_user_"+ String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID))+".png");
        }
    }

    private void loadImages(ImageLoader mImageLoader, String urlThumbnail) {
        if (!urlThumbnail.equals("NA")) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    touchImageView.setImageBitmap(response.getBitmap());
                    savebitmap("avatar_user", response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        }
    }

    private void savebitmap(String filename, Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream outStream = null;
        //Log.e("bitmap", bitmap.toString());
        File file = new File(extStorageDirectory,filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                if (!StateKMS.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }else {
                    touchImageView.setImageBitmap(bitmap);
                    uploadImage("avatar_user", bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String filename, final Bitmap bitmap){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLEndpoints.CHANGE_PICTURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        if(s.equals("success")){
                            Toast.makeText(getApplicationContext(), "Sucessfully Uploded" , Toast.LENGTH_LONG).show();
                            savebitmap(filename, bitmap);
                            /*Intent in = new Intent(getApplicationContext(), ProfileUserActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(in);*/
                        }else{
                            Toast.makeText(getApplicationContext(), "Error Uploading ... " , Toast.LENGTH_LONG).show();
                        }
                        //Log.d("er", s);
                        //Showing toast message of the response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Log.d("er", volleyError.toString());
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Uploading" , Toast.LENGTH_LONG).show();
                        //Showing toast
//                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = "avatar_user_" + String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID));

                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put(KEY_ID, String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID)));

                //returning parameters
                return params;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rep_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_replace) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        VolleySingleton mVolleySingleton = VolleySingleton.getInstance();
        ImageLoader mImageLoader = mVolleySingleton.getImageLoader();

        loadImages(mImageLoader,"http://kmskedelaiapp.web.id/kms-admin/assets/avataruser/avatar_user_"+ String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID))+".png");
    }
}
