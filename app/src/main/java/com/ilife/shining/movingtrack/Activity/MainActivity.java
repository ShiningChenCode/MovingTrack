package com.ilife.shining.movingtrack.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ilife.shining.movingtrack.Fragment.LocationFragment;
import com.ilife.shining.movingtrack.Fragment.MoreFragment;
import com.ilife.shining.movingtrack.Fragment.TracksFragment;
import com.ilife.shining.movingtrack.R;
import com.ilife.shining.movingtrack.Service.LocationService;
import com.ilife.shining.movingtrack.widget.CommonTitleBar;
import com.ilife.shining.movingtrack.widget.DoubleTimeDatePickerDialog;
import com.ilife.shining.movingtrack.widget.UnableScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //声明变量
    private Context mContext;
    public CommonTitleBar titleBar;
    private UnableScrollViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> list_Fragments;
    private LinearLayout linearLayoutHome, linearLayoutTrack, linearLayoutMore;
    private TextView tvHome, tvTrack, tvMore;
    private String startTime, endTime;
    private boolean isServiceStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isServiceStart = isServiceWork(mContext, "com.ilife.shining.movingtrack.Service.LocationService");
        setContentView(R.layout.activity_main);
        initViews();
        initFragments();
        initViewPager();
        bindListeners();
    }

    private void initViewPager() {
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_Fragments.get(position);
            }

            @Override
            public int getCount() {
                return list_Fragments.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    private void initFragments() {
        Fragment fragmentHome = new LocationFragment();
        Fragment fragmentTrack = new TracksFragment();
        Fragment fragmentMore = new MoreFragment();

        list_Fragments = new ArrayList<Fragment>();
        list_Fragments.add(fragmentHome);
        list_Fragments.add(fragmentTrack);
        list_Fragments.add(fragmentMore);
    }

    private void initViews() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        viewPager = (UnableScrollViewPager) findViewById(R.id.viewpager);
        linearLayoutHome = (LinearLayout) findViewById(R.id.ll_home);
        linearLayoutTrack = (LinearLayout) findViewById(R.id.ll_track);
        linearLayoutMore = (LinearLayout) findViewById(R.id.ll_more);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvTrack = (TextView) findViewById(R.id.tv_track);
        tvMore = (TextView) findViewById(R.id.tv_more);



        linearLayoutHome.setOnClickListener(this);
        linearLayoutTrack.setOnClickListener(this);
        linearLayoutMore.setOnClickListener(this);

    }

    private void bindListeners() {


        titleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DoubleTimeDatePickerDialog(MainActivity.this, 0, new DoubleTimeDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, TimePicker startTimePicker, int startHour, int startMinute, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth, TimePicker endTimePicker, int endHour, int endMinute) {

                        calendar.set(startYear, startMonthOfYear,
                                startDayOfMonth, startHour,
                                startMinute, 0);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        startTime = sdf.format(calendar.getTime());
                        calendar.set(endYear, endMonthOfYear,
                                endDayOfMonth, endHour,
                                endMinute, 0);
                        endTime = sdf.format(calendar.getTime());
                        String textString = "开始时间：" + startTime + "\n结束时间：" + endTime;
                        Intent intent = new Intent(MainActivity.this, LocationHistoryActivity.class);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endTime", endTime);
                        startActivity(intent);


                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();

            }
        });
    }
//
//        btnSearchHistoryLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SearchConditionActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnIsOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,String.valueOf(isServiceWork(mContext,"com.ilife.shining.movingtrack.Service.LocationService")),Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void initViews() {
//        btnLocation = (Button) findViewById(R.id.id_btn_location);
//        btnIsOpen = (Button) findViewById(R.id.id_btn_is_open);
//        btnLocation = (Button) findViewById(R.id.id_btn_location);
//        btnSearchHistoryLocations = (Button) findViewById(R.id.id_btn_search_history_locations);
//
//        if (isServiceStart) {
//            btnLocation.setText("关闭定位服务");
//        } else {
//
//            btnLocation.setText("开启定位服务");
//        }
//    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */

    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        // 设置一个默认Service的数量大小
        int defaultNum = 40;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(defaultNum);
        while (defaultNum == myList.size()) {
            defaultNum += 20;
            myList = myAM.getRunningServices(defaultNum);
        }
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_track:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_more:
                viewPager.setCurrentItem(2);
                break;
            case R.id.title_left_area:
                Calendar c = Calendar.getInstance();

                break;
        }

    }
}
