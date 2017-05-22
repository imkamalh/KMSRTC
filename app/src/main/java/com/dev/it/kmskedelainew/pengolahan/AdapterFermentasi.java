package com.dev.it.kmskedelainew.pengolahan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.dev.it.kmskedelainew.classes.ItemPengolahan;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class AdapterFermentasi extends RecyclerView.Adapter<AdapterFermentasi.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<ItemPengolahan> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public AdapterFermentasi(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setList(Context ctx, ArrayList<ItemPengolahan> listMission) {
        this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pengolahan, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final ItemPengolahan currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.namaPengolahan.setText(currentMission.getNamaItem());
        //holder.imageArtikel.setImageUrl(URLEndpoints.ARTICLE_FOLDER + currentMission.getGambarArtikel(),mImageLoader);
        holder.nrLayout.setBackgroundColor(Color.parseColor(currentMission.getWarnaBg()));
        holder.nrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, PengolahanDetailActivity.class);
                in.putExtra("id", String.valueOf(currentMission.getIdItem()));
                in.putExtra("nama", currentMission.getNamaItem());
                in.putExtra("img_bg", currentMission.getImagePengolahan());
                in.putExtra("jenis", "Fermentasi");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
            }
        });
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

        TextView namaPengolahan;
        FrameLayout nrLayout;
        public ViewHolderMissions(View itemView) {
            super(itemView);
            namaPengolahan = (TextView) itemView.findViewById(R.id.textPengolahan);
            nrLayout = (FrameLayout) itemView.findViewById(R.id.frameItemPengolahan);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }
}
