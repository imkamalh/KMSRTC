package com.dev.it.kmskedelainew.artikel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.konsultasi.KomenKonsultasiActivity;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.rey.material.widget.ProgressView;

public class DetailArtikelActivity extends AppCompatActivity {
    WebView wv1;
    ProgressView progressView;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    String idartikel;
    String title;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getIntent().getStringExtra("judul");
        String sub = getIntent().getStringExtra("jenis");
        toolbar.setTitle(title);
        toolbar.setSubtitle(sub);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        idartikel = getIntent().getStringExtra("id");
        //Toast.makeText(getApplicationContext(), idartikel, Toast.LENGTH_LONG).show();
        progressView = (ProgressView) findViewById(R.id.progresDetail);

        wv1=(WebView)findViewById(R.id.webViewArtikel);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(URLEndpoints.GET_ARTICLES_WEBVIEW + idartikel);
        wv1.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("about:blank");
            }
        });
        //wv1.onPro
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
        }else if (id == R.id.action_comment) {
            Intent in = new Intent(getApplicationContext(), KomenArtikelActivity.class);
            in.putExtra("idartikel",String.valueOf(idartikel));
            startActivity(in);
            return true;
        }else if(id == R.id.action_share){
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(title)
                        .setContentDescription(
                                "Semua informasi seputar kedelai ada di genggaman anda")
                        .setContentUrl(Uri.parse(URLEndpoints.ARTICLE_PUBLIC + idartikel))
                        .build();

                shareDialog.show(linkContent);
            }
        }
        else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
