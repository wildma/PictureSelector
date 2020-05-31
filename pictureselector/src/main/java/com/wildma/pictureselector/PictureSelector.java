package com.wildma.pictureselector;

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/24
 * Desc	        ${图片选择器}
 */
public class PictureSelector {

    public static final int                     SELECT_REQUEST_CODE = 0x15;//选择图片请求码
    public static final String                  PICTURE_RESULT      = "picture_result";//选择的图片结果
    private             int                     mRequestCode;
    private final       WeakReference<Activity> mActivity;
    private final       WeakReference<Fragment> mFragment;

    /**
     * 创建 PictureSelector（用于 Activity）
     *
     * @param activity    Activity
     * @param requestCode 请求码，用于结果回调 onActivityResult() 中判断
     * @return PictureSelector
     */
    public static PictureSelector create(Activity activity, int requestCode) {
        return new PictureSelector(activity, requestCode);
    }

    /**
     * 创建 PictureSelector（用于 Fragment）
     *
     * @param fragment    Fragment
     * @param requestCode 请求码，用于结果回调 onActivityResult() 中判断
     * @return PictureSelector
     */
    public static PictureSelector create(Fragment fragment, int requestCode) {
        return new PictureSelector(fragment, requestCode);
    }

    private PictureSelector(Activity activity, int requestCode) {
        this(activity, (Fragment) null, requestCode);
    }

    private PictureSelector(Fragment fragment, int requestCode) {
        this(fragment.getActivity(), fragment, requestCode);
    }

    private PictureSelector(Activity activity, Fragment fragment, int requestCode) {
        this.mActivity = new WeakReference(activity);
        this.mFragment = new WeakReference(fragment);
        this.mRequestCode = requestCode;
    }

    /**
     * 选择图片（默认不裁剪）
     */
    public void selectPicture() {
        selectPicture(false, 0, 0, 0, 0);
    }

    /**
     * 选择图片（根据参数决定是否裁剪）
     *
     * @param cropEnabled 是否裁剪
     */
    public void selectPicture(boolean cropEnabled) {
        selectPicture(cropEnabled, 0, 0, 0, 0);
    }

    /**
     * 选择图片（指定宽高及宽高比例裁剪）
     *
     * @param cropEnabled 是否裁剪
     * @param cropWidth   裁剪宽
     * @param cropHeight  裁剪高
     * @param ratioWidth  宽比例
     * @param ratioHeight 高比例
     */
    public void selectPicture(boolean cropEnabled, int cropWidth, int cropHeight, int ratioWidth, int ratioHeight) {
        Activity activity = this.mActivity.get();
        Fragment fragment = this.mFragment.get();
        Intent intent = new Intent(activity, PictureSelectActivity.class);
        intent.putExtra(PictureSelectActivity.ENABLE_CROP, cropEnabled);
        intent.putExtra(PictureSelectActivity.CROP_WIDTH, cropWidth);
        intent.putExtra(PictureSelectActivity.CROP_HEIGHT, cropHeight);
        intent.putExtra(PictureSelectActivity.RATIO_WIDTH, ratioWidth);
        intent.putExtra(PictureSelectActivity.RATIO_HEIGHT, ratioHeight);
        if (fragment != null) {
            fragment.startActivityForResult(intent, mRequestCode);
        } else {
            activity.startActivityForResult(intent, mRequestCode);
        }
    }
}

