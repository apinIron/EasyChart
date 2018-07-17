package com.github.iron.chart.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/16
 */
public class DashboardView3 extends BaseDashboardView {

    //外环画笔
    private Paint mPaintOuterArc;
    //内环画笔
    private Paint mPaintInnerArc;
    //进度点画笔
    private Paint mPaintProgressPoint;
    //指示器画笔
    private Paint mPaintIndicator;
    //外环区域
    private RectF mRectOuterArc;
    //内环区域
    private RectF mRectInnerArc;
    //圆环画笔颜色
    private int mOuterArcColor;
    private int mProgressOuterArcColor;
    //内环画笔颜色
    private int mInnerArcColor;
    private int mProgressInnerArcColor;
    //内外环之间的间距
    private float mArcSpacing;
    //进度条的圆点属性
    private float[] mProgressPointPosition;
    private float[] mProgressPointTan;
    private Matrix mProgressPointMatrix;
    private float mProgressPointRadius;
    //指标器的Path
    private Path mIndicatorPath;
    //指示器的起始位置
    private float mIndicatorStart;

    //默认圆环之间间距
    private static final float DEFAULT_ARC_SPACING = 10;
    //外环的默认属性
    private static final float DEFAULT_OUTER_ARC_WIDTH = 1.5f;
    private static final int DEFAULT_OUTER_ARC_COLOR = Color.argb(80, 255, 255, 255);
    //外环进度的默认属性
    private static final int DEFAULT_PROGRESS_OUTER_ARC_COLOR = Color.argb(200, 255, 255, 255);
    //进度点的默认属性
    private static final float DEFAULT_PROGRESS_POINT_RADIUS = 3;
    private static final int DEFAULT_PROGRESS_POINT_COLOR = Color.WHITE;
    //内环默认属性
    private static final float DEFAULT_INNER_ARC_WIDTH = 1.5f;
    private static final int DEFAULT_INNER_ARC_COLOR = Color.argb(50, 255, 255, 255);
    //内环进度的默认属性
    private static final int DEFAULT_PROGRESS_INNER_ARC_COLOR = Color.argb(170, 255, 255, 255);
    //指示器默认属性
    private static final int DEFAULT_INDICATOR_COLOR = Color.argb(200, 255, 255, 255);


    public DashboardView3(Context context) {
        this(context, null);
    }

    public DashboardView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        //默认数据
        mArcSpacing = dp2px(DEFAULT_ARC_SPACING);
        mOuterArcColor = DEFAULT_OUTER_ARC_COLOR;
        mProgressOuterArcColor = DEFAULT_PROGRESS_OUTER_ARC_COLOR;
        mProgressPointRadius = dp2px(DEFAULT_PROGRESS_POINT_RADIUS);
        mInnerArcColor = DEFAULT_INNER_ARC_COLOR;
        mProgressInnerArcColor = DEFAULT_PROGRESS_INNER_ARC_COLOR;

        //初始化画笔
        //外环画笔
        mPaintOuterArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterArc.setStrokeWidth(dp2px(DEFAULT_OUTER_ARC_WIDTH));
        mPaintOuterArc.setStyle(Paint.Style.STROKE);

        //内环画笔
        mPaintInnerArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerArc.setStrokeWidth(dp2px(DEFAULT_INNER_ARC_WIDTH));
        mPaintInnerArc.setStyle(Paint.Style.STROKE);
        mPaintInnerArc.setStrokeCap(Paint.Cap.ROUND);
        PathEffect mPathEffect = new DashPathEffect(new float[] { 10, 10 }, 0);
        mPaintInnerArc.setPathEffect(mPathEffect);

        //进度点画笔
        mPaintProgressPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressPoint.setStyle(Paint.Style.FILL);
        mPaintProgressPoint.setColor(DEFAULT_PROGRESS_POINT_COLOR);

        //指示器画笔
        mPaintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintIndicator.setStrokeCap(Paint.Cap.SQUARE);
        mPaintIndicator.setColor(DEFAULT_INDICATOR_COLOR);
        mPaintIndicator.setStrokeWidth(dp2px(1));

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
        //内环位置
        mRectInnerArc = new RectF(mRectOuterArc.left + mArcSpacing,mRectOuterArc.top + mArcSpacing,
                mRectOuterArc.right - mArcSpacing , mRectOuterArc.bottom - mArcSpacing);

