package com.github.iron.chart.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author iron
 *         created at 2018/7/13
 */
public class DashboardView2 extends BaseDashboardView {

    //圆环画笔
    private Paint mPaintArc;
    //圆环进度画笔
    private Paint mPaintProgress;
    //圆环的刻度路径
    private Path mArcCalibrationPath;
    //圆环刻度半径
    private float mArcCalibrationRadius;
    //距离顶部
    private float mPaddingTop;

    //圆形贝塞尔曲线常量
    private static final float C = 0.551915024494f;
    //默认的圆环刻度半径
    private static final float DEFAULT_ARC_CALIBRATION_RADIUS = 2.5f;
    //默认圆环颜色
    private static final int DEFAULT_ARC_COLOR = Color.argb(120, 255, 255, 255);
    //默认圆环进度颜色
    private static final int DEFAULT_PROGRESS_COLOR = Color.argb(200, 255, 255, 255);


    public DashboardView2(Context context) {
        this(context, null);
    }

    public DashboardView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        //初始化数据
        mArcCalibrationRadius = dp2px(DEFAULT_ARC_CALIBRATION_RADIUS);
        //初始化画笔
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setColor(DEFAULT_ARC_COLOR);
        mPaintArc.setStyle(Paint.Style.FILL);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setColor(DEFAULT_PROGRESS_COLOR);
        mPaintProgress.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化圆环区域
     */
    @Override
    protected void initArcRect(float left, float top, float right, float bottom) {
        mPaddingTop = top;
        //初始化贝塞尔曲线
        initPath();
    }

    /**
     * 初始化圆环刻度路径
     */
    private void initPath() {
        //圆环区域
        HorizontalLine mTopLine = new HorizontalLine(mRadius, mPaddingTop);
        HorizontalLine mBottomLine = new HorizontalLine(mRadius, mPaddingTop + 4 * mArcCalibrationRadius);
        VerticalLine mLeftLine = new VerticalLine(mRadius - mArcCalibrationRadius,mPaddingTop + mArcCalibrationRadius);
        VerticalLine mRightLine = new VerticalLine(mRadius + mArcCalibrationRadius, mPaddingTop + mArcCalibrationRadius);
        //设置刻度路径
        mArcCalibrationPath = new Path();
        mArcCalibrationPath.moveTo(mTopLine.middle.x, mTopLine.middle.y);
        mArcCalibrationPath.cubicTo(mTopLine.right.x, mTopLine.right.y, mRightLine.top.x, mRightLine.top.y,
                mRightLine.middle.x, mRightLine.middle.y);
        mArcCalibrationPath.cubicTo(mRightLine.bottom.x, mRightLine.bottom.y, mBottomLine.right.x, mBottomLine.right.y,
                mBottomLine.middle.x, mBottomLine.middle.y);
        mArcCalibrationPath.cubicTo(mBottomLine.left.x, mBottomLine.left.y, mLeftLine.bottom.x, mLeftLine.bottom.y,
                mLeftLine.middle.x, mLeftLine.middle.y);
        mArcCalibrationPath.cubicTo(mLeftLine.top.x, mLeftLine.top.y, mTopLine.left.x, mTopLine.left.y,
                mTopLine.middle.x, mTopLine.middle.y);
    }

    /**
     * 绘制圆环
     */
    @Override
    protected void drawArc(Canvas canvas, float arcStartAngle, float arcSweepAngle) {
        //如果没有刻度数量
        if(mCalibrationTotalNumber == 0){
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270, mRadius, mRadius);
        //遍历数量
        for (int i = 0; i < mCalibrationTotalNumber; i++) {
            //绘制刻度线
            canvas.drawPath(mArcCalibrationPath, mPaintArc);
            //旋转
            canvas.rotate(mSmallCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();

    }

    /**
     * 绘制进度圆环
     */
    @Override
    protected void drawProgressArc(Canvas canvas, float arcStartAngle, float progressSweepAngle) {
        if(progressSweepAngle <= 0){
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270, mRadius, mRadius);
        int count = (int) (progressSweepAngle / mSmallCalibrationBetweenAngle) + 1;
        //遍历数量
        for (int i = 0; i < count; i++) {
            //绘制刻度线
            canvas.drawPath(mArcCalibrationPath, mPaintProgress);
            //旋转
            canvas.rotate(mSmallCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();
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
     * 设置圆环颜色
     */
    public void setArcColor(@ColorInt int color){
        mPaintArc.setColor(color);

        postInvalidate();
    }

    /**
     * 设置进度颜色
     */
    public void setProgressColor(@ColorInt int color){
        mPaintProgress.setColor(color);

        postInvalidate();
    }

    /**
     *  圆环上面的刻度大小
     */
    public void setArcCalibrationSize(int dpSize){
        mArcCalibrationRadius = dp2px(dpSize);

        initPath();

        postInvalidate();
    }

    /**
     * 圆形贝塞尔曲线的水平方向线
     */
    class HorizontalLine {

        PointF left = new PointF(); //P7 P11
        PointF middle = new PointF(); //P0 P6
        PointF right = new PointF(); //P1 P5

        HorizontalLine(float x, float y) {
            middle.x = x;
            middle.y = y;
            left.x = x - mArcCalibrationRadius * C;
            left.y = y;
            right.x = x + mArcCalibrationRadius * C;
            right.y = y;
        }

        public void setY(float y) {
            left.y = y;
            middle.y = y;
            right.y = y;
        }
    }

    /**
     * 圆形贝塞尔曲线的垂直方向线
     */
    class VerticalLine {

        PointF top = new PointF(); //P2 P10
        PointF middle = new PointF(); //P3 P9
        PointF bottom = new PointF(); //P4 P8

        VerticalLine(float x, float y) {
            middle.x = x;
            middle.y = y;
            top.x = x;
            top.y = y - mArcCalibrationRadius * C;
            bottom.x = x;
            bottom.y = y + mArcCalibrationRadius * C;
        }

        public void setX(float x) {
            top.x = x;
            middle.x = x;
            bottom.x = x;
        }
    }
}
