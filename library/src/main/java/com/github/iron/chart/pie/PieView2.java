package com.github.iron.chart.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/20
 */
public class PieView2 extends BasePieView {

    //圆环画笔
    private Paint mPaintArc;
    //透明画笔
    private Paint mPaintAlpha;
    //文字画笔
    private Paint mPaintText;
    //区域
    private RectF mRectArc;
    //透明环区域
    private RectF mRectAlphaArc;
    //圆环路径
    private Path mPathArc;
    //透明圆环路径
    private Path mPathAlphaArc;
    //文字位置
    private float[] mTextPoint;

    //默认饼图圆环宽度
    private static final int DEFAULT_PIE_ARC_WIDTH = 50;
    //默认间隔度数
    private static final float DEFAULT_INTERVAL_ANGLE = 1f;
    //默认文字画笔
    private static final float DEFAULT_TEXT_SIZE = 11;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;


    public PieView2(Context context) {
        this(context, null);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //默认数据
        super.setArcIntervalAngle(DEFAULT_INTERVAL_ANGLE);

        //初始化路径
        mPathArc = new Path();
        mPathAlphaArc = new Path();
        mTextPoint = new float[2];

        //初始化画笔
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeWidth(dp2px(DEFAULT_PIE_ARC_WIDTH));

        mPaintAlpha = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAlpha.setStyle(Paint.Style.STROKE);
        mPaintAlpha.setColor(Color.WHITE);
        mPaintAlpha.setAlpha(100);
        mPaintAlpha.setStrokeWidth(mPaintArc.getStrokeWidth() / 6);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(sp2px(DEFAULT_TEXT_SIZE));
        mPaintText.setColor(DEFAULT_TEXT_COLOR);
    }

    /**
     * 初始化饼图区域
     */
    @Override
    protected void initPieRect(float radius) {
        //圆环区域
        float width = mPaintArc.getStrokeWidth() / 2;
        mRectArc = new RectF(-radius + width, -radius + width, radius - width, radius - width);

        //透明环区域
        float padding = mPaintArc.getStrokeWidth() - mPaintAlpha.getStrokeWidth() / 3;
        mRectAlphaArc = new RectF(-radius + padding,-radius + padding ,radius - padding,radius - padding);
    }

    /**
     * 绘制拼图圆弧
     */
    @Override
    protected void drawPieArc(Canvas canvas, String value, String percent, String name, int color, float arcStartAngle, float sweepAngle) {
        //绘制圆弧
        mPaintArc.setColor(color);

        //绘制圆环
        mPathArc.reset();
        mPathArc.addArc(mRectArc, arcStartAngle, sweepAngle);
        canvas.drawPath(mPathArc, mPaintArc);

        //绘制透明层
        mPathAlphaArc.reset();
        mPathAlphaArc.addArc(mRectAlphaArc, arcStartAngle, sweepAngle);
        canvas.drawPath(mPathAlphaArc, mPaintAlpha);

        //绘制数值
        if(new PathMeasure(mPathArc, false).getLength() >
                Math.max(getPaintWidth(mPaintText, percent), getPaintWidth(mPaintText, name))) {
            PathMeasure pathMeasure = new PathMeasure(mPathArc, false);
            pathMeasure.getPosTan(pathMeasure.getLength() / 2, mTextPoint, null);
            canvas.drawText(name, mTextPoint[0], mTextPoint[1], mPaintText);
            canvas.drawText(percent, mTextPoint[0], mTextPoint[1] + dp2px(2) + getPaintHeight(mPaintText, percent), mPaintText);
        }
    }

    /**
     * 绘制标题
     */
    @Override
    protected void drawTitle(Canvas canvas, String title, String totalValue) {
        //绘制标题
        canvas.drawText(title, 0, 0, mPaintTitle);
    }

    /**
     * 设置圆弧之间的间隔角度
     */
    public void setArcIntervalAngle(float intervalAngle){
        super.setArcIntervalAngle(intervalAngle);

        postInvalidate();
    }

    /**
     * 设置饼图的圆弧宽度
     */
    public void setPieArcWidth(int dpSize){
        mPaintArc.setStrokeWidth(dp2px(dpSize));
        mPaintAlpha.setStrokeWidth(mPaintArc.getStrokeWidth() / 6);
        //重新初始化圆饼区域
        initPieRect(mRadius);

        postInvalidate();
    }

    /**
     * 设置文字画笔
     */
    public void setValueTextPaint(float spSize,@ColorInt int color){
        mPaintText.setTextSize(sp2px(spSize));
        mPaintText.setColor(color);

        postInvalidate();
    }
}
