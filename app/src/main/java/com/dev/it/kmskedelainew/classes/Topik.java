package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class Topik implements Parcelable{
    public static final Creator<Topik> CREATOR
            = new Creator<Topik>() {
        public Topik createFromParcel(Parcel in) {
            return new Topik(in);
        }

        public Topik[] newArray(int size) {
            return new Topik[size];
        }
    };

    private int idTopik;
    private String judulTopik;
    private int jumlahKomentar;
    private String tanggalTopik;
    private String isiTopik;

    public Topik(Parcel input) {
        idTopik = input.readInt();
        jumlahKomentar = input.readInt();
        judulTopik = input.readString();
        tanggalTopik = input.readString();
        isiTopik = input.readString();
    }

    public Topik(int idTopik, String judulTopik, int jumlahKomentar, String tanggalTopik, String isiTopik) {
        this.idTopik = idTopik;
        this.judulTopik = judulTopik;
        this.jumlahKomentar = jumlahKomentar;
        this.tanggalTopik = tanggalTopik;
        this.isiTopik = isiTopik;
    }

    public int getIdTopik() {
        return idTopik;
    }

    public String getJudulTopik() {
        return judulTopik;
    }

    public int getJumlahKomentar() {
        return jumlahKomentar;
    }

    public String getTanggalTopik() {
        return tanggalTopik;
    }

    public String getIsiTopik() {
        return isiTopik;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idTopik);
        dest.writeInt(jumlahKomentar);
        dest.writeString(tanggalTopik);
        dest.writeString(judulTopik);
        dest.writeString(isiTopik);
    }
}
