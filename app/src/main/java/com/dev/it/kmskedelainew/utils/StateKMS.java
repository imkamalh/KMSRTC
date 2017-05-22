package com.dev.it.kmskedelainew.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by jemsnaban on 4/21/2016.
 */
public class StateKMS {
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public static String getBgColor(int position){
        String[] bg_color = {"#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354",
                "#2BA195", "#5BBD76","#5bbd76", "#5793BA", "#8FDBB2", "#3CBAA7", "#74DB4F", "#207354", "#2BA195", "#5BBD76",
                "#74DB4F", "#207354", "#2BA195", "#5BBD76"};
        return bg_color[position];
    }
}
