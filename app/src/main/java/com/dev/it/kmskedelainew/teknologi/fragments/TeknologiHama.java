package com.dev.it.kmskedelainew.teknologi.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.ItemTeknologi;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.teknologi.adapters.AdapterBudidaya;
import com.dev.it.kmskedelainew.teknologi.adapters.AdapterHama;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TeknologiHama extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String STATE_MISSIONS = "state_hama";
    private ArrayList<ItemTeknologi> mListTeknologi = new ArrayList<>();
    private AdapterHama mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerMissions;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    public TeknologiHama() {
        // Required empty public constructor
    }

    public static TeknologiHama newInstance(String param1, String param2) {
        TeknologiHama fragment = new TeknologiHama();
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
        View layout = inflater.inflate(R.layout.fragment_teknologi_one, container, false);
        frameLayout = (FrameLayout) layout.findViewById(R.id.frameTeknologi);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBarTeknologi);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeTeknologi);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerMissions = (RecyclerView) layout.findViewById(R.id.listTeknologi);
        //set the layout manager before trying to display data
        mRecyclerMissions.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterHama(getActivity());
        mRecyclerMissions.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListTeknologi = savedInstanceState.getParcelableArrayList(STATE_MISSIONS);
            mAdapter.setList(getActivity(), mListTeknologi);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListTeknologi.isEmpty()) {
                loadTeknologi();
            }
        }
        return layout;
    }

    private void loadTeknologi(){
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
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_LIST_TEKNOLOGI + "3", new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ItemTeknologi item = new ItemTeknologi(jsonObject.getInt("id_d_master_teknologi"),
                                    jsonObject.getString("detail_teknologi"),StateKMS.getBgColor(i));
                            mListTeknologi.add(item);
                        }
                    }catch (Exception e){}
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getActivity(), mListTeknologi);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getActivity(), mListTeknologi);
                    Snackbar s = Snackbar.make(frameLayout, "Sorry, Error Encountered", Snackbar.LENGTH_LONG);
                    s.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    s.show();
                }
            });
            VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
        }
    }

    @Override
    public void onRefresh() {
        mListTeknologi = new ArrayList<>();
        loadTeknologi();
    }

}
