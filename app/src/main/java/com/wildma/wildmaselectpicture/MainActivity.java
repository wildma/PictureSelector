package com.wildma.wildmaselectpicture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ImageView mIvImage;
    private final int PERMISSION_CODE_FIRST  = 1;//权限请求码
    private final int PERMISSION_CODE_SECOND = 2;//权限请求码
    private SelectPictureDialog mSelectPictureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvImage = (ImageView) findViewById(R.id.iv_image);

        //请求应用需要的所有权限
        boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, PERMISSION_CODE_FIRST,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
    }

    /**
     * 选择图片按钮点击事件
     *
     * @param view
     */
    public void selectPicture(View view) {
        boolean checkPermissionSecond = PermissionUtils.checkPermissionSecond(this, PERMISSION_CODE_SECOND,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        if (checkPermissionSecond) {
            selectPicture();
        } else {
            Toast.makeText(getApplicationContext(), "请手动打开该应用需要的权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理请求权限的响应
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 请求权限结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissions = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isPermissions = false;
            }
        }
        if (isPermissions) {
            Log.d(TAG, "onRequestPermissionsResult: " + "允许所有权限");
            if (requestCode == PERMISSION_CODE_SECOND) {
                selectPicture();
            }
        } else {
            Log.d(TAG, "onRequestPermissionsResult: " + "有权限不允许");
        }
    }

    /**
     * 选择图片
     */
    public void selectPicture() {
        mSelectPictureDialog = new SelectPictureDialog(this, R.style.ActionSheetDialogStyle);
        mSelectPictureDialog.setOnItemClickListener(new SelectPictureDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int type) {
                if (type == Constant.CAMERA) {
                    SelectPictureUtils.getByCamera(MainActivity.this);
                } else if (type == Constant.ALBUM) {
                    SelectPictureUtils.getByAlbum(MainActivity.this);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //裁剪后的图片宽高为400*400，选择图片时的裁剪比例是1:1
        Bitmap bitmap = SelectPictureUtils.onActivityResult(this, requestCode, resultCode, data, 480, 480, 1, 1);
        if (bitmap != null) {
            mIvImage.setImageBitmap(bitmap);
        }
    }

}
