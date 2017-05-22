package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class ItemPengolahan implements Parcelable {
    public static final Creator<ItemPengolahan> CREATOR
            = new Creator<ItemPengolahan>() {
        public ItemPengolahan createFromParcel(Parcel in) {
            return new ItemPengolahan(in);
        }

        public ItemPengolahan[] newArray(int size) {
            return new ItemPengolahan[size];
        }
    };

    private int idItem;
    private String namaItem;
    private String warnaBg;
    private String imagePengolahan;

    public ItemPengolahan(Parcel input) {
        idItem = input.readInt();
        namaItem = input.readString();
        warnaBg = input.readString();
        imagePengolahan = input.readString();
    }

    public ItemPengolahan(int idItem, String namaItem, String imagePengolahan, String warnaBg) {
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.warnaBg = warnaBg;
        this.imagePengolahan = imagePengolahan;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public String getWarnaBg() {
        return warnaBg;
    }

    public String getImagePengolahan() {
        return imagePengolahan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idItem);
        dest.writeString(namaItem);
        dest.writeString(warnaBg);
        dest.writeString(imagePengolahan);
    }
}
