package com.dev.it.kmskedelainew.usermanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.pkmmte.view.CircularImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ProfileUserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    TextView nama, alamat, profesi;
    CircularImageView circularImageView;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    SessionManager sessionManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*String s_nama = getIntent().getStringExtra("nama");
        String s_alamat = getIntent().getStringExtra("alamat");
        String s_profesi = getIntent().getStringExtra("pekerjaan");
        String s_avatar = getIntent().getStringExtra("avatar");*/
        sessionManager = new SessionManager(getApplicationContext());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeProfile);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getProfileImageLoader();

        nama = (TextView) findViewById(R.id.textNama);
        alamat = (TextView) findViewById(R.id.textAlamat);
        profesi = (TextView) findViewById(R.id.textProfesi);
        circularImageView = (CircularImageView) findViewById(R.id.image_profile);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ReplacePictureActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        });

        setFields();
    }

    private void setFields(){
        HashMap<String, String> user = sessionManager.getUserDetails();
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
            circularImageView.setImageBitmap(myBitmap);
        }else{
            //cek di server
            //Toast.makeText(getApplicationContext(),"Not Exist", Toast.LENGTH_LONG).show();
            cek_avatar();
        }

        nama.setText(user.get(SessionManager.KEY_NAME));
        try{
            alamat.setText(sessionManager.getUserDetails().get(SessionManager.KEY_ALAMAT).equals("") ? "-": sessionManager.getUserDetails().get(SessionManager.KEY_ALAMAT));
        }catch (Exception e){}
        try{
            profesi.setText(sessionManager.getUserDetails().get(SessionManager.KEY_PEKERJAAN).equals("") ? "-": sessionManager.getUserDetails().get(SessionManager.KEY_PEKERJAAN));
        }catch (Exception e){}
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void cek_avatar(){
        if (!StateKMS.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URLEndpoints.CEK_AVATAR + String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String r = response.getString("status");
                                if (r.equals("success")) {
                                    String name = URLEndpoints.AVATARUSERS_FOLDER + "avatar_user_" + String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID) + ".png");
                                    Log.d("FILENAME", name);
                                    //loadImages(name);
                                } else {
                                    if (sessionManager.getUserDetails().get(SessionManager.KEY_LOGIN).equals("fb")) {
                                        FacebookSdk.sdkInitialize(getApplicationContext());
                                        Profile profile = Profile.getCurrentProfile();
                                        loadImages("http://graph.facebook.com/" + profile.getId() + "/picture?type=large");
                                    } else {
                                        //loadImages(URLEndpoints.AVATARS_FOLDER + s_avatar);
                                        circularImageView.setImageDrawable(getResources().getDrawable(R.drawable.dummyuser));
                                    }
                                }
                            } catch (Exception e) {

                            }
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

    private void loadImages(String urlThumbnail) {
        if (!urlThumbnail.equals("NA")) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    circularImageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        setFields();
    }
}
