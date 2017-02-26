/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ilife.shining.movingtrack.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.ilife.shining.movingtrack.R;

/**
 * A simple dialog containing an {@link android.widget.DatePicker}.
 * <p/>
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
public class DoubleTimeDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private static final String START_YEAR = "start_year";
    private static final String END_YEAR = "end_year";
    private static final String START_MONTH = "start_month";
    private static final String END_MONTH = "end_month";
    private static final String START_DAY = "start_day";
    private static final String END_DAY = "end_day";
    private static final String START_HOURS = "start_hours";
    private static final String END_HOURS = "end_hours";
    private static final String START_MINUTE = "start_minute";
    private static final String END_MINUTE = "end_minute";

    private final DatePicker mDatePicker_start;
    private final DatePicker mDatePicker_end;
    private final TimePicker mTimePicker_start;
    private final TimePicker mTimePicker_end;
    private final OnDateSetListener mCallBack;


    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {


        void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                       TimePicker startTimePicker, int startHour, int startMinute,
                       DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth,
                       TimePicker endTimePicker, int endHour, int endtMinute);
    }

    /**
     * @param context     The context the dialog is to run in.
     * @param callBack    How the parent is notified that the date is set.
     * @param year        The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth  The initial day of the dialog.
     */
    public DoubleTimeDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth,int hour,int minute) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth ,hour,minute );
    }

    public DoubleTimeDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                      int dayOfMonth,int hour,int minute) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, hour,minute, true);

    }

    /**
     * @param context     The context the dialog is to run in.
     * @param theme       the theme to apply to this dialog
     * @param callBack    How the parent is notified that the date is set.
     * @param year        The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth  The initial day of the dialog.
     */
    public DoubleTimeDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                      int dayOfMonth,int hour,int minute, boolean isDayVisible) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, "确 定", this);
        setButton(BUTTON_NEGATIVE, "取 消", this);
        // setButton(BUTTON_POSITIVE,
        // themeContext.getText(android.R.string.date_time_done), this);
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_double_time_date_picker, null);
        setView(view);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
        mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);
        mTimePicker_start = (TimePicker) view.findViewById(R.id.timePickerStart);
        mTimePicker_end = (TimePicker) view.findViewById(R.id.timePickerend);
        mTimePicker_start.setIs24HourView(true);
        mTimePicker_end.setIs24HourView(true);
        mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
        mDatePicker_end.init(year, monthOfYear, dayOfMonth, this);

        resizePicker(mDatePicker_start);
        resizePicker(mTimePicker_start);
        resizePicker(mDatePicker_end);
        resizePicker(mTimePicker_end);

        // updateTitle(year, monthOfYear, dayOfMonth);

        // 如果要隐藏当前日期，则使用下面方法。
        if (!isDayVisible) {
            hidDay(mDatePicker_start);
            hidDay(mDatePicker_end);
        }
    }

    /**
     * 隐藏DatePicker中的日期显示
     *
     * @param mDatePicker
     */
    private void hidDay(DatePicker mDatePicker) {
        Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                // datePicker.getCalendarView().setVisibility(View.GONE);
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        // Log.d(this.getClass().getSimpleName(), String.format("which:%d",
        // which));
        // 如果是“取 消”按钮，则返回，如果是“确 定”按钮，则往下执行
        if (which == BUTTON_POSITIVE)
            tryNotifyDateSet();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (view.getId() == R.id.datePickerStart)
            mDatePicker_start.init(year, month, day, this);
        if (view.getId() == R.id.datePickerEnd)
            mDatePicker_end.init(year, month, day, this);
        // updateTitle(year, month, day);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        if (view.getId() == R.id.datePickerStart)
            mTimePicker_start.setCurrentHour(hourOfDay);
        mTimePicker_start.setCurrentMinute(minute);
        if (view.getId() == R.id.datePickerEnd)
            mTimePicker_end.setCurrentHour(hourOfDay);
        mTimePicker_end.setCurrentMinute(minute);
    }


    /**
     * 获得开始日期的DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerStart() {
        return mDatePicker_start;
    }

    /**
     * 获得结束日期的DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerEnd() {
        return mDatePicker_end;
    }

    /**
     * Sets the start date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Sets the end date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker_start.clearFocus();
            mDatePicker_end.clearFocus();
            mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                    mDatePicker_start.getDayOfMonth(),
                    mTimePicker_start, mTimePicker_start.getCurrentHour(), mTimePicker_start.getCurrentMinute(),
                    mDatePicker_end, mDatePicker_end.getYear(),
                    mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth(),
                    mTimePicker_end, mTimePicker_end.getCurrentHour(), mTimePicker_end.getCurrentMinute());
        }
    }

    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, mDatePicker_start.getYear());
        state.putInt(START_MONTH, mDatePicker_start.getMonth());
        state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
        state.putInt(END_YEAR, mDatePicker_end.getYear());
        state.putInt(END_MONTH, mDatePicker_end.getMonth());
        state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt(START_YEAR);
        int start_month = savedInstanceState.getInt(START_MONTH);
        int start_day = savedInstanceState.getInt(START_DAY);
        mDatePicker_start.init(start_year, start_month, start_day, this);

        int end_year = savedInstanceState.getInt(END_YEAR);
        int end_month = savedInstanceState.getInt(END_MONTH);
        int end_day = savedInstanceState.getInt(END_DAY);
        mDatePicker_end.init(end_year, end_month, end_day, this);

    }


    public static void resizePicker(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    private static void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }
}