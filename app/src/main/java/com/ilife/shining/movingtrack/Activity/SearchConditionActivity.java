package com.ilife.shining.movingtrack.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ilife.shining.movingtrack.Fragment.TimePickerFragment;
import com.ilife.shining.movingtrack.R;
import com.ilife.shining.movingtrack.widget.DoubleTimeDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchConditionActivity extends AppCompatActivity {
    Button btnShowPicker, btnSearch;
    EditText etShowTime;
    String startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_condition);
        btnShowPicker = (Button) findViewById(R.id.id_btn_show_picker);
        btnSearch = (Button) findViewById(R.id.id_btn_search);
        etShowTime = (EditText) findViewById(R.id.id_et_show_time);

        btnShowPicker.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {

                new DoubleTimeDatePickerDialog(SearchConditionActivity.this, 0, new DoubleTimeDatePickerDialog.OnDateSetListener() {

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
                        etShowTime.setText(textString);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchConditionActivity.this, LocationHistoryActivity.class);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                startActivity(intent);
            }
        });
    }

    public void showTimePickerDialog(View view) {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "timePicker");
    }
}
