[![](https://jitpack.io/v/wildma/PictureSelector.svg)](https://jitpack.io/#wildma/PictureSelector)

# PictureSelector
Android 图片选择器

### 效果图：

![效果图.jpg](https://upload-images.jianshu.io/upload_images/5382223-9d82fb9c0f22bfb2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 功能特点
- 支持通过拍照获取图片
- 支持通过相册获取图片
- 支持图片是否裁剪两种场景
- 支持仿IOS底部弹出选择菜单ActionSheet效果
- 支持6.0动态授予权限
- 解决图片有黑边问题
- 解决7.0调用相机crash问题
- 解决小米miui系统调用系统裁剪图片功能crash问题

### 使用
##### Step 1. 添加JitPack仓库
在项目的build.gradle添加JitPack仓库
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
##### Step 2. 添加依赖
在需要使用的module中添加依赖
```
dependencies {
	compile 'com.github.wildma:PictureSelector:1.1.0'
}
```
或者引用本地lib
```
compile project(':pictureselector')
```
##### Step 3. 拍照或者从相册选择图片
```
        /**
         * create()方法参数一是上下文，在activity中传activity.this，在fragment中传fragment.this。参数二为请求码，用于结果回调onActivityResult中判断
         * selectPicture()方法参数分别为 是否裁剪、裁剪后图片的宽(单位px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽200，高200，宽高比例为1：1。
         */
        PictureSelector
                .create(MainActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 200, 200, 1, 1);
```
##### Step 4. 获取裁剪后的图片地址
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
            }
        }
    }
```


详细介绍请看文章：[一个非常好用的Android图片选择框架](https://www.jianshu.com/p/6ac6b681c413)

ps:如果对你有帮助，点下star就是对我最大的认可。
