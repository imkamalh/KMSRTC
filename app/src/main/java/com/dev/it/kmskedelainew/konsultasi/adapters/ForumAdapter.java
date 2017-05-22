package com.dev.it.kmskedelainew.konsultasi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dev.it.kmskedelainew.R;
import com.dev.it.kmskedelainew.classes.ItemForum;
import com.dev.it.kmskedelainew.konsultasi.TopikActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulei on 8/12/2015.
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.GmailVH> {
    ArrayList<ItemForum> dataList;
    String letter;
    Context context;
    ColorGenerator generator = ColorGenerator.MATERIAL;


    /*int colors[] = {R.color.red, R.color.pink, R.color.purple, R.color.deep_purple,
            R.color.indigo, R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green,
            R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange, R.color.deep_orange};*/

    public ForumAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(Context context, ArrayList<ItemForum> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public GmailVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gmail_list_item, viewGroup, false);
        return new GmailVH(view);
    }

    @Override
    public void onBindViewHolder(GmailVH gmailVH, final int i) {
        gmailVH.title.setText(dataList.get(i).getJudulForum());
//        Get the first letter of list item
        letter = String.valueOf(dataList.get(i).getJudulForum().charAt(0));
        gmailVH.subtitle.setText(String.valueOf(dataList.get(i).getJumlahTopik()) + " topics");

//        Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());

        gmailVH.letter.setImageDrawable(drawable);
        gmailVH.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, TopikActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("idforum", String.valueOf(dataList.get(i).getIdForum()));
                in.putExtra("topik", dataList.get(i).getJudulForum());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class GmailVH extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView letter;
        LinearLayout item;

        public GmailVH(View itemView) {
            super(itemView);
            letter = (ImageView) itemView.findViewById(R.id.gmailitem_letter);
            title = (TextView) itemView.findViewById(R.id.gmailitem_title);
            subtitle = (TextView) itemView.findViewById(R.id.textSubtitle);
            item = (LinearLayout) itemView.findViewById(R.id.item_forum);
        }
    }

}


