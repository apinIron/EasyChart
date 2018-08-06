## 饼图图表API

### 效果

![](https://github.com/apinIron/EasyChart/blob/master/image/frame1.gif)

### 公共方法介绍

```

//设置数据 values:数据 name:名称 colors:颜色 anim:是否开启动画 （不设置颜色的话，颜色会随机）
setData(float[] values, boolean anim)
setData(float[] values, int[] colors, boolean anim)
setData(float[] values, String[] name, boolean anim)
setData(float[] values, String[] name, int[] color, boolean anim)

//设置标题
setTitle(String title)

//设置数据的显示位数 (默认值: 0)
setDisplayDigits(int digits)

//设置文字的间隔 (默认值: 7dp)
setTextSpacing(int spacingDp)

//设置动画时长 (默认值: 1500毫秒)
setProgressAnimTime(long time)

//设置动画样式 (默认值: 平移样式)
setAnimStyle(int style)

//设置标题画笔属性 (默认值: 12sp Color.GRAY)
setTitlePaint(float spSize, @ColorInt int color)

//设置总值画笔属性 (默认值: 18sp Color.BLACK)
setTotalValuePaint(float spSize, @ColorInt int color) 

//绑定数据界面 (后面会有介绍)
attachPieDataView(IPieDataView view)

```

#### Style 1

![](https://github.com/apinIron/EasyChart/blob/master/image/4.png)

```
//设置饼图的圆弧宽度 (默认值: 15dp)
setPieArcWidth(int dpSize)

```

#### Style 2

![](https://github.com/apinIron/EasyChart/blob/master/image/5.png)

```

//设置圆弧之间的间隔度数 (默认值: 1)
setArcIntervalAngle(float intervalAngle)

//设置饼图的圆弧宽度 (默认值: 50dp)
setPieArcWidth(int dpSize)

//设置每个圆弧的数值画笔属性 (默认值: 11sp Color.WHITE)
setValueTextPaint(float spSize,@ColorInt int color)

```

#### Style 3

![](https://github.com/apinIron/EasyChart/blob/master/image/6.png)

```

//设置线和饼图之间的间距 (默认值: 5dp)
setLinePadding(float dpSize)

//设置线的宽度 (默认值: 0.5dp)
setLineWidth(float dpSize)

//设置数值的属性 (默认值: 8sp)
setValueTextPaint(float spSize)

```
