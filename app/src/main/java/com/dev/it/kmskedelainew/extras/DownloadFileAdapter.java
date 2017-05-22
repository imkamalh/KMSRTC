package com.dev.it.kmskedelainew.extras;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.ItemFile;
import com.dev.it.kmskedelainew.network.VolleySingleton;
import com.dev.it.kmskedelainew.utils.InputStreamVolleyRequest;
import com.dev.it.kmskedelainew.utils.SessionManager;
import com.dev.it.kmskedelainew.utils.URLEndpoints;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Windows on 12-02-2015.
 */
public class DownloadFileAdapter extends RecyclerView.Adapter<DownloadFileAdapter.ViewHolderMissions> {

    //contains the list of movies
    private static ArrayList<ItemFile> mListMissions = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private SessionManager sessionManager;
    private ImageLoader mImageLoader;
    private Context mContext;
    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;


    public DownloadFileAdapter(Context context) {
        super();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
        sessionManager = new SessionManager(context);
    }

    public void setDataList(Context ctx, ArrayList<ItemFile> listMission) {
        //this.mContext = ctx;
        this.mListMissions = listMission;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMissions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_download, parent, false);
        ViewHolderMissions viewHolder = new ViewHolderMissions(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMissions holder, int position) {
        final ItemFile currentMission = mListMissions.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
        String tipe = currentMission.getTipeFile();
        if (tipe.equals(".xls") || tipe.equals(".xlsx")) {
            holder.av.setImageDrawable(mContext.getResources().getDrawable(R.drawable.xls));
        } else if (tipe.equals(".ppt") || tipe.equals(".pptx")) {
            holder.av.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ppt));
        } else if (tipe.equals(".pdf")) {
            holder.av.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdf));
        } else if (tipe.equals(".doc") || tipe.equals(".docx")) {
            holder.av.setImageDrawable(mContext.getResources().getDrawable(R.drawable.word));
        }

        holder.namFile.setText(currentMission.getNamaFile()+currentMission.getTipeFile());
        holder.ukuranFile.setText(String.valueOf(currentMission.getUkuranFile()) + " Kb");
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext())
                        .setMessage("Unduh File Ini ?")
                        .setPositiveButton("Unduh", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, URLEndpoints.FILE_FOLDER +
                                        currentMission.getNamaFile() + currentMission.getTipeFile(),
                                        new Response.Listener<byte[]>() {
                                            @Override
                                            public void onResponse(byte[] response) {
                                                // TODO handle the response
                                                try {
                                                    if (response != null) {
                                                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                                        FileOutputStream outputStream;
                                                        String name = currentMission.getNamaFile() + currentMission.getTipeFile();
                                                        File file = new File(path, name);
                                                        outputStream = new FileOutputStream(file);
                                                        outputStream.write(response);
                                                        outputStream.close();
                                                        Toast.makeText(mContext, "Download complete.", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (Exception e) {
                                                    // TODO Auto-generated catch block
                                                    Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO handle the error
                                        error.printStackTrace();
                                    }
                                }, null);
                                VolleySingleton.getInstance().addToRequestQueue(request);
                            }
                        })
                        .setNegativeButton("Batal", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return mListMissions.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static class ViewHolderMissions extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView namFile;
        TextView ukuranFile;
        LinearLayout nrLayout;
        ImageView av;
        ImageView download;

        public ViewHolderMissions(View itemView) {
            super(itemView);
            namFile = (TextView) itemView.findViewById(R.id.namaFile);
            ukuranFile = (TextView) itemView.findViewById(R.id.ukuranFile);
            av = (ImageView) itemView.findViewById(R.id.imageFile);
            download = (ImageView) itemView.findViewById(R.id.imageDownload);
            nrLayout = (LinearLayout) itemView.findViewById(R.id.linearFile);
        }

        @Override
        public void onClick(View v) {
            //Log.d("itemg", "itemClick " + mListMissions.get(getPosition()).getLongitude());
        }
    }

}
