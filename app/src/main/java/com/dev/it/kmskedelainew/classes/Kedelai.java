package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/23/2016.
 */
public class Kedelai implements Parcelable {
    public static final Creator<Kedelai> CREATOR
            = new Creator<Kedelai>() {
        public Kedelai createFromParcel(Parcel in) {
            return new Kedelai(in);
        }

        public Kedelai[] newArray(int size) {
            return new Kedelai[size];
        }
    };

    private int idKedelai;
    private String namaBagian;
    private String isiDeskripsi;
    private String namaImage;
    private String warnaBg;

    public Kedelai(int idKedelai, String namaBagian, String isiDeskripsi, String namaImage, String warnaBg) {
        this.idKedelai = idKedelai;
        this.namaBagian = namaBagian;
        this.isiDeskripsi = isiDeskripsi;
        this.namaImage = namaImage;
        this.warnaBg = warnaBg;
    }

    public Kedelai(Parcel input){
        idKedelai = input.readInt();
        namaBagian = input.readString();
        isiDeskripsi = input.readString();
        namaImage = input.readString();
        warnaBg = input.readString();
    }

    public int getIdKedelai() {
        return idKedelai;
    }

    public String getNamaBagian() {
        return namaBagian;
    }

    public String getIsiDeskripsi() {
        return isiDeskripsi;
    }

    public String getNamaImage() {
        return namaImage;
    }

    public String getWarnaBg() {
        return warnaBg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idKedelai);
        dest.writeString(namaBagian);
        dest.writeString(isiDeskripsi);
        dest.writeString(namaImage);
        dest.writeString(warnaBg);
    }
}
