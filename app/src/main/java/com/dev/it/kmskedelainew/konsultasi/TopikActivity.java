package com.dev.it.kmskedelainew.konsultasi;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.classes.Topik;
import com.dev.it.kmskedelainew.extras.DownloadActivity;
import com.dev.it.kmskedelainew.extras.SettingsActivity;
import com.dev.it.kmskedelainew.konsultasi.adapters.TopikAdapter;
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

public class TopikActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    TopikAdapter adapter;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private static final String STATE_MISSIONS = "state_topik_activity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Topik> listData = new ArrayList<>();
    SessionManager sessionManager;
    private String idforum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("topik"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());
        idforum = getIntent().getStringExtra("idforum");

        recyclerView = (RecyclerView) findViewById(R.id.listTopik);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_topik);
        progressBar = (ProgressBar) findViewById(R.id.progressBarTopik);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeTopik);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TopikAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            listData = savedInstanceState.getParcelableArrayList(STATE_MISSIONS);
            adapter.setDataList(getApplicationContext(), listData);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (listData.isEmpty()) {
                adapter.setDataList(getApplicationContext(), listData);
                loadData();
            }
        }

    }

    public void loadData(){
        if (!StateKMS.isNetworkConnected(getApplicationContext())){
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Snackbar s = Snackbar.make(coordinatorLayout, "Check Your Internet Connection", Snackbar.LENGTH_LONG);
            s.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            s.show();
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_TOPICS + "/"+ idforum, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        for(int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = response.getJSONObject(i);
                            Topik f = new Topik(jsonObject.getInt("id_topik"), jsonObject.getString("judul_topik"),jsonObject.getInt("jlh_komen"),
                                    jsonObject.getString("tanggal_buat"),jsonObject.getString("isi_topik"));
                            listData.add(f);
                            Log.d("response", jsonObject.toString());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.setDataList(getApplicationContext(), listData);
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
            VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
        }
    }

    @Override
    public void onRefresh() {
        listData = new ArrayList<>();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_konsultasi_after, menu);
        else
            getMenuInflater().inflate(R.menu.menu_konsultasi, menu);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(TopikActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(TopikActivity.this);
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
        //search ?
        return super.onOptionsItemSelected(item);
    }
}
