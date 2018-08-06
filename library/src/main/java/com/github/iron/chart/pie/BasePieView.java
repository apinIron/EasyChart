package com.github.iron.chart.pie;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

/**
 * @author iron
 *         created at 2018/7/18
 */
public abstract class BasePieView extends View {

    //控件宽高
    protected int mWidth;
    protected int mHeight;
    //边距
    protected float mPadding;
    //饼图的圆点
    protected int mRadius;
    //数值
    private float[] mValues;
    //每个点的名称
    private String[] mNames;
    //每个数据的颜色
    private int[] mColors;
    //每个数值的占比
    private float[] mValuePercents;
    //当前数值总值
    private float mTotalValue;
    //标题
    private String mTitle;
    //数值位数
    private NumberFormat mValueFormat;
    //百分比格式
    private NumberFormat mPercentFormat;
    //标题画笔
    protected Paint mPaintTitle;
    //总值画笔
    protected Paint mPaintTotalValue;
    //文字之间的间距
    protected int mTextSpacing;
    //圆弧间隔角度
    private float mArcIntervalAngle;
    //动画时长
    private long mProgressAnimTime;
    //当前进度角度
    private float mProgressAngle;
    //动画样式
    private int mAnimStyle;
    //数据控件
    private IPieDataView mPieDataView;

    // 进度动画时长
    private final static long DEFAULT_PROGRESS_ANIM_TIME = 1500;
    //中间文字之间的间距
    private static final int DEFAULT_TEXT_SPACING = 7;
    // 默认控件大小
    private final static int DEFAULT_SIZE = 180;
    // 默认边距
    private final static int DEFAULT_PADDING = 5;
    //默认位数
    private final static int DEFAULT_DISPLAY_DIGITS = 0;
    //标题画笔属性
    private final static int DEFAULT_TITLE_TEXT_SIZE = 12;
    private final static int DEFAULT_TITLE_TEXT_COLOR = Color.GRAY;
    //总值画笔属性
    private final static int DEFAULT_TOTAL_VALUE_TEXT_SIZE = 18;
    private final static int DEFAULT_TOTAL_VALUE_TEXT_COLOR = Color.BLACK;
    //动画样式
    public final static int ANIM_STYLE_SCALE = 1;
    public final static int ANIM_STYLE_TRANSLATE = 2;


    public BasePieView(Context context) {
        this(context, null);
    }

    public BasePieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BasePieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //默认数据
        mTextSpacing = dp2px(DEFAULT_TEXT_SPACING);
        mProgressAnimTime = DEFAULT_PROGRESS_ANIM_TIME;
        mAnimStyle = ANIM_STYLE_TRANSLATE;
        mValueFormat = NumberFormat.getInstance(Locale.CHINA);
        mValueFormat.setMaximumFractionDigits(DEFAULT_DISPLAY_DIGITS);
        mValueFormat.setMinimumFractionDigits(DEFAULT_DISPLAY_DIGITS);
        mPercentFormat = NumberFormat.getPercentInstance();
        mPercentFormat.setMaximumFractionDigits(DEFAULT_DISPLAY_DIGITS);
        mPercentFormat.setMinimumFractionDigits(DEFAULT_DISPLAY_DIGITS);

        //初始化画笔
        //标题画笔
        mPaintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTitle.setTextAlign(Paint.Align.CENTER);
        mPaintTitle.setTextSize(sp2px(DEFAULT_TITLE_TEXT_SIZE));
        mPaintTitle.setColor(DEFAULT_TITLE_TEXT_COLOR);

        // 总值画笔
        mPaintTotalValue = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTotalValue.setTextAlign(Paint.Align.CENTER);
        mPaintTotalValue.setTextSize(sp2px(DEFAULT_TOTAL_VALUE_TEXT_SIZE));
        mPaintTotalValue.setColor(DEFAULT_TOTAL_VALUE_TEXT_COLOR);

        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //屏幕宽高
        mWidth = w;
        mHeight = h;
        //圆点
        mRadius = (int) (Math.min(mWidth, mHeight) / 2 - mPadding);
        //设置边缘
        initPieRect(mRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = dp2px(DEFAULT_SIZE);
        mPadding = Math.max(Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom()));
        mPadding = Math.max(dp2px(DEFAULT_PADDING), mPadding);