        //指标器的路径
        mIndicatorStart = mRectInnerArc.top + mArcSpacing / 2;
        mIndicatorPath = new Path();
        mIndicatorPath.moveTo(mRadius, mIndicatorStart);
        mIndicatorPath.rLineTo(-dp2px(2), dp2px(5));
        mIndicatorPath.rLineTo(dp2px(4), 0);
        mIndicatorPath.close();
    }

    /**
     * 绘制圆环
     */
    @Override
    protected void drawArc(Canvas canvas, float arcStartAngle, float arcSweepAngle) {
        //绘制圆环
        mPaintOuterArc.setColor(mOuterArcColor);
        canvas.drawArc(mRectOuterArc, arcStartAngle, arcSweepAngle, false, mPaintOuterArc);

        //绘制内环
        mPaintInnerArc.setColor(mInnerArcColor);
        canvas.drawArc(mRectInnerArc, arcStartAngle, arcSweepAngle, false, mPaintInnerArc);
    }

    /**
     * 绘制进度圆环
     */
    @Override
    protected void drawProgressArc(Canvas canvas, float arcStartAngle, float progressSweepAngle) {
        //绘制进度点
        if(progressSweepAngle == 0) {
            return;
        }
        Path path = new Path();
        //添加进度圆环的区域
        path.addArc(mRectOuterArc, arcStartAngle, progressSweepAngle);
        //计算切线值和为重
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), mProgressPointPosition, mProgressPointTan);
        //根据切点进行位置的平移
        mProgressPointMatrix.reset();
        mProgressPointMatrix.postTranslate(mProgressPointPosition[0] - mProgressPointRadius,mProgressPointPosition[1] - mProgressPointRadius);
        //绘制圆环
        mPaintOuterArc.setColor(mProgressOuterArcColor);
        canvas.drawPath(path, mPaintOuterArc);
        //绘制进度点
        if(mProgressPointPosition[0] != 0 && mProgressPointPosition[1] != 0) {
            canvas.drawCircle(mProgressPointPosition[0], mProgressPointPosition[1], mProgressPointRadius, mPaintProgressPoint);
        }

        //绘制内环
        mPaintInnerArc.setColor(mProgressInnerArcColor);
        canvas.drawArc(mRectInnerArc, arcStartAngle, progressSweepAngle, false, mPaintInnerArc);

        //绘制指针
        canvas.save();
        canvas.rotate(arcStartAngle + progressSweepAngle - 270, mRadius, mRadius);
        mPaintIndicator.setStyle(Paint.Style.FILL);
        canvas.drawPath(mIndicatorPath, mPaintIndicator);
        mPaintIndicator.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mRadius, mIndicatorStart + dp2px(6) + 1, dp2px(2), mPaintIndicator);
        canvas.restore();
    }

    /**
     * 绘制文字
     */
    @Override
    protected void drawText(Canvas canvas, int value, String valueLevel, String currentTime) {
        //绘制数值
        float marginTop = mRadius + mTextSpacing;
        canvas.drawText(String.valueOf(value), mRadius, marginTop, mPaintValue);

        //绘制数值文字信息
        if(!TextUtils.isEmpty(valueLevel)){
            float margin = mRadius - mTextSpacing - getPaintHeight(mPaintValue, "9");
            canvas.drawText(valueLevel, mRadius, margin, mPaintValueLevel);
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
    public void setProgressOuterArcColor(@ColorInt int color){
        mProgressOuterArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setInnerArcPaint(float dpSize, @ColorInt int color){
        mPaintInnerArc.setStrokeWidth(dp2px(dpSize));
        mInnerArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setProgressInnerArcPaint(@ColorInt int color){
        mProgressInnerArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环实线和虚线状态
     */
    public void setInnerArcPathEffect(float[] intervals){
        PathEffect mPathEffect = new DashPathEffect(intervals, 0);
        mPaintInnerArc.setPathEffect(mPathEffect);

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
     * 设置指示器属性
     */
    public void setIndicatorPaint(@ColorInt int color){
        mPaintIndicator.setColor(color);

        postInvalidate();
    }
}
