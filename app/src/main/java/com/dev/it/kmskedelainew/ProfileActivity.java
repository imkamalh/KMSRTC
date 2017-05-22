package com.dev.it.kmskedelainew;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.usermanagement.ProfileUserActivity;
import com.dev.it.kmskedelainew.usermanagement.RegisterActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.login.LoginManager;
import com.pkmmte.view.CircularImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView nama, alamat, profesi;
    CircularImageView circularImageView;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profil Penyuluh");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String s_nama = getIntent().getStringExtra("nama");
        String s_alamat = getIntent().getStringExtra("alamat");
        String s_profesi = getIntent().getStringExtra("pekerjaan");
        String s_avatar = getIntent().getStringExtra("avatar");
        sessionManager = new SessionManager(getApplicationContext());

        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();

        nama = (TextView) findViewById(R.id.textNama);
        alamat = (TextView) findViewById(R.id.textAlamat);
        profesi = (TextView) findViewById(R.id.textProfesi);
        circularImageView = (CircularImageView) findViewById(R.id.image_profile);
        loadImages(URLEndpoints.AVATARS_FOLDER + s_avatar);

        nama.setText(s_nama);
        alamat.setText(s_alamat);
        profesi.setText(s_profesi);
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
        if(sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_after_login, menu);
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_profile) {
            Intent in = new Intent(getApplicationContext(), ProfileUserActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_user) {
            Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            return true;
        } else if (id == R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(ProfileActivity.this);
                            pDialog.setMessage("mohon tunggu ...");
                            pDialog.show();
                            pDialog.setCancelable(false);

                            sessionManager.logoutUser();
                            LoginManager.getInstance().logOut();
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    //LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                }
                            }, 2000);
                        }
                    })
                    .setNegativeButton("Batal", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (id == R.id.action_login){
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else if (id == R.id.action_download){
            Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