        setMeasuredDimension(measureSize(widthMeasureSpec, size),measureSize(heightMeasureSpec, size));
    }

    /**
     * 判断当前控件宽高类型
     */
    private int measureSize(int measureSpec, int defaultSize) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                defaultSize = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return defaultSize;
    }

    /**
     * 绘制方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制饼图
        drawPie(canvas);

        //绘制标题
        drawTitle(canvas, mTitle == null ? "" : mTitle, mValueFormat.format(mTotalValue));
    }

    /**
     * 绘制饼图
     */
    private void drawPie(Canvas canvas) {
        if(mValues == null || mValues.length == 0){
            return;
        }
        canvas.translate(mWidth / 2,mHeight / 2);
        //起始度数
        float startAngle = -90;
        float totalAngle = 360;
        //绘制饼图
        for (int i = 0; i < mValues.length; i++) {
            float percent = mValuePercents[i];
            String dValue = mValueFormat.format(mValues[i]);
            String dPercent = mPercentFormat.format(percent);
            if(percent != 0) {
                float sweepAngle;
                //判断动画类型
                if(mAnimStyle == ANIM_STYLE_SCALE) {
                    //如果是最后一个
                    if (i == mValues.length - 1) {
                        //直接绘制到最后
                        sweepAngle = mProgressAngle - 90 - startAngle - mArcIntervalAngle;
                    } else {
                        //根据比例绘制
                        sweepAngle = percent * (mProgressAngle - mArcIntervalAngle * mValues.length);
                    }
                } else {
                    //如果是最后一个
                    if( i == mValues.length - 1){
                        sweepAngle = 270 - startAngle - mArcIntervalAngle;
                    }else{
                        sweepAngle = percent * (totalAngle - mArcIntervalAngle * mValues.length);
                    }
                    //如果超出应该显示的范围
                    if(startAngle + sweepAngle > mProgressAngle - 90){
                        sweepAngle = mProgressAngle - 90 - startAngle;
                        //绘制圆弧
                        drawPieArc(canvas, dValue, dPercent, mNames[i], mColors[i], startAngle, sweepAngle);
                        break;
                    }
                }
                //绘制圆弧
                drawPieArc(canvas, dValue, dPercent, mNames[i], mColors[i], startAngle, sweepAngle);
                //修改下一个圆弧的起点
                startAngle += sweepAngle + mArcIntervalAngle;
            }
        }
    }

    /**
     * 设置数据
     */
    public void setData(float[] values, boolean anim) {
        setData(values, null, null, anim);
    }

    /**
     * 设置数据（包含数值，颜色）
     */
    public void setData(float[] values, int[] colors, boolean anim) {
        setData(values, null, colors, anim);
    }

    /**
     * 设置数据（包含数值，名字）
     */
    public void setData(float[] values, String[] name, boolean anim) {
        setData(values, name, null, anim);
    }

    /**
     * 设置数据（包含数值，名字，颜色）
     */
    public void setData(float[] values, String[] name, int[] color, boolean anim) {
        if(values == null){
            return;
        }
        mTotalValue = 0;
        //计算当前总值
        for (float value : values) {
            mTotalValue += value;
        }
        //初始化数组
        mValues = values;
        mNames = new String[mValues.length];
        mColors = new int[mValues.length];
        mValuePercents = new float[mValues.length];
        Random random = new Random();
        //遍历设置数据
        for (int i = 0; i < values.length; i++) {
            //设置占比
            mValuePercents[i] = values[i] / mTotalValue;
            //设置名字
            mNames[i] = name == null || i >= name.length ? "" : name[i];
            //设置颜色
            mColors[i] = color == null || i >= color.length ?
                    Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)) : color[i];
        }

        //如果需要动画
        if (anim) {
            startProgressAnim();
        } else {
            mProgressAngle = 360;
            //进行重绘
            postInvalidate();
        }
        //设置数据界面数据
        if(mPieDataView != null){
            mPieDataView.setData(values, name, color, mValueFormat.getMaximumFractionDigits());
        }
    }

    /**
     * 启动进度动画
     */
    private void startProgressAnim() {
        //启动角度变动动画
        ValueAnimator angleAnim = ValueAnimator.ofFloat(0, 360);
        angleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        angleAnim.setDuration(mProgressAnimTime);
        angleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //设置当进度
                mProgressAngle = (float) valueAnimator.getAnimatedValue();

                postInvalidate();
            }
        });
        angleAnim.start();
    }

    /**
     * 设置标题
     */
    public void setTitle(String title){
        mTitle = title;

        postInvalidate();
    }

    /**
     * 设置显示的小数点后位数
     */
    public void setDisplayDigits(int digits){
        mValueFormat.setMinimumFractionDigits(digits);
        mValueFormat.setMaximumFractionDigits(digits);
        mPercentFormat.setMinimumFractionDigits(digits);
        mPercentFormat.setMaximumFractionDigits(digits);

        postInvalidate();
    }

    /**
     * 设置文字之间的间隔
     */
    public void setTextSpacing(int spacingDp){
        mTextSpacing = dp2px(spacingDp);

        postInvalidate();
    }

    /**
     * 设置进度条动画时长
     */
    public void setProgressAnimTime(long time){
        mProgressAnimTime = time;
    }

    /**
     * 设置动画样式
     */
    public void setAnimStyle(int style){
        mAnimStyle = style;
    }

    /**
     * 设置标题画笔属性
     */
    public void setTitlePaint(float spSize, @ColorInt int color){
        mPaintTitle.setTextSize(sp2px(spSize));
        mPaintTitle.setColor(color);

        postInvalidate();
    }

    /**
     * 设置总值画笔属性
     */
    public void setTotalValuePaint(float spSize, @ColorInt int color){
        mPaintTotalValue.setTextSize(sp2px(spSize));
        mPaintTotalValue.setColor(color);

        postInvalidate();
    }

    /**
     * 绑定饼图的数据界面
     */
    public void attachPieDataView(IPieDataView view){
        mPieDataView = view;
    }

    /**
     * 设置圆弧之间间隔角度
     */
    protected void setArcIntervalAngle(float intervalAngle){
        mArcIntervalAngle = intervalAngle;
    }

    /**
     * dp2px
     */
    protected int dp2px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * sp2px
     */
    protected int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 测量画笔高度
     */
    protected float getPaintHeight(Paint paint,String text){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * 获取画笔宽度
     */
    protected float getPaintWidth(Paint paint,String text){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    protected abstract void initView();

    protected abstract void initPieRect(float radius);

    protected abstract void drawPieArc(Canvas canvas, String value,String percent, String name, int color, float arcStartAngle, float sweepAngle);

    protected abstract void drawTitle(Canvas canvas,String title,String totalValue);

    public interface IPieDataView{

        void setData(float[] value, String[] name, int[] color, int digits);
    }
}
