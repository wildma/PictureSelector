[![](https://jitpack.io/v/wildma/PictureSelector.svg)](https://jitpack.io/#wildma/PictureSelector)

# PictureSelector
Android 图片选择器

### 效果图：

![效果图.jpg](https://upload-images.jianshu.io/upload_images/5382223-9d82fb9c0f22bfb2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 功能特点
- 支持通过拍照获取图片
- 支持通过相册获取图片
- 支持图片是否裁剪两种场景
- 支持仿 IOS 底部弹出选择菜单 ActionSheet 效果
- 支持 6.0 动态授予权限
- 解决图片有黑边问题
- 解决 7.0 调用相机 crash 问题
- 解决小米 miui 系统调用系统裁剪图片功能 crash 问题
- 解决华为设备裁剪框为圆形的问题

### 使用
##### Step 1. 添加 JitPack 仓库
在项目的 build.gradle 添加 JitPack 仓库
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
##### Step 2. 添加依赖
在需要使用的 module 中添加依赖
```
dependencies {
	compile 'com.github.wildma:PictureSelector:1.1.3'
}
```
或者引用本地 lib
```
compile project(':pictureselector')
```
##### Step 3. 拍照或者从相册选择图片
```
        /**
         * create() 方法参数一是上下文，在 activity 中传 activity.this，在 fragment 中传 fragment.this。参数二为请求码，用于结果回调 onActivityResult 中判断
         * selectPicture() 方法参数分别为 是否裁剪、裁剪后图片的宽(单位 px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽 200，高 200，宽高比例为 1：1。
         */
        PictureSelector
                .create(MainActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 200, 200, 1, 1);
```
##### Step 4. 获取图片地址进行显示
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                mIvImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                /*使用 Glide 加载图片，由于裁剪后的图片地址是相同的，所以不能从缓存中加载*/
                /*RequestOptions requestOptions = RequestOptions
                        .circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(this).load(picturePath).apply(requestOptions).into(mIvImage);*/
            }
        }
    }
```

### 注意
如果你没有使用依赖的方式，而是直接拷贝源码到你的项目中使用。那么需要自己适配 Android 7.0 导致的 FileUriExposedException 异常，具体方式如下：

将 PictureSelectUtils 中的 authority 与你项目中 AndroidManifest.xml 下的 authority 保持一致。
例如 AndroidManifest.xml 下的 authority 为：
```
android:authorities="myAuthority"
```
则需要修改 PictureSelectUtils 中的 authority（ [这一行](https://github.com/wildma/PictureSelector/blob/master/pictureselector/src/main/java/com/wildma/pictureselector/PictureSelectUtils.java#L74)） 为：
```
String authority = "myAuthority";
```


详细介绍请看文章：[一个非常好用的 Android 图片选择框架](https://www.jianshu.com/p/6ac6b681c413)

ps：如果对你有帮助，点下 star 就是对我最大的认可。
