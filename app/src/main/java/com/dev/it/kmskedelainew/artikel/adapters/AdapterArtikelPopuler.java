package com.dev.it.kmskedelainew.artikel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.artikel.DetailArtikelActivity;
import com.dev.it.kmskedelainew.classes.Artikel;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class AdapterArtikelPopuler extends RecyclerView.Adapter<AdapterArtikelPopuler.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<Artikel> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public AdapterArtikelPopuler(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setList(Context ctx, ArrayList<Artikel> listMission) {
        this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_artikel, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final Artikel currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.judulArtikel.setText(currentMission.getJudulArtikel());
        holder.tanggalArtikel.setText(currentMission.getTanggalArtikel());
        holder.imageArtikel.setImageUrl(URLEndpoints.ARTICLE_FOLDER + currentMission.getGambarArtikel(),mImageLoader);

        holder.nrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHit(String.valueOf(currentMission.getIdArtikel()));
                Intent in = new Intent(mContext, DetailArtikelActivity.class);
                in.putExtra("id", String.valueOf(currentMission.getIdArtikel()));
                in.putExtra("judul", currentMission.getJudulArtikel());
                in.putExtra("tanggal", currentMission.getTanggalArtikel());
                in.putExtra("avatar", currentMission.getGambarArtikel());
                in.putExtra("jenis", "Terbaru");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
            }
        });
    }

    void updateHit(String id){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URLEndpoints.UPDATE_HIT + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("hit",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        try {
            return mListMissions.size();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static class ViewHolderMissions extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView judulArtikel;
        TextView tanggalArtikel;
        LinearLayout nrLayout;
        NetworkImageView imageArtikel;
        public ViewHolderMissions(View itemView) {
            super(itemView);
            judulArtikel = (TextView) itemView.findViewById(R.id.textJudulArtikel);
            tanggalArtikel = (TextView) itemView.findViewById(R.id.textTglArtikel);
            nrLayout = (LinearLayout) itemView.findViewById(R.id.linearArtikel);
            imageArtikel = (NetworkImageView) itemView.findViewById(R.id.imageArtikel);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }
}
