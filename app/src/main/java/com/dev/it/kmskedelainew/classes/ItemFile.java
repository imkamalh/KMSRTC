package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 5/19/2016.
 */
public class ItemFile implements Parcelable {
    public static final Creator<ItemFile> CREATOR
            = new Creator<ItemFile>() {
        public ItemFile createFromParcel(Parcel in) {
            return new ItemFile(in);
        }

        public ItemFile[] newArray(int size) {
            return new ItemFile[size];
        }
    };

    private int idFile;
    private String namaFile;
    private int ukuranFile;
    private String tipeFile;

    public ItemFile(int idFile, String namaFile, int ukuranFile, String tipeFile) {
        this.idFile = idFile;
        this.namaFile = namaFile;
        this.ukuranFile = ukuranFile;
        this.tipeFile = tipeFile;
    }

    public ItemFile(Parcel input) {
        idFile = input.readInt();
        namaFile = input.readString();
        ukuranFile = input.readInt();
        tipeFile = input.readString();
    }

    public int getIdFile() {
        return idFile;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public int getUkuranFile() {
        return ukuranFile;
    }

    public String getTipeFile() {
        return tipeFile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idFile);
        dest.writeString(namaFile);
        dest.writeInt(ukuranFile);
        dest.writeString(tipeFile);
    }
}
