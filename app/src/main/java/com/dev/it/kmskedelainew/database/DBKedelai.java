package com.dev.it.kmskedelainew.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by jems on 14-Sep-15.
 */

public class DBKedelai {
    public static final int ALL_MISSIONS = 0;
    public static final int MY_MISSIONS = 1;
    private MissionsHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBKedelai(Context context) {
        mHelper = new MissionsHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }


    private static class MissionsHelper extends SQLiteOpenHelper {
        public static final String TABLE_ALL_MISSIONS = "all_missions";
        public static final String TABLE_MY_MISSIONS = "my_missions";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_NAMATEMPAT = "nama_tempat";
        public static final String COLUMN_NAMA = "nama_mission";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_REWARD = "reward";

        private static final String CREATE_TABLE_ALL_MISSIONS = "CREATE TABLE " + TABLE_ALL_MISSIONS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAMATEMPAT + " TEXT," +
                COLUMN_NAMA + " TEXT," +
                COLUMN_LATITUDE + " INTEGER," +
                COLUMN_LONGITUDE + " INTEGER," +
                COLUMN_REWARD + " INTEGER" +
                ");";
        private static final String CREATE_TABLE_MY_MISSIONS = "CREATE TABLE " + TABLE_MY_MISSIONS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAMATEMPAT + " TEXT," +
                COLUMN_NAMA + " TEXT," +
                COLUMN_LATITUDE + " INTEGER," +
                COLUMN_LONGITUDE + " INTEGER," +
                COLUMN_REWARD + " INTEGER" +
                ");";
        private static final String DB_NAME = "missions_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public MissionsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ALL_MISSIONS);
                db.execSQL(CREATE_TABLE_MY_MISSIONS);
                Log.d("query", CREATE_TABLE_ALL_MISSIONS);
            } catch (SQLiteException exception) {
                Log.d("exception", exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.d("statement","upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_ALL_MISSIONS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_MY_MISSIONS + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                Log.d("exception", exception + "");
            }
        }
    }
}
