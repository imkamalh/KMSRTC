package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class Komentar implements Parcelable{
    public static final Creator<Komentar> CREATOR
            = new Creator<Komentar>() {
        public Komentar createFromParcel(Parcel in) {
            return new Komentar(in);
        }

        public Komentar[] newArray(int size) {
            return new Komentar[size];
        }
    };

    private int idKomentar;
    private String tanggalKomentar;
    private String isiKomentar;
    private String namaUser;
    private int idUser;
    private String avatarUser;
    private int idTopik;

    public Komentar(Parcel input) {
        idKomentar = input.readInt();
        idTopik = input.readInt();
        tanggalKomentar = input.readString();
        isiKomentar = input.readString();
        namaUser = input.readString();
        avatarUser = input.readString();
        idUser = input.readInt();
    }

    public Komentar(String tanggalKomentar, String isiKomentar, String namaUser, String avatarUser, int idTopik, int idKomentar, int idUser) {
        this.tanggalKomentar = tanggalKomentar;
        this.isiKomentar = isiKomentar;
        this.namaUser = namaUser;
        this.avatarUser = avatarUser;
        this.idTopik = idTopik;
        this.idKomentar = idKomentar;
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdKomentar() {
        return idKomentar;
    }

    public String getTanggalKomentar() {
        return tanggalKomentar;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public int getIdTopik() {
        return idTopik;
    }

    public void setIdKomentar(int idKomentar) {
        this.idKomentar = idKomentar;
    }

    public void setTanggalKomentar(String tanggalKomentar) {
        this.tanggalKomentar = tanggalKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public void setIdTopik(int idTopik) {
        this.idTopik = idTopik;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idKomentar);
        dest.writeInt(idTopik);
        dest.writeString(namaUser);
        dest.writeString(avatarUser);
        dest.writeString(isiKomentar);
        dest.writeString(tanggalKomentar);
        dest.writeInt(idUser);
    }
}
