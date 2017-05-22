package com.dev.it.kmskedelainew.penyuluh;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.ProfileActivity;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.Penyuluh;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.URLEndpoints;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class AdapterPenyuluh extends RecyclerView.Adapter<AdapterPenyuluh.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<Penyuluh> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public AdapterPenyuluh(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setList(Context ctx, ArrayList<Penyuluh> listMission) {
        this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_penyuluh, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final Penyuluh currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.namaPenyuluh.setText(currentMission.getNamaPenyuluh());
        holder.profesi.setText(currentMission.getProfesiPenyuluh());
        loadImages(URLEndpoints.AVATARS_FOLDER + currentMission.getAvatarPenyuluh(), holder);

        holder.nrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ProfileActivity.class);
                in.putExtra("nama", currentMission.getNamaPenyuluh());
                in.putExtra("pekerjaan", currentMission.getProfesiPenyuluh());
                in.putExtra("alamat", currentMission.getAlamatPenyuluh());
                in.putExtra("avatar", currentMission.getAvatarPenyuluh());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
            }
        });
    }

    private void loadImages(String urlThumbnail, final ViewHolderMissions holder) {
        if (!urlThumbnail.equals("NA")) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.avatarPenyuluh.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
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

        TextView namaPenyuluh;
        TextView profesi;
        LinearLayout nrLayout;
        CircularImageView avatarPenyuluh;
        public ViewHolderMissions(View itemView) {
            super(itemView);
            namaPenyuluh = (TextView) itemView.findViewById(R.id.namaPenyuluh);
            profesi = (TextView) itemView.findViewById(R.id.profesiPenyuluh);
            nrLayout = (LinearLayout) itemView.findViewById(R.id.linearPenyuluh);
            avatarPenyuluh = (CircularImageView) itemView.findViewById(R.id.avatarPenyuluh);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }
}
