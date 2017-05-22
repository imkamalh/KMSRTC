package com.dev.it.kmskedelainew.konsultasi.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.MainActivity;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.ItemTeknologi;
import com.dev.it.kmskedelainew.classes.Topik;
import com.dev.it.kmskedelainew.konsultasi.DetailKonsultasiActivity;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.teknologi.TeknologiDetailActivity;
import com.dev.it.kmskedelainew.usermanagement.LoginActivity;
import com.dev.it.kmskedelainew.utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class TopikAdapter extends RecyclerView.Adapter<TopikAdapter.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<Topik> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private SessionManager sessionManager;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public TopikAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
        sessionManager = new SessionManager(context);
    }

    public void setDataList(Context ctx, ArrayList<Topik> listMission) {
        this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_topik, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final Topik currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        holder.namTopik.setText(currentMission.getJudulTopik());
        holder.isiTopik.setText(currentMission.getIsiTopik());
        holder.jlhKomen.setText(String.valueOf(currentMission.getJumlahKomentar()) + " comments");
        holder.nrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionManager.isLoggedIn()){
                    Intent in = new Intent(mContext, DetailKonsultasiActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("judul", currentMission.getJudulTopik());
                    in.putExtra("idtopik", String.valueOf(currentMission.getIdTopik()));
                    in.putExtra("isitopik", String.valueOf(currentMission.getIsiTopik()));
                    mContext.startActivity(in);
                }else{
                    AlertDialog.Builder builder  = new AlertDialog.Builder(v.getRootView().getContext())
                            .setMessage("Silahkan masuk ke akun anda untuk mengakses konten ini")
                            .setPositiveButton("Masuk", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in = new Intent(mContext, LoginActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(in);
                                }
                            })
                            .setNegativeButton("Batal", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
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

        TextView namTopik;
        TextView isiTopik;
        TextView jlhKomen;
        LinearLayout nrLayout;
        public ViewHolderMissions(View itemView) {
            super(itemView);
            namTopik = (TextView) itemView.findViewById(R.id.judulTopik);
            isiTopik = (TextView) itemView.findViewById(R.id.isiTopik);
            jlhKomen = (TextView) itemView.findViewById(R.id.jumlahKomenTopik);
            nrLayout = (LinearLayout) itemView.findViewById(R.id.linearTopik);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }
}
