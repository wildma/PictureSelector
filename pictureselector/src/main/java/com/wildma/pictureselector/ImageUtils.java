package com.wildma.pictureselector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Author   wildma
 * Github   https://github.com/wildma
 * Date     2018/6/24
 * Desc     ${图片相关工具类}
 */

public class ImageUtils {

    /**
     * 获取Bitmap
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath) {
        if (isSpace(filePath))
            return null;
        return BitmapFactory.decodeFile(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null)
            return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取图片路径
     *
     * @param context Context
     * @param uri     图片 Uri
     * @return 图片路径
     */
    public static String getImagePath(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取图片 Uri
     *
     * @param context Context
     * @param path    图片路径
     * @return 图片 Uri
     */
    public static Uri getImageUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ", new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}