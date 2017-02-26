package com.ilife.shining.movingtrack.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilife.shining.movingtrack.R;

/**
 * file：       CustmosTitleBar
 * Description：通用标题栏
 * Author：     Shining Chen
 * Create Date：2016/2/24
 */
public class CommonTitleBar extends RelativeLayout {

    // 防重复点击时间
    private static final int BTN_LIMIT_TIME = 500;

    private TextView leftButton;
    private ImageView leftButtonImg;
    private TextView middleButton;
    private TextView rightButton;
    private ImageView rightButtonImg;
    private RelativeLayout relativeLayoutTitleBar;

    private int    leftBtnIconId;
    private String leftBtnStr;
    private int    leftBtnTxtColor;
    private float  leftBtnTxtSize;

    private String titleTxtStr;
    private int    titleTxtColor;
    private int    titleTxtStyle;
    private float  titleTxtSize;

    private String rightBtnStr;
    private int    rightBtnTxtColor;
    private int    rightBtnIconId;
    private float  rightBtnTxtSize;

    private int    backgroundColor;

    public CommonTitleBar(Context context) {
        super(context);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);

        leftBtnStr = arr.getString(R.styleable.CommonTitleBar_leftBtnTxt);
        leftBtnTxtColor = arr.getColor(R.styleable.CommonTitleBar_leftBtnTxtColor, 0);
        leftBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_leftBtnIcon, 0);
        leftBtnTxtSize = arr.getDimension(R.styleable.CommonTitleBar_leftBtnTxtSize, 0);

        titleTxtStr = arr.getString(R.styleable.CommonTitleBar_titleTxt);
        titleTxtStyle = arr.getInt(R.styleable.CommonTitleBar_titleTxtstyle, 0);
        titleTxtColor = arr.getColor(R.styleable.CommonTitleBar_titleTxtColor, 0);
        titleTxtSize = arr.getDimension(R.styleable.CommonTitleBar_titleTxtSize, 0);

        rightBtnStr = arr.getString(R.styleable.CommonTitleBar_rightBtnTxt);
        rightBtnTxtColor = arr.getColor(R.styleable.CommonTitleBar_rightBtnTxtColor, 0);
        rightBtnIconId = arr.getResourceId(R.styleable.CommonTitleBar_rightBtnIcon, 0);
        rightBtnTxtSize = arr.getDimension(R.styleable.CommonTitleBar_rightBtnTxtSize,0);

        backgroundColor = arr.getColor(R.styleable.CommonTitleBar_backgroundColor, 0);
        if (isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_common_title_bar, this);
            return;
        }

        LayoutInflater.from(context).inflate(R.layout.view_common_title_bar, this);
        findViewById(R.id.title_out_frame).setBackgroundResource(R.color.light_blue_300);
        arr.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        leftButtonImg = (ImageView) findViewById(R.id.title_left_btn);
        leftButton = (TextView) findViewById(R.id.title_left);
        middleButton = (TextView) findViewById(R.id.title_middle);
        rightButtonImg = (ImageView) findViewById(R.id.title_right_btn);
        rightButton = (TextView) findViewById(R.id.title_right);
        relativeLayoutTitleBar = (RelativeLayout) findViewById(R.id.title_out_frame);

        if (leftBtnIconId != 0) {
            leftButtonImg.setImageResource(leftBtnIconId);
            leftButtonImg.setVisibility(View.VISIBLE);
        } else {
            leftButtonImg.setVisibility(View.GONE);
        }
        if (rightBtnIconId != 0) {
            rightButtonImg.setImageResource(rightBtnIconId);
            rightButtonImg.setVisibility(View.VISIBLE);
        } else {
            rightButtonImg.setVisibility(View.GONE);
        }
        //背景色
        if (backgroundColor != 0) {
            relativeLayoutTitleBar.setBackgroundColor(backgroundColor);
        }
        //标题字体加粗
        if (titleTxtStyle != 0) {
            TextPaint tp = middleButton.getPaint();
            tp.setFakeBoldText(true);
        }

        //字体背景色
        if (titleTxtColor != 0) {
            middleButton.setTextColor(titleTxtColor);
        }
        if (leftBtnTxtColor != 0) {
            leftButton.setTextColor(leftBtnTxtColor);
        }
        if (rightBtnTxtColor != 0) {
            rightButton.setTextColor(rightBtnTxtColor);
        }

        //字体大小
        if (titleTxtSize != 0) {
            middleButton.setTextSize(titleTxtSize);
        }
        if (leftBtnTxtSize != 0) {
            leftButton.setTextSize(titleTxtSize);
        }
        if (rightBtnTxtSize != 0) {
            rightButton.setTextSize(titleTxtSize);
        }


        setLeftTxtBtn(leftBtnStr);
        setTitleTxt(titleTxtStr);
        setRightTxtBtn(rightBtnStr);
    }

    public void setRightTxtBtn(String btnTxt) {
        if (!TextUtils.isEmpty(btnTxt)) {
            rightButton.setText(btnTxt);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }

    public void setLeftTxtBtn(String leftBtnStr) {
        if (!TextUtils.isEmpty(leftBtnStr)) {
            leftButton.setText(leftBtnStr);
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }

    public void setTitleTxt(String title) {
        if (!TextUtils.isEmpty(title)) {
            middleButton.setText(title);
            middleButton.setVisibility(View.VISIBLE);
        } else {
            middleButton.setVisibility(View.GONE);
        }
    }

    public void hideLeftBtn() {
        leftButton.setVisibility(View.GONE);
        leftButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_left_area).setOnClickListener(null);
    }

    public void hideRightBtn() {
        rightButton.setVisibility(View.GONE);
        rightButtonImg.setVisibility(View.GONE);
        findViewById(R.id.title_right_area).setOnClickListener(null);
    }

    public void setLeftBtnOnclickListener(OnClickListener listener) {
        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_left_area).setOnClickListener(myListener);
    }

    public void setRightBtnOnclickListener(OnClickListener listener) {
        OnClickListener myListener = new GlobalLimitClickOnClickListener(listener, BTN_LIMIT_TIME);
        findViewById(R.id.title_right_area).setOnClickListener(myListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec , heightMeasureSpec );
    }
}


/**
 * 全局防止频繁点击代理Listener
 *
 */
  class GlobalLimitClickOnClickListener implements View.OnClickListener {

    // 全局防频繁点击
    private static long     lastClick;

    private View.OnClickListener listener;

    private long            intervalClick;

    public GlobalLimitClickOnClickListener(View.OnClickListener listener, long intervalClick) {
        this.intervalClick = intervalClick;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() > lastClick
                && System.currentTimeMillis() - lastClick <= intervalClick) {
            return;
        }
        listener.onClick(v);
        lastClick = System.currentTimeMillis();
    }
}

