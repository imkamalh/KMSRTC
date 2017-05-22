package com.dev.it.kmskedelainew.kedelai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.Kedelai;

import java.util.ArrayList;

/**
 * Created by jemsnaban on 4/23/2016.
 */
public class KedelaiAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Kedelai> listKedelai = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public KedelaiAdapter(Context ctx){
        this.ctx = ctx;
        inflater = (LayoutInflater)ctx.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(Context ctx, ArrayList<Kedelai> listKedelai){
        this.ctx = ctx;
        this.listKedelai = listKedelai;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listKedelai.size();
    }

    @Override
    public Object getItem(int position) {
        return listKedelai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if(row == null){
            row = inflater.inflate(R.layout.item_kedelai, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.textNamaBagian);
            holder.linearLayout = (LinearLayout) row.findViewById(R.id.linearKedelai);
            row.setTag(holder);
            final Kedelai currentItem = listKedelai.get(position);
            holder.txtTitle.setText(currentItem.getNamaBagian());
            holder.linearLayout.setBackgroundColor(Color.parseColor(currentItem.getWarnaBg()));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ctx, KedelaiDetailActivity.class);
                    in.putExtra("judul", currentItem.getNamaBagian());
                    in.putExtra("id", String.valueOf(currentItem.getIdKedelai()));
                    in.putExtra("nama_image", currentItem.getNamaImage());
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);
                }
            });
        }

        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        LinearLayout linearLayout;
    }
}
