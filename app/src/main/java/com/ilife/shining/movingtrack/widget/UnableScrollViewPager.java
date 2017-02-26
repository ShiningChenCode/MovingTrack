package com.ilife.shining.movingtrack.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * file：       UnableScrollViewPager
 * Description：禁止手势滑动
 * Author：     Shining Chen
 * Create Date：2016/3/1
 */
public class UnableScrollViewPager extends ViewPager {
    public UnableScrollViewPager(Context context) {
        super(context);
    }


    public UnableScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
