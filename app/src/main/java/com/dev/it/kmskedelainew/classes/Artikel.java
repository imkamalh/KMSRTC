package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class Artikel implements Parcelable{
    public static final Creator<Artikel> CREATOR
            = new Creator<Artikel>() {
        public Artikel createFromParcel(Parcel in) {
            return new Artikel(in);
        }

        public Artikel[] newArray(int size) {
            return new Artikel[size];
        }
    };

    private int idArtikel;
    private String judulArtikel;
    private String tanggalArtikel;
    private String gambarArtikel;

    public Artikel(Parcel input) {
        idArtikel = input.readInt();
        judulArtikel = input.readString();
        tanggalArtikel = input.readString();
        gambarArtikel = input.readString();
    }

    public Artikel(int idArtikel, String judulArtikel, String tanggalArtikel, String gambarArtikel) {
        this.idArtikel = idArtikel;
        this.judulArtikel = judulArtikel;
        this.tanggalArtikel = tanggalArtikel;
        this.gambarArtikel = gambarArtikel;
    }

    public int getIdArtikel() {
        return idArtikel;
    }

    public String getJudulArtikel() {
        return judulArtikel;
    }

    public String getTanggalArtikel() {
        return tanggalArtikel;
    }

    public String getGambarArtikel() {
        return gambarArtikel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idArtikel);
        dest.writeString(judulArtikel);
        dest.writeString(tanggalArtikel);
        dest.writeString(gambarArtikel);
    }
}
