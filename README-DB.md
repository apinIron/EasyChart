## 仪表盘图表API

### 效果

![](https://github.com/apinIron/EasyChart/blob/master/image/frame.gif)

### 公共方法介绍

```

//设置当前数值 value:数值 isAnim:是否开启东湖 reset:是否从头开始进行动画
setValue(int value, boolean isAnim, boolean reset)

//设置动画时长 (默认为2.5秒)
setProgressAnimTime(long time)

//设置圆弧角度 
//arcStartAngle:起始角度 (默认值: 165)
//arcSweepAngle:圆弧度数 (默认值: 210)
setArcAngle(float arcStartAngle,float arcSweepAngle){

//设置刻度属性
//calibrationNumberText 每个大刻度对应的数值 (默认值: int[]{350, 550, 600, 650, 700, 950})
//calibrationBetweenText 每个大刻度中间的文字 (默认值: String[]{"较差", "中等", "良好", "优秀", "极好"})
//largeCalibrationBetweenNumber 两个大刻度中间有多少个小刻度 (默认值: 3)
setCalibration(int[] calibrationNumberText, String[] calibrationBetweenText,int largeCalibrationBetweenNumber)

//设置日期格式化的格式 (默认值: yyyy-MM-dd)
setDatePattern(String pattern)

//设置时间的显示格式 格式(如: 评估时间：{date}) {date}为占位符
setDateStrPattern(String pattern)

//设置数值等级的模板 格式(如: 信用{level}) {level}为占位符
setValueLevelPattern(String pattern)

//设置数值的画笔属性 (默认值: 60sp white)
setValuePaint(float spSize, @ColorInt int color)

//设置数值等级的画笔属性 (默认值: 25sp white)
setValueLevelPaint(float spSize, @ColorInt int color)

//设置日期对应的画笔属性 (默认值: 10sp white)
setDatePaint(float spSize, @ColorInt int color)

//设置中间文字中间的间距 (默认值: 7dp)
setTextSpacing(int spacingDp)

```

#### Style 1

![](https://github.com/apinIron/EasyChart/blob/master/image/1.png)

```

//设置圆环之间的距离 (默认值: 15dp)
setArcSpacing(float dpSize)

//设置外环的画笔属性 (默认值: 3dp Color.argb(80, 255, 255, 255))
setOuterArcPaint(float dpSize, @ColorInt int color)

//设置内环的画笔属性(默认值: 10dp Color.argb(80, 255, 255, 255))
setInnerArcPaint(float dpSize, @ColorInt int color)

//设置进度条的颜色 (默认值: Color.argb(200, 255, 255, 255))
setProgressArcColor(@ColorInt int color)

//设置进度条的点的画笔属性 (默认值: 3dp white)
setProgressPointPaint(float dpRadiusSize,@ColorInt int color)

//设置大刻度的画笔属性 (默认值: 2dp Color.argb(200, 255, 255, 255))
setLargeCalibrationPaint(float dpSize, @ColorInt int color)

//设置小刻度的画笔属性 (默认值: 0.5dp Color.argb(100, 255, 255, 255))
setSmallCalibrationPaint(float dpSize, @ColorInt int color)

//设置刻度文字的画笔属性 (默认值: 10sp white)
setCalibrationTextPaint(float spSize, @ColorInt int color)

//设置大刻度中间的数值等级的画笔属性 (默认值: 10sp white)
setCalibrationBetweenTextPaint(float spSize, @ColorInt int color)

```

#### Style 2

![](https://github.com/apinIron/EasyChart/blob/master/image/2.png)

```

//设置圆环颜色 (默认值: Color.argb(120, 255, 255, 255))
setArcColor(@ColorInt int color)

//设置进度圆环的颜色 (默认值: Color.argb(200, 255, 255, 255))
setProgressColor(@ColorInt int color)

//设置圆环的刻度大小 (默认值: 2.5dp)
setArcCalibrationSize(int dpSize)

```

#### Style 3

![](https://github.com/apinIron/EasyChart/blob/master/image/3.png)

```

//设置圆环之间的距离 (默认值: 10dp)
setArcSpacing(float dpSize)

//设置外环的画笔属性 (默认值: 1.5dp Color.argb(80, 255, 255, 255))
setOuterArcPaint(float dpSize, @ColorInt int color)

//设置外环的进度颜色 (默认值: Color.argb(200, 255, 255, 255))
setProgressOuterArcColor(@ColorInt int color)

//设置内环的画笔属性 (默认值: 1.5dp Color.argb(50, 255, 255, 255))
setInnerArcPaint(float dpSize, @ColorInt int color)

//设置内环的进度颜色 (默认值: Color.argb(170, 255, 255, 255))
setProgressInnerArcPaint(@ColorInt int color)

//设置内环实线和虚线状态 (默认值: float[] { 10, 10 })
setInnerArcPathEffect(float[] intervals)

//设置进度点的画笔属性 (默认值: 3dp white)
setProgressPointPaint(float dpRadiusSize,@ColorInt int color)

//设置指示器颜色 (默认值: Color.argb(200, 255, 255, 255))
setIndicatorPaint(@ColorInt int color)

```
