package com.wildma.pictureselector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.core.content.FileProvider;


/**
 * Author       wildma
 * Github       https://github.com/wildma
 * CreateDate   2018/6/10
 * Desc	        ${选择图片工具类}
 * 使用方法：
 * 1. 调用getByCamera()、getByAlbum()可通过拍照或相册获取图片
 * 2. 在onActivityResult中调用本工具类的onActivityResult方法处理通过相册或拍照获取的图片
 */
public class PictureSelectUtils {

    public static final int  GET_BY_ALBUM  = 0x11;//相册标记
    public static final int  GET_BY_CAMERA = 0x12;//拍照标记
    public static final int  CROP          = 0x13;//裁剪标记
    private static      Uri  takePictureUri;//拍照图片uri
    private static      Uri  cropPictureTempUri;//裁剪图片uri
    private static      File takePictureFile;//拍照图片File

    /**
     * 通过相册获取图片
     */
    public static void getByAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, GET_BY_ALBUM);
    }

    /**
     * 通过拍照获取图片
     */
    public static void getByCamera(Activity activity) {
        takePictureUri = createImagePathUri(activity);
        if (takePictureUri != null) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, takePictureUri);//输出路径（拍照后的保存路径）
            activity.startActivityForResult(i, GET_BY_CAMERA);
        } else {
            Toast.makeText(activity, "打开相机失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建一个图片地址uri,用于保存拍照后的照片
     *
     * @param activity
     * @return 图片的uri
     */
    public static Uri createImagePathUri(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //适配 Android Q
            String displayName = String.valueOf(System.currentTimeMillis());
            ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //SD 卡是否可用，可用则用 SD 卡，否则用内部存储
                takePictureUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                takePictureUri = activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            }
        } else {
            String pathName = new StringBuffer().append(FileUtils.getExtPicturesPath()).append(File.separator)
                    .append(System.currentTimeMillis()).append(".jpg").toString();
            takePictureFile = new File(pathName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //解决Android 7.0 拍照出现FileUriExposedException的问题
                String authority = activity.getPackageName() + ".fileProvider";
                takePictureUri = FileProvider.getUriForFile(activity, authority, takePictureFile);
            } else {
                takePictureUri = Uri.fromFile(takePictureFile);
            }
        }
        return takePictureUri;
    }

    /**
     * 处理拍照或相册获取的图片
     *
     * @param activity    上下文
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        Intent
     * @param cropEnabled 是否裁剪
     * @param w           输出宽
     * @param h           输出高
     * @param aspectX     宽比例
     * @param aspectY     高比例
     * @return picturePath 图片路径
     */
    public static String onActivityResult(Activity activity, int requestCode, int resultCode, Intent data,
                                          boolean cropEnabled, int w, int h, int aspectX, int aspectY) {
        String picturePath = null;//图片路径
        if (resultCode == activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
                    if (cropEnabled) {
                        activity.startActivityForResult(crop(activity, uri, w, h, aspectX, aspectY), CROP);
                    } else {
                        picturePath = ImageUtils.getImagePath(activity, uri);
                    }
                    break;
                case GET_BY_CAMERA:
                    uri = takePictureUri;
                    if (cropEnabled) {
                        activity.startActivityForResult(crop(activity, uri, w, h, aspectX, aspectY), CROP);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            picturePath = ImageUtils.getImagePath(activity, uri);
                        } else {
                            picturePath = takePictureFile.getAbsolutePath();
                        }
                    }
                    /*Android Q 以下发送广播通知图库更新，Android Q 以上使用 insert 的方式则会自动更新*/
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takePictureFile)));
                    }
                    break;
                case CROP:
                    dealCrop(activity);
                    File file = new File(cropPictureTempUri.getPath());
                    if (file != null) {
                        picturePath = file.getAbsolutePath();
                    }
                    break;
            }
        }
        return picturePath;
    }

    /**
     * 裁剪，例如：输出100*100大小的图片，宽高比例是1:1
     *
     * @param activity Activity
     * @param uri      图片的uri
     * @param w        输出宽
     * @param h        输出高
     * @param aspectX  宽比例
     * @param aspectY  高比例
     */
    public static Intent crop(Activity activity, Uri uri, int w, int h, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if (aspectX != 0 && aspectX == aspectY) {
            /*宽高比例相同时，华为设备的系统默认裁剪框是圆形的，这里统一改成方形的*/
            if (Build.MANUFACTURER.equals("HUAWEI")) {
                aspectX = 9998;
                aspectY = 9999;
            }
        }
        if (w != 0 && h != 0) {
            intent.putExtra("outputX", w);
            intent.putExtra("outputY", h);
        }
        if (aspectX != 0 || aspectY != 0) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }

        /*解决图片有黑边问题*/
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);

        /*解决跳转到裁剪提示“图片加载失败”问题*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        /*解决小米miui系统调用系统裁剪图片功能camera.action.CROP后崩溃或重新打开app的问题*/
        String pathName = new StringBuffer().append("file:///").append(FileUtils.getImageCacheDir(activity)).append(File.separator)
                .append(System.currentTimeMillis()).append(".jpg").toString();
        cropPictureTempUri = Uri.parse(pathName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropPictureTempUri);//输出路径(裁剪后的保存路径)
        // 输出格式
        intent.putExtra("outputFormat", "JPEG");
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 处理裁剪，获取裁剪后的图片
     */
    public static Bitmap dealCrop(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(cropPictureTempUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}