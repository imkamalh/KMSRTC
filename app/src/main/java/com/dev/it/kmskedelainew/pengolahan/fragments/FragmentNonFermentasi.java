package com.dev.it.kmskedelainew.pengolahan.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.dev.it.kmskedelainew.classes.ItemPengolahan;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.pengolahan.AdapterFermentasi;
import com.dev.it.kmskedelainew.pengolahan.AdapterNONFermentasi;
import com.dev.it.kmskedelainew.utils.StateKMS;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentNonFermentasi extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String STATE_MISSIONS = "state_non_fermentasi";
    private ArrayList<ItemPengolahan> mListPengolahan = new ArrayList<>();
    private AdapterNONFermentasi mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerMissions;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    String[] bg_color = {"#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354",
            "#2BA195", "#5BBD76","#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354", "#2BA195", "#5BBD76",
            "#74DB4F", "#207354", "#2BA195", "#5BBD76"};

    public FragmentNonFermentasi() {
        // Required empty public constructor
    }

    public static FragmentNonFermentasi newInstance(String param1, String param2) {
        FragmentNonFermentasi fragment = new FragmentNonFermentasi();
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
        View layout = inflater.inflate(R.layout.fragment_pengolahan_one, container, false);
        frameLayout = (FrameLayout) layout.findViewById(R.id.frameFermantasi);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBarPengolahan);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipePengolahan);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerMissions = (RecyclerView) layout.findViewById(R.id.listPengolahan);
        //set the layout manager before trying to display data
        mRecyclerMissions.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterNONFermentasi(getActivity());
        mRecyclerMissions.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing movies from a parcelable
            mListPengolahan = savedInstanceState.getParcelableArrayList(STATE_MISSIONS);
            mAdapter.setList(getActivity(), mListPengolahan);
        } else {
            //if this fragment starts for the first time, load the list of movies from a database
            // mListMissions = ApplicationClass.getWritableDatabase().readMissions(DBMissions.ALL_MISSIONS);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (mListPengolahan.isEmpty()) {
                loadPengolahan();
            }
        }
        return layout;
    }

    void loadPengolahan(){
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
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLEndpoints.GET_LIST_NON_FERMENTASI, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ItemPengolahan itemPengolahan = new ItemPengolahan(jsonObject.getInt("id_pengolahan"), jsonObject.getString("nama_pengolahan"),
                                    jsonObject.getString("gambar_pengolahan"), bg_color[i]);
                            mListPengolahan.add(itemPengolahan);
                            Log.d("itemPengolahan", jsonObject.toString());
                        }
                    } catch (Exception e) {
                    }
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setList(getActivity(), mListPengolahan);
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
        mListPengolahan = new ArrayList<>();
        loadPengolahan();
    }
}
