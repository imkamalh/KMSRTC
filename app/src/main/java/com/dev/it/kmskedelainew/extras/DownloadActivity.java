package com.dev.it.kmskedelainew.extras;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.classes.ItemFile;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.penyuluh.AdapterPenyuluh;
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

public class DownloadActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String STATE_PENYULUH = "state_downlaod";
    private ArrayList<ItemFile> mListFile = new ArrayList<>();
    private DownloadFileAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerDownload;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_download);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDownload);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeDownload);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerDownload = (RecyclerView) findViewById(R.id.listDownload);
        //set the layout manager before trying to display data
        mRecyclerDownload.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new DownloadFileAdapter(getApplicationContext());
        mRecyclerDownload.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListFile = savedInstanceState.getParcelableArrayList(STATE_PENYULUH);
            mAdapter.setDataList(getApplicationContext(), mListFile);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListFile.isEmpty()) {
                loadAllFiles();
            }
        }
    }

    public void loadAllFiles(){
        if (!StateKMS.isNetworkConnected(this)){
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Snackbar s = Snackbar.make(coordinatorLayout, "Check Your Internet Connection", Snackbar.LENGTH_LONG);
            s.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            s.show();
        }else{
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(URLEndpoints.GET_FILE_LIST, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("response", response.toString());
                    try{
                        for(int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = response.getJSONObject(i);
                            mListFile.add(new ItemFile(jsonObject.getInt("id_file"), jsonObject.getString("nama_file"), jsonObject.getInt("ukuran_file"),
                                    jsonObject.getString("tipe_file")));
                        }
                    }catch (Exception e){

                    }
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setDataList(getApplicationContext(), mListFile);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    Snackbar s = Snackbar.make(coordinatorLayout, "Sorry, Error Encountered", Snackbar.LENGTH_LONG);
                    s.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {}
                    });
                    s.show();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(DownloadActivity.this);
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

        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mListFile = new ArrayList<>();
        loadAllFiles();
    }
}
