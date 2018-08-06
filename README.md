# EasyChart

### 前言

由于项目需要会使用到一些简单的图表，而在github上找了一圈后发现要么太庞大要么就是可配置性不强，参数都是写死的。所以后来我就想试着自己写一个，也希望能帮助到有需要的人。

而我认识的一些Android开发的朋友其实对自定义View相关的了解也比较少，所以我的想法就是做一个对不了解自定义View的人可以通过方法可以自由的配置，对初步了解自定义View的人提供基础，能更方便的实现自己需要的View。对于精通自定义View的人，那么...欢迎指出我的错误。（手动乖巧）

不过由于个人能力有限，所以也无法做的大家都满意，所以希望大家有问题多交流一起进步。

后面发现好玩的图表会持续更新。

### 使用

每个图表会提供一个基类和我自己实现的几个样式图表。如果有适合你的样式，可以拷贝基类图表和选中的样式图表到你的项目中即可使用。如果你需要实现自己的图表，那么可以拷贝基类图表到你的项目，继承他就可以绘制自己的图表。

其实在Base中已经做了很多逻辑的处理，所以绘制自己样式的时候会比较方便，简单的10多行，稍微复杂的60，70行的绘制代码内基本都能实现。

### 效果

#### 仪表盘

介绍文章：
[https://juejin.im/post/5b4d813451882519a62f5b80](https://juejin.im/post/5b4d813451882519a62f5b80)

[仪表盘图表 API](https://github.com/apinIron/EasyChart/blob/master/README-DB.md)

![](https://github.com/apinIron/EasyChart/blob/master/image/frame.gif)

#### 饼图

[饼图图表 API](https://github.com/apinIron/EasyChart/blob/master/README-PIE.md)

![](https://github.com/apinIron/EasyChart/blob/master/image/frame1.gif)
