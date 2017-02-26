package com.ilife.shining.movingtrack.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * file：       DatabaseUtils
 * Description：TODO
 * Author：     Shining Chen
 * Create Date：2016/1/16
 */
public class DatabaseUtils extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;  //数据库版本号
    private static final String DATABASE_NAME    = "MovingTrack";  //数据库名称
    private  static final String LOCATION_INFO    = "LOCATION_INFO";//定位信息

    public DatabaseUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqldept = "create table LOCATION_INFO(LATITUDE DOUBLE,LONGITUDE DOUBLE,SPEED FLOAT,TIME LONG)";
        db.execSQL(sqldept);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
