package com.wildma.wildmaselectpicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wildma.pictureselector.ImageUtils;
import com.wildma.pictureselector.PictureSelector;


public class MainActivity extends AppCompatActivity {
    private ImageView mIvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvImage = (ImageView) findViewById(R.id.iv_image);
    }

    /**
     * 选择图片按钮点击事件
     *
     * @param view
     */
    public void selectPicture(View view) {
        /**
         * create方法参数一是上下文，在activity中传activity.this，在fragment中传fragment.this。参数二为请求码，用于结果回调onActivityResult中判断
         * selectPicture方法参数分别为图片的裁剪宽、裁剪高、宽比例、高比例。默认不传则为宽200，高200，宽高比例为1：1。
         */
        PictureSelector
                .create(MainActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(200, 200, 1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                mIvImage.setImageBitmap(ImageUtils.getBitmap(picturePath));
            }
        }
    }
}
