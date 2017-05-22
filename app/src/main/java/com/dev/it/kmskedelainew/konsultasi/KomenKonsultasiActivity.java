package com.dev.it.kmskedelainew.konsultasi;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.Komentar;
import com.dev.it.kmskedelainew.konsultasi.adapters.KomentarKonsulAdapter;
import com.dev.it.kmskedelainew.network.CustomRequest;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.dev.it.kmskedelainew.utils.VersionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KomenKonsultasiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private String idtopik;
    RecyclerView recyclerView;
    KomentarKonsulAdapter adapter;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private static final String STATE_MISSIONS = "state_komenkonsul";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Komentar> listData = new ArrayList<>();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komen_konsultasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        idtopik = getIntent().getStringExtra("idtopik");
        sessionManager = new SessionManager(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.listKomen);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor_komen);
        progressBar = (ProgressBar) findViewById(R.id.progressBarKomen);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeKomen);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new KomentarKonsulAdapter(getApplicationContext());
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
                loadKomentar();
            }
        }

        final EditText komen = (EditText) findViewById(R.id.editKomen);
        ImageView imageView = (ImageView) findViewById(R.id.imageKomen);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_komen", String.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID)));
                map.put("topik_id", idtopik);
                map.put("isi_komen", komen.getText().toString());

                CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLEndpoints.SUBMIT_COMEENTS_TOPICS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("response", response.toString());
                            String res = response.getString("status");
                            if (res.equals("success")){
                                listData.add(new Komentar(response.getString("tanggal"), komen.getText().toString(),
                                        sessionManager.getUserDetails().get(SessionManager.KEY_NAME), "aaa", Integer.valueOf(idtopik),response.getInt("id_komen")
                                        ,Integer.valueOf(sessionManager.getUserDetails().get(SessionManager.KEY_ID))));
                                adapter.setDataList(getApplicationContext(), listData);
                            }else{

                            }
                        }catch (Exception e){}
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        komen.setText("");
                        progressBar.setVisibility(View.GONE);
                        adapter.setDataList(getApplicationContext(), listData);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        komen.setText("");
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
                VolleySingleton.getInstance().addToRequestQueue(customRequest);
            }
        });
    }

    private void loadKomentar(){
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
        }else{
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_COMEENTS_TOPICS + idtopik, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        for(int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = response.getJSONObject(i);
                            listData.add(new Komentar(jsonObject.getString("tanggal_komen"),
                                    jsonObject.getString("isi_komen"),jsonObject.getString("nama_user")
                                    , "aaa", jsonObject.getInt("topik_id")
                                    ,jsonObject.getInt("id_komen"),jsonObject.getInt("user_komen")));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_dark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        listData = new ArrayList<>();
        loadKomentar();
    }
}
