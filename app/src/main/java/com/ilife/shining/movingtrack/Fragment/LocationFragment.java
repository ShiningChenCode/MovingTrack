package com.ilife.shining.movingtrack.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.ilife.shining.movingtrack.Activity.MainActivity;
import com.ilife.shining.movingtrack.R;
import com.ilife.shining.movingtrack.Service.LocationService;
import com.ilife.shining.movingtrack.model.LocationInfo;
import com.ilife.shining.movingtrack.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {
    private final String ACTION_NAME = "SendLocationData";
    List<LocationInfo> list = new ArrayList();
    private MapView mMapView;
    private AMap mAMap;
    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    private CommonTitleBar titleBar;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private RelativeLayout rlStartLocation;
    private Button btnStartLocation;
    private boolean isFirst = true;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        initViews(view);
        bindListeners();
        mMapView.onCreate(savedInstanceState);// 必须要写
        mAMap = mMapView.getMap();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式


        return view;
    }

    private void setLocation() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标


        mAMap.setMyLocationStyle(myLocationStyle);
//        mAMap.setLocationSource(this);// 设置定位监听
//        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    private void bindListeners() {
        btnStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationService.class);
                getContext().startService(intent);
                rlStartLocation.setVisibility(View.GONE);
                titleBar = ((MainActivity) getActivity()).titleBar;
                titleBar.setRightTxtBtn("停止定位");
                registerBoradcastReceiver();

                titleBar.setRightBtnOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), LocationService.class);
                        getContext().stopService(intent);
                        rlStartLocation.setVisibility(View.VISIBLE);
                        titleBar.setRightTxtBtn("");
                    }
                });
                //    setLocation();
            }
        });
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播

        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLongitude(intent.getDoubleExtra("longitude", 0));
                locationInfo.setLatitude(intent.getDoubleExtra("latitude", 0));
                list.add(locationInfo);
                initLocationData();
            }
        }

    };

    private void initViews(View view) {
        rlStartLocation = (RelativeLayout) view.findViewById(R.id.rl_start_location);
        btnStartLocation = (Button) view.findViewById(R.id.btn_start_location);
        mMapView = (MapView) view.findViewById(R.id.map);
    }

    private void initLocationData() {

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
            mVirtureRoad = mAMap.addPolyline(polylineOptions);
            addStartMarker(polylineOptions.getPoints().get(0));

            // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10));
            //  mMoveMarker.setRotateAngle((float) getAngle(0));


        } else if (list.size() == 1) {
            LatLng latLng = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
            addStartMarker(latLng);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        }

    }

    private void addStartMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));
        markerOptions.position(latLng);
        mMoveMarker = mAMap.addMarker(markerOptions);
    }


}
