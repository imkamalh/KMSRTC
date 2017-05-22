package com.dev.it.kmskedelainew.teknologi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.ItemTeknologi;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.teknologi.TeknologiDetailActivity;

import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class AdapterHama extends RecyclerView.Adapter<AdapterHama.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<ItemTeknologi> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public AdapterHama(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setList(Context ctx, ArrayList<ItemTeknologi> listMission) {
        this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_teknologi, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final ItemTeknologi currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.namaTeknologi.setText(currentMission.getNamaItem());
        //holder.imageArtikel.setImageUrl(URLEndpoints.ARTICLE_FOLDER + currentMission.getGambarArtikel(),mImageLoader);
        holder.nrLayout.setBackgroundColor(Color.parseColor(currentMission.getWarnaBg()));
        holder.nrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, TeknologiDetailActivity.class);
                in.putExtra("id", String.valueOf(currentMission.getIdItem()));
                in.putExtra("nama", currentMission.getNamaItem());
                in.putExtra("sub", "Hama");
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

        TextView namaTeknologi;
        FrameLayout nrLayout;
        public ViewHolderMissions(View itemView) {
            super(itemView);
            namaTeknologi = (TextView) itemView.findViewById(R.id.textViewTeknologi);
            nrLayout = (FrameLayout) itemView.findViewById(R.id.frameTeknologi);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }
}
