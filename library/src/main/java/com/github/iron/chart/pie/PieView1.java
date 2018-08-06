package com.github.iron.chart.pie;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/18
 */
public class PieView1 extends BasePieView {

    //圆环画笔
    private Paint mPaintArc;
    //区域
    private RectF mRectArc;
    //圆弧路径
    private Path mPathArc;

    //默认饼图的圆弧宽度
    private static final int DEFAULT_PIE_ARC_WIDTH = 15;


    public PieView1(Context context) {
        this(context,null);
    }

    public PieView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //初始化路径
        mPathArc = new Path();
        //初始化画笔
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeWidth(dp2px(DEFAULT_PIE_ARC_WIDTH));
    }

    /**
     * 初始化饼状图区域
     */
    @Override
    protected void initPieRect(float radius) {
        //初始化区域
        float width = mPaintArc.getStrokeWidth() / 2;
        mRectArc = new RectF(-radius + width, -radius + width, radius - width, radius - width);
    }

    /**
     * 绘制圆弧
     */
    @Override
    protected void drawPieArc(Canvas canvas,String value, String percent, String name, int color, float arcStartAngle, float sweepAngle) {
        //绘制圆弧
        mPaintArc.setColor(color);
        //添加圆弧路径
        mPathArc.reset();
        mPathArc.addArc(mRectArc, arcStartAngle, sweepAngle);
        //绘制
        canvas.drawPath(mPathArc, mPaintArc);
    }

    /**
     * 绘制文字
     */
    @Override
    protected void drawTitle(Canvas canvas, String title, String totalValue) {
        //绘制数值
        canvas.drawText(totalValue, 0, 0, mPaintTotalValue);

        //绘制标题
        canvas.drawText(title, 0, mTextSpacing + getPaintHeight(mPaintTitle, title), mPaintTitle);
    }

    /**
     * 设置饼图的圆弧宽度
     */
    public void setPieArcWidth(int dpSize){
        mPaintArc.setStrokeWidth(dp2px(dpSize));
        //重新初始化圆饼区域
        initPieRect(mRadius);

        postInvalidate();
    }

}
