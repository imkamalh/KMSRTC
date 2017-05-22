package com.dev.it.kmskedelainew.konsultasi;

import android.app.SearchManager;
import android.content.Context;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.teknologi.KomenTeknologiActivity;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class DetailKonsultasiActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private String idtopik;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konsultasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("judul"));
        //toolbar.setSubtitle("Forum");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        //toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getStringExtra("judul");

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        TextView judul = (TextView) findViewById(R.id.textJudulTopik);
        TextView isi = (TextView) findViewById(R.id.textIsiTopik);

        judul.setText(getIntent().getStringExtra("judul"));
        isi.setText(getIntent().getStringExtra("isitopik"));
        idtopik = (getIntent().getStringExtra("idtopik"));
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
            Intent in = new Intent(getApplicationContext(), KomenKonsultasiActivity.class);
            in.putExtra("idtopik",idtopik);
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
                        .setContentUrl(Uri.parse(URLEndpoints.TOPIK_PUBLIC + idtopik))
                        .build();

                shareDialog.show(linkContent);
            }
        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
