# EasyChart

### 前言

由于项目需要使用到仪表盘图表，所以就本着一贯的操作流程就来github上面找，结果发现大多的仪表盘图表扩展性不强，所以就自己动手写了一个，希望能帮助到有需要的人。（本人能力有限，哪里不足的话欢迎指出）

### 使用

本项目目前提供了三种样式可以供使用，但是考虑到每个人在开发过程中需求都会不一样所以没有上传仓库，如果看上了哪个样式可以拷贝相应的样式View和 `BaseDashboardView` 到你的项目即可使用。也可以只拷贝 `BaseDashboardView` 然后继承他实现你自己想要的效果。

(其实实现自己的一个 DashboardView不需要多少代码，只要你有点自定义View基础，除去一些初始化代码，100行代码以内就能写出一个绚丽的仪表盘)

### 效果

![](https://github.com/apinIron/EasyChart/blob/master/image/frame.gif)

### 公共方法介绍

```

//设置当前数值 value:数值 isAnim:是否开启东湖 reset:是否从头开始进行动画
setValue(int value, boolean isAnim, boolean reset)

//设置动画时长 (默认为3秒)
setProgressAnimTime(long time)

//设置圆弧角度 
//arcStartAngle:起始角度 (默认值:165)
//arcSweepAngle:圆弧度数 (默认值:210)
setArcAngle(float arcStartAngle,float arcSweepAngle){

//设置刻度属性
//calibrationNumberText 每个大刻度对应的数值 (默认值:int[]{350, 550, 600, 650, 700, 950})
//calibrationBetweenText 每个大刻度中间的文字 (默认值:String[]{"较差", "中等", "良好", "优秀", "极好"})
//largeCalibrationBetweenNumber 两个大刻度中间有多少个小刻度 (默认值:3)
setCalibration(int[] calibrationNumberText, String[] calibrationBetweenText,int largeCalibrationBetweenNumber)

//设置日期格式化的格式 (默认值:yyyy-MM-dd)
setDatePattern(String pattern)

//设置时间的显示格式 格式(如: 评估时间：{date}) {date}为占位符
setDateStrPattern(String pattern)

//设置数值等级的模板 格式(如: 信用{level}) {level}为占位符
setValueLevelPattern(String pattern)

//设置数值的画笔属性 (默认值 60sp white)
setValuePaint(float spSize, @ColorInt int color)

//设置数值等级的画笔属性 (默认值 25sp white)
setValueLevelPaint(float spSize, @ColorInt int color)

//设置日期对应的画笔属性 (默认值 10sp white)
setDatePaint(float spSize, @ColorInt int color)

//设置中间文字中间的间距 (默认值 7dp)
setTextSpacing(int spacingDp)

```

#### Style 1

![](https://github.com/apinIron/EasyChart/blob/master/image/1.png)

#### Style 2

![](https://github.com/apinIron/EasyChart/blob/master/image/2.png)

#### Style 3

![](https://github.com/apinIron/EasyChart/blob/master/image/3.png)

