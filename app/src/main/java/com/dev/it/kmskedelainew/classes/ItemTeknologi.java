package com.dev.it.kmskedelainew.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class ItemTeknologi implements Parcelable {
    public static final Creator<ItemTeknologi> CREATOR
            = new Creator<ItemTeknologi>() {
        public ItemTeknologi createFromParcel(Parcel in) {
            return new ItemTeknologi(in);
        }

        public ItemTeknologi[] newArray(int size) {
            return new ItemTeknologi[size];
        }
    };

    private int idItem;
    private String namaItem;
    private String warnaBg;
    private String imageTeknologi;

    public ItemTeknologi(Parcel input) {
        idItem = input.readInt();
        namaItem = input.readString();
        warnaBg = input.readString();
        imageTeknologi = input.readString();
    }

    public ItemTeknologi(int idItem, String namaItem, String imagePengolahan, String warnaBg) {
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.warnaBg = warnaBg;
        this.imageTeknologi = imagePengolahan;
    }

    public ItemTeknologi(int idItem, String namaItem, String warnaBg) {
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.warnaBg = warnaBg;
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

    public String getImageTeknologi() {
        return imageTeknologi;
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
        dest.writeString(imageTeknologi);
    }
}
