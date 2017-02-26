package com.ilife.shining.movingtrack.Fragment;


import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ilife.shining.movingtrack.Adapter.TrackPagerAdapter;
import com.ilife.shining.movingtrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TracksFragment extends Fragment {

    private ViewPager viewPager;
    private List<View> listTrackViews;
    private TrackPagerAdapter pagerAdapter;

    public TracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        initFragments(inflater, container);

        initViewPager();
        return view;
    }

    private void initFragments(LayoutInflater inflater, ViewGroup container) {
        listTrackViews = new ArrayList<View>();
        String fileName = "";
        //selection: 指定查询条件
        String selection = MediaStore.Images.Media.DATA + " like ?";
//设定查询目录
        String path = "/MovingTrack/tracks/";
//定义selectionArgs：
        String[] selectionArgs = {"%" + path + "%"};
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                selection, selectionArgs, null);
        while (cursor.moveToNext()) {

            View vTrack;
            ImageView ivTrack;
            vTrack = (View) inflater.inflate(R.layout.view_track_image, container, false);
            ivTrack = (ImageView) vTrack.findViewById(R.id.iv_track);

            int fileNameColum = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            fileName = cursor.getString(fileNameColum);

            ivTrack.setImageBitmap(BitmapFactory.decodeFile("/sdcard/MovingTrack/tracks/" + fileName));

            listTrackViews.add(vTrack);
        }

    }


    private void initViewPager() {
        pagerAdapter = new TrackPagerAdapter(listTrackViews);
        viewPager.setAdapter(pagerAdapter);
    }


}
