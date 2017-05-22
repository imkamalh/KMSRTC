package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/20/2016.
 */
public class Penyuluh implements Parcelable {

    public static final Creator<Penyuluh> CREATOR
            = new Creator<Penyuluh>() {
        public Penyuluh createFromParcel(Parcel in) {
            return new Penyuluh(in);
        }

        public Penyuluh[] newArray(int size) {
            return new Penyuluh[size];
        }
    };

    private int idPenyuluh;
    private String namaPenyuluh;
    private String alamatPenyuluh;
    private String profesiPenyuluh;
    private String avatarPenyuluh;

    public Penyuluh(int idPenyuluh, String namaPenyuluh, String alamatPenyuluh, String profesiPenyuluh, String avatarPenyuluh) {
        this.idPenyuluh = idPenyuluh;
        this.namaPenyuluh = namaPenyuluh;
        this.alamatPenyuluh = alamatPenyuluh;
        this.profesiPenyuluh = profesiPenyuluh;
        this.avatarPenyuluh = avatarPenyuluh;
    }

    public Penyuluh(Parcel input) {
        idPenyuluh = input.readInt();
        namaPenyuluh = input.readString();
        alamatPenyuluh = input.readString();
        profesiPenyuluh = input.readString();
        avatarPenyuluh = input.readString();
    }

    public int getIdPenyuluh() {
        return idPenyuluh;
    }

    public String getNamaPenyuluh() {
        return namaPenyuluh;
    }

    public String getAlamatPenyuluh() {
        return alamatPenyuluh;
    }

    public String getProfesiPenyuluh() {
        return profesiPenyuluh;
    }

    public String getAvatarPenyuluh() {
        return avatarPenyuluh;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idPenyuluh);
        dest.writeString(namaPenyuluh);
        dest.writeString(alamatPenyuluh);
        dest.writeString(profesiPenyuluh);
        dest.writeString(avatarPenyuluh);
    }
}
