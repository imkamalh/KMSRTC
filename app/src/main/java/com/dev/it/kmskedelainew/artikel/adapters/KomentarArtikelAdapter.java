package com.dev.it.kmskedelainew.artikel.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.Komentar;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sulei on 8/12/2015.
 */
public class KomentarArtikelAdapter extends RecyclerView.Adapter<KomentarArtikelAdapter.GmailVH> {
    ArrayList<Komentar> dataList;
    String letter;
    Context context;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    SessionManager sessionManager;


    /*int colors[] = {R.color.red, R.color.pink, R.color.purple, R.color.deep_purple,
            R.color.indigo, R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green,
            R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange, R.color.deep_orange};*/

    public KomentarArtikelAdapter(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    public void setDataList(Context context, ArrayList<Komentar> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public GmailVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_komentar, viewGroup, false);
        return new GmailVH(view);
    }

    @Override
    public void onBindViewHolder(GmailVH gmailVH, final int i) {
        final Komentar current = dataList.get(i);
        gmailVH.nama.setText(current.getNamaUser());
        gmailVH.tanggal.setText(current.getTanggalKomentar());
        gmailVH.isi.setText(current.getIsiKomentar());
        gmailVH.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(String.valueOf(current.getIdUser()).equals(sessionManager.getUserDetails().get(SessionManager.KEY_ID))){
                    AlertDialog.Builder builder  = new AlertDialog.Builder(v.getRootView().getContext())
                            .setMessage("Delete this comment?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = String.valueOf(current.getIdKomentar());
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URLEndpoints.DELETE_COMEENTS_ARTICLES + id, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.d("hapus", response.toString());
                                                String res = response.getString("status");
                                                if (res.equals("success")){
                                                    dataList.remove(i);
                                                    notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(context.getApplicationContext(),"Error Encountered", Toast.LENGTH_LONG).show();
                                                }
                                            }catch (Exception e){}
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context.getApplicationContext(),"Error Encountered", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class GmailVH extends RecyclerView.ViewHolder {
        TextView nama;
        TextView tanggal;
        TextView isi;
        ImageView avatar;
        LinearLayout item;

        public GmailVH(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            tanggal = (TextView) itemView.findViewById(R.id.textTanggalKomen);
            nama = (TextView) itemView.findViewById(R.id.textNamaKomen);
            isi = (TextView) itemView.findViewById(R.id.textIsiKomen);
            item = (LinearLayout) itemView.findViewById(R.id.item_komen);
        }
    }

}


