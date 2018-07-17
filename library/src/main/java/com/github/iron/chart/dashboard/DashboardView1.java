package com.github.iron.chart.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/10
 */
public class DashboardView1 extends BaseDashboardView {

    //外环画笔
    private Paint mPaintOuterArc;
    //内环画笔
    private Paint mPaintInnerArc;
    //进度点画笔
    private Paint mPaintProgressPoint;
    //刻度文字画笔
    protected Paint mPaintCalibrationText;
    //刻度中间的文字画笔
    protected Paint mPaintCalibrationBetweenText;
    //大刻度画笔
    protected Paint mPaintLargeCalibration;
    //小刻度画笔
    protected Paint mPaintSmallCalibration;
    //外环区域
    private RectF mRectOuterArc;
    //内环区域
    private RectF mRectInnerArc;
    //进度条的圆点属性
    private float[] mProgressPointPosition;
    private float[] mProgressPointTan;
    private Matrix mProgressPointMatrix;
    private float mProgressPointRadius;
    //圆环画笔颜色
    private int mOuterArcColor;
    private int mProgressArcColor;
    //内外环之间的间距
    private float mArcSpacing;
    //刻度起始位置和结束位置
    private float mCalibrationStart;
    private float mCalibrationEnd;
    //刻度的文本位置
    private float mCalibrationTextStart;

    //默认圆环之间间距
    private static final int DEFAULT_ARC_SPACING = 15;
    //外环的默认属性
    private static final int DEFAULT_OUTER_ARC_WIDTH = 3;
    private static final int DEFAULT_OUTER_ARC_COLOR = Color.argb(80, 255, 255, 255);
    //内环的默认属性
    private static final int DEFAULT_INNER_ARC_WIDTH = 10;
    private static final int DEFAULT_INNER_ARC_COLOR = Color.argb(80, 255, 255, 255);
    //进度环的默认属性
    private static final int DEFAULT_PROGRESS_ARC_COLOR = Color.argb(200, 255, 255, 255);
    //进度点的默认属性
    private static final int DEFAULT_PROGRESS_POINT_COLOR = Color.WHITE;
    private static final float DEFAULT_PROGRESS_POINT_RADIUS = 3;
    // 大刻度画笔默认值
    private final static float DEFAULT_LARGE_CALIBRATION_WIDTH = 2f;
    private final static int DEFAULT_LARGE_CALIBRATION_COLOR = Color.argb(200, 255, 255, 255);
    // 小刻度画笔默认值
    private final static float DEFAULT_SMALL_CALIBRATION_WIDTH = 0.5f;
    private final static int DEFAULT_SMALL_CALIBRATION_COLOR = Color.argb(100, 255, 255, 255);
    // 默认刻度文字画笔参数
    private final static float DEFAULT_CALIBRATION_TEXT_TEXT_SIZE = 10f;
    private final static int DEFAULT_CALIBRATION_TEXT_TEXT_COLOR = Color.WHITE;


    public DashboardView1(Context context) {
        this(context,null);
    }

    public DashboardView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //默认数据
        mArcSpacing = dp2px(DEFAULT_ARC_SPACING);
        mOuterArcColor = DEFAULT_OUTER_ARC_COLOR;
        mProgressArcColor = DEFAULT_PROGRESS_ARC_COLOR;
        mProgressPointRadius = dp2px(DEFAULT_PROGRESS_POINT_RADIUS);

        //外环画笔
        mPaintOuterArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterArc.setStrokeWidth(dp2px(DEFAULT_OUTER_ARC_WIDTH));
        mPaintOuterArc.setStyle(Paint.Style.STROKE);

        //内环画笔
        mPaintInnerArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerArc.setStrokeWidth(dp2px(DEFAULT_INNER_ARC_WIDTH));
        mPaintInnerArc.setColor(DEFAULT_INNER_ARC_COLOR);
        mPaintInnerArc.setStyle(Paint.Style.STROKE);

        //进度点画笔
        mPaintProgressPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressPoint.setStyle(Paint.Style.FILL);
        mPaintProgressPoint.setColor(DEFAULT_PROGRESS_POINT_COLOR);

        //大刻度画笔
        mPaintLargeCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLargeCalibration.setStrokeWidth(dp2px(DEFAULT_LARGE_CALIBRATION_WIDTH));
        mPaintLargeCalibration.setColor(DEFAULT_LARGE_CALIBRATION_COLOR);

