package com.github.iron.chart.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author iron
 *         created at 2018/7/19
 */
public class PieDataView extends View implements BasePieView.IPieDataView {

    //控件属性
    private int mWidth;
    private int mHeight;
    //数据
    private float[] mValues;
    private String[] mNames;
    private int[] mColors;
    //显示位数
    private NumberFormat mValueFormat;
    //颜色画笔
    private Paint mPaintColor;
    //文字画笔
    private Paint mPaintText;
    //每行显示的数量
    private int mColumnNumber;
    //边距
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    //坐标点X坐标
    private float[] mPointXCoordinate;
    //每行直接的间距
    private int mTextSpacing;
    //显示格式
    private String mShowFormat;

    //文字属性
    private final static int DEFAULT_TEXT_SIZE = 12;
    private final static int DEFAULT_TEXT_COLOR = Color.BLACK;
    //默认列数
    private final static int DEFAULT_COLUMN_NUMBER = 1;
    //默认边距
    private final static int DEFAULT_PADDING = 5;
    //每行直接的间距
    private final static int DEFAULT_TEXT_SPACING = 8;
    //默认显示样式
    private final static String DEFAULT_SHOW_FORMAT = "{name} {value}";


    public PieDataView(Context context) {
        this(context, null);
    }

    public PieDataView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //初始数据
        mValueFormat = NumberFormat.getInstance(Locale.CHINA);
        mColumnNumber = DEFAULT_COLUMN_NUMBER;
        mTextSpacing = dp2px(DEFAULT_TEXT_SPACING);
        mShowFormat = DEFAULT_SHOW_FORMAT;

        //初始化画笔
        //颜色画笔
        mPaintColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintColor.setStyle(Paint.Style.FILL);

        //文字画笔
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextSize(sp2px(DEFAULT_TEXT_SIZE));
        mPaintText.setColor(DEFAULT_TEXT_COLOR);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        int defaultPadding = dp2px(DEFAULT_PADDING);
        mPaddingLeft = Math.max(getPaddingLeft(), defaultPadding);
        mPaddingRight = Math.max(getPaddingRight(), defaultPadding);
        mPaddingTop = Math.max(getPaddingTop(), defaultPadding);

        //初始化X坐标
        resetPointX();
    }

    /**
     * 重置X坐标
     */
    private void resetPointX(){
        //设置X坐标
        mPointXCoordinate = new float[mColumnNumber];
        //计算每块区域的宽度
        float temp = (mWidth - mPaddingLeft - mPaddingRight) / mColumnNumber;
        //遍历
        for (int i = 0; i < mColumnNumber; i++) {
            mPointXCoordinate[i] = mPaddingLeft + temp * i;
        }
    }

    /**
     * 绘制方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果有数据
        if (mValues == null || mValues.length == 0) {
            return;
        }
        //计算每行的高度
        float rh = getPaintHeight(mPaintText, mNames[0] + " " + mValues[0]);
        //当前行
        int row = 0;
        //文字和色块之间的距离
        int spacing = dp2px(5);
        //方块的大小
        float colorSize = rh * 2 / 3;
        float colorPadding = rh / 4;
        //遍历数据
        for (int i = 0; i < mValues.length; ) {
            //遍历每行
            for (float x : mPointXCoordinate) {
                if(i >= mValues.length){
                    break;
                }
                //绘制颜色
                mPaintColor.setColor(mColors[i]);
                //边距
                float paddingTop = mPaddingTop + (rh + mTextSpacing) * row;
                //绘制方块
                canvas.drawRect(x, paddingTop + colorPadding, x + colorSize, paddingTop + colorPadding + colorSize, mPaintColor);
                //绘制文字
                String text = mShowFormat.replace("{name}", mNames[i]).replace("{value}",mValueFormat.format(mValues[i]));
                canvas.drawText(text, x + colorSize + spacing, paddingTop + rh, mPaintText);
                //递增
                i++;
            }
            row++;
        }
    }

    /**
     * 设置数据
     */
    public void setData(float[] value, String[] name, int[] color, int digits) {
        mValues = value;
        mNames = name;
        mColors = color;
        mValueFormat.setMaximumFractionDigits(digits);
        mValueFormat.setMinimumFractionDigits(digits);

        postInvalidate();
    }

    /**
     * 设置列数
     */
    public void setColumnNumber(int number){
        mColumnNumber = number;

        resetPointX();

        postInvalidate();
    }

    /**
     * 设置文字画笔属性
     */
    public void setTextPaint(float spSize,@ColorInt int color){
        mPaintText.setTextSize(sp2px(spSize));
        mPaintText.setColor(color);

        postInvalidate();
    }

    /**
     * 设置文字之间的间隔
     */
    public void setTextSpacing(float dpSize){
        mTextSpacing = dp2px(dpSize);

        postInvalidate();
    }

    /**
     * 显示样式
     * @param format 默认为({name} {value})
     */
    public void setShowFormat(String format){
        mShowFormat = format;
    }

    /**
     * dp2px
     */
    private int dp2px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * sp2px
     */
    private int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 测量画笔高度
     */
    private float getPaintHeight(Paint paint,String text){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

}
