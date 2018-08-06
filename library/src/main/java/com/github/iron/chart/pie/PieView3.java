package com.github.iron.chart.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/27
 */
public class PieView3 extends BasePieView {

    //圆环画笔
    private Paint mPaintArc;
    //文字画笔
    private Paint mPaintText;
    //区域
    private RectF mRectPie;
    //饼图半径
    private float mPieRadius;
    //饼图和线直接的距离
    private float mLinePadding;
    private float mLineLength1;
    private float mLineLength2;
    //数值文字之间的间距
    private float mValueTextSpacing;

    //数值线和拼图之间的间距
    private static final int DEFAULT_LINE_PADDING = 5;
    //线的宽度
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    //数值画笔的大小
    private static final int DEFAULT_VALUE_TEXT_SIZE = 8;


    public PieView3(Context context) {
        this(context,null);
    }

    public PieView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //默认数据
        mLinePadding = dp2px(DEFAULT_LINE_PADDING);
        mValueTextSpacing = dp2px(2);

        //初始化画笔
        //饼图画笔
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setStyle(Paint.Style.FILL);
        mPaintArc.setStrokeWidth(dp2px(DEFAULT_LINE_WIDTH));

        //文字画笔
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextSize(sp2px(DEFAULT_VALUE_TEXT_SIZE));
    }

    /**
     * 初始化拼图区域
     */
    @Override
    protected void initPieRect(float radius) {
        //初始化区域
        mPieRadius = radius / 2;
        mLineLength1 = mPieRadius / 3;
        mLineLength2 = mLineLength1 / 3;

        //圆环区域
        mRectPie = new RectF(-mPieRadius, -mPieRadius, mPieRadius, mPieRadius);
    }

    /**
     * 绘制圆弧
     */
    @Override
    protected void drawPieArc(Canvas canvas, String value, String percent, String name, int color, float arcStartAngle, float sweepAngle) {
        // 设置颜色
        mPaintArc.setColor(color);
        mPaintText.setColor(color);
        // 绘制饼图块
        canvas.drawArc(mRectPie, arcStartAngle, sweepAngle, true, mPaintArc);

        //绘制数值线
        // 旋转
        canvas.save();
        float angle = arcStartAngle + sweepAngle / 2 - 90;
        canvas.rotate(angle);
        //绘制线1
        canvas.drawLine(0, mPieRadius + mLinePadding, 0, mPieRadius + mLinePadding + mLineLength1, mPaintArc);
        canvas.translate(0, mPieRadius + mLinePadding + mLineLength1);
        canvas.rotate(-angle);
        //结束没在
        float endX;
        //如果是在左边
        if(angle > 0 && angle < 180) {
            endX = -mLineLength2;
            mPaintText.setTextAlign(Paint.Align.RIGHT);
        }else{
            endX = mLineLength2;
            mPaintText.setTextAlign(Paint.Align.LEFT);
        }

        //绘制线2
        canvas.drawLine(0, 0, endX, 0, mPaintArc);
        //绘制文字
        canvas.drawText(name, endX, -mValueTextSpacing, mPaintText);
        canvas.drawText(percent, endX, mValueTextSpacing + getPaintHeight(mPaintText, percent), mPaintText);

        canvas.restore();
    }

    /**
     * 绘制标题
     */
    @Override
    protected void drawTitle(Canvas canvas, String title, String totalValue) {

    }

    /**
     * 设置圆和线之间的间距
     */
    public void setLinePadding(float dpSize){
        mLinePadding = dp2px(dpSize);
        postInvalidate();
    }

    /**
     * 设置线的宽度
     */
    public void setLineWidth(float dpSize){
        mPaintArc.setStrokeWidth(dp2px(dpSize));
        postInvalidate();
    }

    /**
     * 设置数值数据画笔
     */
    public void setValueTextPaint(float spSize){
        mPaintText.setTextSize(sp2px(spSize));
        postInvalidate();
    }
}