        //小刻度画笔
        mPaintSmallCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSmallCalibration.setStrokeWidth(dp2px(DEFAULT_SMALL_CALIBRATION_WIDTH));
        mPaintSmallCalibration.setColor(DEFAULT_SMALL_CALIBRATION_COLOR);

        //刻度文字画笔
        mPaintCalibrationText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCalibrationText.setTextAlign(Paint.Align.CENTER);
        mPaintCalibrationText.setTextSize(sp2px(DEFAULT_CALIBRATION_TEXT_TEXT_SIZE));
        mPaintCalibrationText.setColor(DEFAULT_CALIBRATION_TEXT_TEXT_COLOR);

        //刻度中间的文字画笔
        mPaintCalibrationBetweenText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCalibrationBetweenText.setTextAlign(Paint.Align.CENTER);
        mPaintCalibrationBetweenText.setTextSize(sp2px(DEFAULT_CALIBRATION_TEXT_TEXT_SIZE));
        mPaintCalibrationBetweenText.setColor(DEFAULT_CALIBRATION_TEXT_TEXT_COLOR);

        //进度点的图片
        mProgressPointPosition = new float[2];
        mProgressPointTan = new float[2];
        mProgressPointMatrix = new Matrix();
    }

    /**
     * 初始化圆环区域
     */
    @Override
    protected void initArcRect(float left, float top, float right, float bottom) {
        //外环区域
        mRectOuterArc = new RectF(left, top, right, bottom);

        initInnerRect();
    }

    /**
     * 初始化内部的区域
     */
    private void initInnerRect() {
        //内环区域
        mRectInnerArc = new RectF(mRectOuterArc.left + mArcSpacing,mRectOuterArc.top + mArcSpacing,
                mRectOuterArc.right - mArcSpacing , mRectOuterArc.bottom - mArcSpacing);

        //计算刻度位置
        mCalibrationStart = mRectOuterArc.top + mArcSpacing - mPaintInnerArc.getStrokeWidth() / 2;
        mCalibrationEnd = mCalibrationStart + mPaintInnerArc.getStrokeWidth();

        //刻度文字位置
        mCalibrationTextStart = mCalibrationEnd + dp2px(13);
    }

    /**
     * 绘制圆环
     */
    @Override
    protected void drawArc(Canvas canvas, float arcStartAngle, float arcSweepAngle) {
        //绘制外环
        mPaintOuterArc.setColor(mOuterArcColor);
        canvas.drawArc(mRectOuterArc, arcStartAngle, arcSweepAngle, false, mPaintOuterArc);

        //绘制内环
        canvas.drawArc(mRectInnerArc, arcStartAngle, arcSweepAngle, false, mPaintInnerArc);

        //绘制刻度
        drawCalibration(canvas, arcStartAngle);

        //绘制刻度中间的文字
        drawableCalibrationBetweenText(canvas, arcStartAngle);
    }

    /**
     * 绘制刻度
     */
    private void drawCalibration(Canvas canvas, float arcStartAngle) {
        if(mLargeCalibrationNumber == 0){
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270, mRadius, mRadius);
        int mod = mLargeBetweenCalibrationNumber + 1;
        //遍历数量
        for (int i = 0; i < mCalibrationTotalNumber; i++) {
            //绘制刻度线
            if(i % mod == 0){
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintLargeCalibration);
                //绘制刻度文字
                int index = i / mod;
                if(mCalibrationNumberText != null && mCalibrationNumberText.length > index){
                    canvas.drawText(String.valueOf(mCalibrationNumberText[index]), mRadius, mCalibrationTextStart, mPaintCalibrationText);
                }
            } else {
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintSmallCalibration);
            }
            //旋转
            canvas.rotate(mSmallCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();
    }

    /**
     * 绘制刻度文本
     */
    private void drawableCalibrationBetweenText(Canvas canvas, float arcStartAngle) {
        //如果没有设置大刻度的文字或者大刻度的文字数量和刻度数量不同
        if(mCalibrationBetweenText == null || mCalibrationBetweenText.length == 0) {
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270 + mLargeCalibrationBetweenAngle / 2, mRadius, mRadius);
        //需要绘制的数量
        int number = Math.min(mLargeCalibrationNumber - 1, mCalibrationBetweenText.length);
        //遍历
        for (int i = 0; i < number; i++) {
            canvas.drawText(mCalibrationBetweenText[i], mRadius, mCalibrationTextStart, mPaintCalibrationBetweenText);
            canvas.rotate(mLargeCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();
    }

    /**
     * 绘制进度圆环
     */
    @Override
    protected void drawProgressArc(Canvas canvas, float arcStartAngle, float progressSweepAngle) {
        if(progressSweepAngle == 0) {
            return;
        }
        Path path = new Path();
        //添加进度圆环的区域
        path.addArc(mRectOuterArc, arcStartAngle, progressSweepAngle);
        //计算切线值和为重
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), mProgressPointPosition, mProgressPointTan);
        //进行平移
        mProgressPointMatrix.reset();
        mProgressPointMatrix.postTranslate(mProgressPointPosition[0] - mProgressPointRadius / 2,mProgressPointPosition[1] - mProgressPointRadius / 2);
        //绘制圆环
        mPaintOuterArc.setColor(mProgressArcColor);
        canvas.drawPath(path, mPaintOuterArc);
        //绘制进度点
        canvas.drawCircle(mProgressPointPosition[0], mProgressPointPosition[1], mProgressPointRadius,  mPaintProgressPoint);
    }

    /**
     * 绘制文本
     */
    @Override
    protected void drawText(Canvas canvas, int value, String valueLevel, String currentTime) {
        //绘制数值
        float marginTop = mRadius + mTextSpacing;
        canvas.drawText(String.valueOf(value), mRadius, marginTop, mPaintValue);

        //绘制数值文字信息
        if(!TextUtils.isEmpty(valueLevel)){
            marginTop = marginTop + getPaintHeight(mPaintValueLevel, valueLevel) + mTextSpacing;
            canvas.drawText(valueLevel, mRadius, marginTop, mPaintValueLevel);
        }

        //绘制日期
        if(!TextUtils.isEmpty(currentTime)) {
            marginTop = marginTop + getPaintHeight(mPaintDate, currentTime) + mTextSpacing;
            canvas.drawText(currentTime, mRadius, marginTop, mPaintDate);
        }
    }

    /**
     * 设置圆环的距离
     */
    public void setArcSpacing(float dpSize){
        mArcSpacing = dp2px(dpSize);

        initInnerRect();

        postInvalidate();
    }

    /**
     * 设置外环颜色
     */
    public void setOuterArcPaint(float dpSize, @ColorInt int color){
        mPaintOuterArc.setStrokeWidth(dp2px(dpSize));
        mOuterArcColor = color;

        postInvalidate();
    }

    /**
     * 设置进度条的颜色
     */
    public void setProgressArcColor(@ColorInt int color){
        mProgressArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setInnerArcPaint(float dpSize, @ColorInt int color){
        mPaintInnerArc.setStrokeWidth(dp2px(dpSize));
        mPaintInnerArc.setColor(color);

        postInvalidate();
    }

    /**
     * 设置进度圆点的属性
     */
    public void setProgressPointPaint(float dpRadiusSize,@ColorInt int color){
        mProgressPointRadius = dp2px(dpRadiusSize);
        mPaintProgressPoint.setColor(color);

        postInvalidate();
    }

    /**
     * 设置大刻度画笔属性
     */
    public void setLargeCalibrationPaint(float dpSize, @ColorInt int color){
        mPaintLargeCalibration.setStrokeWidth(dp2px(dpSize));
        mPaintLargeCalibration.setColor(color);

        postInvalidate();
    }

    /**
     * 设置小刻度画笔属性
     */
    public void setSmallCalibrationPaint(float dpSize, @ColorInt int color){
        mPaintSmallCalibration.setStrokeWidth(dp2px(dpSize));
        mPaintSmallCalibration.setColor(color);

        postInvalidate();
    }

    /**
     * 设置刻度文字画笔属性
     * @param spSize 字体大小
     * @param color 字体颜色
     */
    public void setCalibrationTextPaint(float spSize, @ColorInt int color){
        mPaintCalibrationText.setTextSize(sp2px(spSize));
        mPaintCalibrationText.setColor(color);

        postInvalidate();
    }

    /**
     * 设置刻度中间的文字画笔
     */
    public void setCalibrationBetweenTextPaint(float spSize, @ColorInt int color) {
        mPaintCalibrationBetweenText.setTextSize(sp2px(spSize));
        mPaintCalibrationBetweenText.setColor(color);

        postInvalidate();
    }

}
