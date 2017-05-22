package com.dev.it.kmskedelainew.konsultasi.fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.Topik;
import com.dev.it.kmskedelainew.konsultasi.adapters.TopikAdapter;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopikFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    TopikAdapter adapter;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    private static final String STATE_MISSIONS = "state_topik";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Topik> listData = new ArrayList<>();


    public TopikFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TopikFragment newInstance(String param1, String param2) {
        TopikFragment fragment = new TopikFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topik, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listTopik);
        frameLayout = (FrameLayout) view.findViewById(R.id.frag_topik);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarTopik);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeTopik);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TopikAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            listData = savedInstanceState.getParcelableArrayList(STATE_MISSIONS);
            adapter.setDataList(getActivity(), listData);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (listData.isEmpty()) {
                adapter.setDataList(getActivity(), listData);
                loadData();
            }
        }
        return view;
    }

    public void loadData(){
        if (!StateKMS.isNetworkConnected(getActivity())){
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Snackbar s = Snackbar.make(frameLayout, "Check Your Internet Connection", Snackbar.LENGTH_LONG);
            s.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            s.show();
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_TOPICS, new Response.Listener<JSONArray>() {
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
                    adapter.setDataList(getActivity(), listData);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
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
    public void onRefresh() {
        listData = new ArrayList<>();
        loadData();
    }
}
