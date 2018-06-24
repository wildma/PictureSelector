package com.wildma.wildmaselectpicture;

import java.io.File;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/10
 * Desc	        ${常量}
 */
public class Constant {
    public static final String APP_NAME   = "wildmaSelectPicture";//app名称
    public static final String BASE_DIR   = "wildmaSelectPicture/";
    public static final String DIR_ROOT   = FileUtils.getRootPath() + File.separator + Constant.BASE_DIR;//文件夹根目录
    public static final String DIR_EXPORT = FileUtils.getRootPath() + File.separator + BASE_DIR + "Export/";
    public static final String AUTHORITY  = AppUtils.getAppPackageName(MyApplication.getContext()) + ".fileProvider";

    public static final int CANCEL = 0;//取消
    public static final int CAMERA = 1;//拍照
    public static final int ALBUM  = 2;//相册
}

