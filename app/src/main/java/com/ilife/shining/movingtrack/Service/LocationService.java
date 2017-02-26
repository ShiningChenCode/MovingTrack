package com.ilife.shining.movingtrack.Service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.ilife.shining.movingtrack.utils.UseDatabase;

public class LocationService extends Service  implements
        AMapLocationListener {
    private Context mContext;
    private final String ACTION_NAME = "SendLocationData";
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener =  this;
    // 声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation lastLocation;
    private UseDatabase mUseDatabase;
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = this;
        mUseDatabase = new UseDatabase(mContext);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand", "onStartCommand" );
        //声明AMapLocationClient类对象
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000);
//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
        return super.onStartCommand(intent, flags, startId);

    }

    private void saveLocationInfo(AMapLocation amapLocation) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("LATITUDE", amapLocation.getLatitude());
        initialValues.put("LONGITUDE", amapLocation.getLongitude());
        initialValues.put("SPEED", amapLocation.getSpeed());
        initialValues.put("TIME", amapLocation.getTime());

        mUseDatabase.insert("LOCATION_INFO", initialValues);
        Toast.makeText(mContext,amapLocation.getLatitude()+":"+amapLocation.getLongitude(),Toast.LENGTH_LONG).show();
        Log.d("saveLocationInfo", amapLocation.getLatitude() + ":" + amapLocation.getLongitude());
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLocationListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
              //  mLocationListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Toast.makeText(mContext,amapLocation.getLatitude()+":"+amapLocation.getLongitude(),Toast.LENGTH_LONG).show();
                Log.d("saveLocationInfo", amapLocation.getLatitude() + ":" + amapLocation.getLongitude());
                if(lastLocation==null||!(lastLocation.getLatitude() == amapLocation.getLatitude() && lastLocation.getLongitude() == amapLocation.getLongitude()))
                    saveLocationInfo(amapLocation);
                sendLocationData(amapLocation);
                lastLocation = amapLocation;
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                Toast.makeText(mContext,errText,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendLocationData(AMapLocation amapLocation) {
        Intent mIntent = new Intent(ACTION_NAME);
        mIntent.putExtra("latitude", amapLocation.getLatitude());
        mIntent.putExtra("longitude", amapLocation.getLongitude());
        //发送广播
        sendBroadcast(mIntent);
    }


}
