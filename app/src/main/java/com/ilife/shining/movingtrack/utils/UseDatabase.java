package com.ilife.shining.movingtrack.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ilife.shining.movingtrack.model.LocationInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * file：       UseDatabase
 * Description：TODO
 * Author：     Shining Chen
 * Create Date：2016/1/16
 */
public class UseDatabase {

    Context context;
    DatabaseUtils dbhelper;
    public SQLiteDatabase sqlitedatabase;

    public UseDatabase(Context context)
    {
        super();
        this.context = context;
    }
    //打开数据库连接
    public void opendb(Context context)
    {
        dbhelper = new DatabaseUtils(context);
        sqlitedatabase = dbhelper.getWritableDatabase();
    }
    //关闭数据库连接
    public void closedb(Context context)
    {
        if(sqlitedatabase.isOpen())
        {
            sqlitedatabase.close();
        }
    }
    //插入表数据
    public void insert (String table_name,ContentValues values)
    {
        opendb(context);
        sqlitedatabase.insert(table_name, null, values);
        closedb(context);
    }
    //更新数据
    public int updatatable(String table_name,ContentValues values,int ID)
    {
        opendb(context);
        return sqlitedatabase.update(table_name, values, " Type_ID = ? ", new String[]{String.valueOf(ID)});
    }
    //删除表数据
    public void delete(String table_name)
    {
        opendb(context);
        try{

            sqlitedatabase.delete(table_name, null, null);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            closedb(context);
        }
    }


    //查找数据
    public List<LocationInfo> LocationInfoArray()
    {
        List<LocationInfo> list = new ArrayList();
        try{
            opendb(context);
            String sql = "SELECT * FROM LOCATION_INFO";
            Cursor c = sqlitedatabase.rawQuery(sql, null);
            if(c!=null)
            {
                while(c.moveToNext())
                {
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setLatitude(c.getDouble(c.getColumnIndex("LATITUDE")));
                    locationInfo.setLongitude(c.getDouble(c.getColumnIndex("LONGITUDE")));
                    locationInfo.setSpeed(c.getFloat(c.getColumnIndex("SPEED")));
                    locationInfo.setTime(c.getLong(c.getColumnIndex("TIME")));

                    list.add(locationInfo);
                }
                c.close();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        return list;
    }

    //查找数据
    public List<LocationInfo> LocationInfoArrayByTime(String startTime,String endTime)
    {
        List<LocationInfo> list = new ArrayList();

        try{
            opendb(context);

//            String sql = "SELECT  strftime('%s','" + endTime + "')*1000" ;
            String sql = "SELECT * FROM LOCATION_INFO where TIME < strftime('%s','" + endTime + "')*1000 - 28800000 and time > strftime('%s','"+ startTime + "')*1000 - 28800000 order by time asc";
            Cursor c = sqlitedatabase.rawQuery(sql, null);
            if(c!=null)
            {
                while(c.moveToNext())
                {
//                    c.getString(0);
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setLatitude(c.getDouble(c.getColumnIndex("LATITUDE")));
                    locationInfo.setLongitude(c.getDouble(c.getColumnIndex("LONGITUDE")));
                    locationInfo.setSpeed(c.getFloat(c.getColumnIndex("SPEED")));
                    locationInfo.setTime(c.getLong(c.getColumnIndex("TIME")));

                    list.add(locationInfo);
                }
                c.close();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        return list;
    }
}