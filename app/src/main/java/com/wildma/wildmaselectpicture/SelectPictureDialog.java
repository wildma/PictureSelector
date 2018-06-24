package com.wildma.wildmaselectpicture;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * Author       wildma
 * Github       https://github.com/wildma
 * CreateDate   2018/6/10
 * Desc	        ${选择图片Dialog}
 */
public class SelectPictureDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button  mBtnCamera;
    private Button  mBtnAlbum;
    private Button  mBtnCancel;

    public SelectPictureDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SelectPictureDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initDialog();
    }

    /**
     * 初始化Dialog
     */
    public void initDialog() {
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setGravity(Gravity.RELATIVE_LAYOUT_DIRECTION | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        show();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog() {
        cancel();
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_photo);

        initView();
        initListener();
    }

    private void initView() {
        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mBtnAlbum = (Button) findViewById(R.id.btn_album);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void initListener() {
        mBtnCamera.setOnClickListener(this);
        mBtnAlbum.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                hideDialog();
                mListener.onItemClick(Constant.CAMERA);
                break;
            case R.id.btn_album:
                hideDialog();
                mListener.onItemClick(Constant.ALBUM);
                break;
            case R.id.btn_cancel:
                hideDialog();
                mListener.onItemClick(Constant.CANCEL);
                break;
        }
    }

    OnItemClickListener mListener;

    public interface OnItemClickListener {
        /**
         * 点击条目
         *
         * @param type 0取消，1拍照，2相册
         */
        void onItemClick(int type);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
