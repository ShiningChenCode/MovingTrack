package com.ilife.shining.movingtrack.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * file：       TrackPagerAdapter
 * Description：TODO
 * Author：     Shining Chen
 * Create Date：2016/3/2
 */
public class TrackPagerAdapter extends PagerAdapter {

    List<View> viewLists;

    public TrackPagerAdapter(List<View> lists) {
        viewLists = lists;
    }

    @Override
    public int getCount() {                                                                 //获得size
        // TODO Auto-generated method stub
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewLists.get(position));
        return viewLists.get(position);
    }


}
