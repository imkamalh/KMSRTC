package com.dev.it.kmskedelainew.tentang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.usermanagement.ProfileUserActivity;
import com.dev.it.kmskedelainew.usermanagement.RegisterActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.login.LoginManager;

public class TentangActivity extends AppCompatActivity {
    WebView wv1;
    TextView textView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        wv1=(WebView)findViewById(R.id.webViewTentang);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(URLEndpoints.GET_ABOUT_WEBVIEW);
        wv1.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("about:blank");
            }
        });

        textView = (TextView) findViewById(R.id.textVersion);
        String version = "1.0.1";
        try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        }catch (Exception e){

        }
        textView.setText("Version " + version);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(TentangActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(TentangActivity.this);
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
