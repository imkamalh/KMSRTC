package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/20/2016.
 */
public class ItemForum implements Parcelable {

    public static final Creator<ItemForum> CREATOR
            = new Creator<ItemForum>() {
        public ItemForum createFromParcel(Parcel in) {
            return new ItemForum(in);
        }

        public ItemForum[] newArray(int size) {
            return new ItemForum[size];
        }
    };

    private int idForum;
    private int jumlahTopik;
    private String judulForum;

    public ItemForum(int idForum, String judulForum, int jumlahTopik) {
        this.idForum = idForum;
        this.judulForum = judulForum;
        this.jumlahTopik = jumlahTopik;
    }

    public ItemForum(Parcel input) {
        idForum = input.readInt();
        jumlahTopik = input.readInt();
        judulForum = input.readString();
    }

    public String getJudulForum() {
        return judulForum;
    }

    public int getJumlahTopik() {
        return jumlahTopik;
    }

    public int getIdForum() {
        return idForum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idForum);
        dest.writeString(judulForum);
        dest.writeInt(jumlahTopik);
    }
}
