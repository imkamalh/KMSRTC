package com.dev.it.kmskedelainew.kedelai;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.it.kmskedelainew.ProfileActivity;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.classes.Kedelai;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.usermanagement.ProfileUserActivity;
import com.dev.it.kmskedelainew.usermanagement.RegisterActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class KedelaiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    FrameLayout akar, batang, polong, stadia, biji, daun, asalusul, taksonomi;

    private static final String STATE_MISSIONS = "state_kedelai";
    private ArrayList<Kedelai> mListKedelai;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private KedelaiAdapter mAdapter;
    private GridView gridView;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    SessionManager sessionManager;
    String[] bg_color = {"#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354",
            "#2BA195", "#5BBD76","#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354", "#2BA195", "#5BBD76"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kedelai);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        mListKedelai = new ArrayList<>();
        frameLayout = (FrameLayout) findViewById(R.id.frameKedelai);
        progressBar = (ProgressBar) findViewById(R.id.progressBarKedelai);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeKedelai);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new KedelaiAdapter(this);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListKedelai = savedInstanceState.getParcelableArrayList(STATE_MISSIONS);
            mAdapter.setList(getApplicationContext(), mListKedelai);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListKedelai.isEmpty()) {
                loadKedelai();
            }
        }
    }

    private void loadKedelai(){
        if (!StateKMS.isNetworkConnected(getApplicationContext())){
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Snackbar s = Snackbar.make(frameLayout, "You are offline, check you connection", Snackbar.LENGTH_LONG);
            s.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            s.show();
        }else{
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_LIST_KEDELAI, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try{
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = response.getJSONObject(i);
                            Kedelai k = new Kedelai(jsonObject.getInt("id_kedelai"), jsonObject.getString("nama_bgn_kedelai"),
                                    jsonObject.getString("deskripsi_kedelai"),jsonObject.getString("gambar_kedelai"),bg_color[i]);
                            mListKedelai.add(k);
                        }
                    }catch (Exception e){}

                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getApplicationContext(), mListKedelai);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getApplicationContext(), mListKedelai);
                    Snackbar s = Snackbar.make(frameLayout, "Sorry, Error Encountered", Snackbar.LENGTH_LONG);
                    s.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {}
                    });
                    s.show();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(KedelaiActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(KedelaiActivity.this);
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

    @Override
    public void onRefresh() {
        mListKedelai = new ArrayList<>();
        loadKedelai();
    }
}
