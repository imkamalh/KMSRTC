package com.dev.it.kmskedelainew.penyuluh;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.it.kmskedelainew.ProfileActivity;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.SplashActivity;
import com.dev.it.kmskedelainew.classes.Penyuluh;
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

public class PenyuluhActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String STATE_PENYULUH = "state_penyuluh";
    private ArrayList<Penyuluh> mListPenyuluh = new ArrayList<>();
    private ArrayList<Penyuluh> mSearchPenyuluh = new ArrayList<>();
    private AdapterPenyuluh mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerPenyuluh;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyuluh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_penyuluh);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPenyuluh);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipePenyuluh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerPenyuluh = (RecyclerView) findViewById(R.id.listPenyuluh);
        //set the layout manager before trying to display data
        mRecyclerPenyuluh.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new AdapterPenyuluh(getApplicationContext());
        mRecyclerPenyuluh.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListPenyuluh = savedInstanceState.getParcelableArrayList(STATE_PENYULUH);
            mAdapter.setList(getApplicationContext(), mListPenyuluh);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListPenyuluh.isEmpty()) {
                loadAllPenyuluh();
            }
        }
    }

    void loadAllPenyuluh(){
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URLEndpoints.GET_PENYULUH, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray jsonArray = response.getJSONArray("penyuluh");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Penyuluh p = new Penyuluh(jsonObject.getInt("id_penyuluh"), jsonObject.getString("nama_penyuluh"),
                                    jsonObject.getString("alamat_penyuluh"), jsonObject.getString("profesi_penyuluh"), jsonObject.getString("avatar_penyuluh"));
                            mListPenyuluh.add(p);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getApplicationContext(), mListPenyuluh);
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
        /*if(sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_after_login, menu);
        else
            getMenuInflater().inflate(R.menu.menu_search, menu);*/
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search");
        //searchView.setIconifiedByDefault(false);
        //searchView.onActionViewExpanded();
        //searchView.requestFocusFromTouch();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //cari
                if(!query.equals("")){
                    mSearchPenyuluh.clear();
                    for(int i = 0 ; i < mListPenyuluh.size(); i++){
                        if(mListPenyuluh.get(i).getNamaPenyuluh().toLowerCase().contains(query.toLowerCase())){
                            mSearchPenyuluh.add(mListPenyuluh.get(i));
                            Log.d("hasil",mListPenyuluh.get(i).getNamaPenyuluh().toLowerCase());
                        }
                        Log.d("hasil",mListPenyuluh.get(i).getNamaPenyuluh().toLowerCase());
                    }
                    mAdapter.setList(getApplicationContext(), mSearchPenyuluh);
                }
                else{
                    mAdapter.setList(getApplicationContext(), mListPenyuluh);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query.equals("")){
                    mAdapter.setList(getApplicationContext(), mListPenyuluh);
                }
                return false;
            }
        });
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PenyuluhActivity.this)
                    .setTitle("Keluar")
                    .setMessage("Anda Yakin Keluar dari Aplikasi ?")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pDialog = new ProgressDialog(PenyuluhActivity.this);
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
        mListPenyuluh = new ArrayList<>();
        loadAllPenyuluh();
    }
}
