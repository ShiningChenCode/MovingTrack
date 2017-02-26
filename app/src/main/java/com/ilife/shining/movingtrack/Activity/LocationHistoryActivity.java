package com.ilife.shining.movingtrack.Activity;

/**
 * file：       LocationHistoryActivity
 * Description：TODO
 * Author：     Shining Chen
 * Create Date：2016/1/16
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.ilife.shining.movingtrack.R;
import com.ilife.shining.movingtrack.model.LocationInfo;
import com.ilife.shining.movingtrack.utils.UseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


public class LocationHistoryActivity extends Activity {
    //声明变量
    private Context mContext;
    private MapView mapView;
    private AMap aMap;
    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    private UseDatabase mUseDatabase;
    private String startTime, endTime;
    private TextView tvNoDataStartTime, tvNoDataEndTime;
    private RelativeLayout rlNoDate;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        try {
            startTime = getIntent().getStringExtra("startTime");
            endTime = getIntent().getStringExtra("endTime");
        } catch (Exception e) {

        }

        setContentView(R.layout.activity_location_history);


        tvNoDataStartTime = (TextView) findViewById(R.id.tv_no_date_start_time);
        tvNoDataEndTime = (TextView) findViewById(R.id.tv_no_date_end_time);
        rlNoDate = (RelativeLayout) findViewById(R.id.rl_no_date);
        btnSave = (Button) findViewById(R.id.btn_save);

        //在onCreat方法中给aMap对象赋值
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写

        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
        mUseDatabase = new UseDatabase(mContext);

        initLocationData();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                getScreenHot(mapView, "/sdcard/MovingTrack/tracks/", System.currentTimeMillis() + ".png");

            }
        });

    }


    /**
     * 截屏
     *
     * @param v        视图
     * @param filePath 保存路径
     */
    private void getScreenHot(View v, String filePath, String fileName) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            v.draw(canvas);

            try {
                if (!isFolderExist(filePath)) {
                    File folder = new File(filePath);
                    folder.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(filePath + fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Toast.makeText(mContext, "截屏文件" + fileName + "已保存至" + filePath + " 下", Toast.LENGTH_LONG).show();

                noticePhotos(filePath);
            } catch (FileNotFoundException e) {
                throw new InvalidParameterException();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Indicates if this file represents a directory on the underlying file
     * system.
     *
     * @param directoryPath 文件夹路径
     * @return 文件夹是否存在
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    private void noticePhotos(String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        mContext.sendBroadcast(intent);//这个广播的目的就是更新图库
    }

    private void initLocationData() {

        List<LocationInfo> list = new ArrayList();
        if (startTime.isEmpty() | endTime.isEmpty()) {
            list = mUseDatabase.LocationInfoArray();
        } else {
            list = mUseDatabase.LocationInfoArrayByTime(startTime, endTime);
        }

        // String text = "#";
        if (list.size() > 1) {
            PolylineOptions polylineOptions = new PolylineOptions();
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            for (int i = 0; i < list.size(); i++) {
                //     text = text +list.get(i).getTime() + "#";
                LatLng latLng = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
                polylineOptions.add(latLng);
                boundsBuilder.include(latLng);
            }
            //       Toast.makeText(mContext,list.size()+ text,Toast.LENGTH_LONG).show();
            //      Log.d("LocationTime",text);
            polylineOptions.width(10);
            polylineOptions.color(Color.RED);
            mVirtureRoad = aMap.addPolyline(polylineOptions);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));
            markerOptions.position(polylineOptions.getPoints().get(0));
            mMoveMarker = aMap.addMarker(markerOptions);
            // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10));
            //  mMoveMarker.setRotateAngle((float) getAngle(0));


        } else {
            rlNoDate.setVisibility(View.VISIBLE);
            tvNoDataStartTime.setText(startTime);
            tvNoDataEndTime.setText(endTime);
        }


    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }


    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }


}
