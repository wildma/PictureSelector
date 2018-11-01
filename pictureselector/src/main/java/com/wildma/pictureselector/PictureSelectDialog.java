package com.wildma.pictureselector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * Author       wildma
 * Github       https://github.com/wildma
 * CreateDate   2018/6/10
 * Desc	        ${选择图片Dialog}
 */
public class PictureSelectDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button  mBtnCamera;
    private Button  mBtnAlbum;
    private Button  mBtnCancel;

    public PictureSelectDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PictureSelectDialog(Context context, int theme) {
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
        setCanceledOnTouchOutside(false);
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

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {  //返回键监听
                    hideDialog();
                    mListener.onItemClick(Constant.CANCEL);//返回键关闭dialog
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOutOfBounds(getContext(), event)) {
            hideDialog();
            mListener.onItemClick(Constant.CANCEL);//触摸dialog外部关闭dialog
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断当前用户触摸是否超出了Dialog的显示区域
     *
     * @param context
     * @param event
     * @return
     */
    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_camera) {
            hideDialog();
            mListener.onItemClick(Constant.CAMERA);
        } else if (v.getId() == R.id.btn_album) {
            hideDialog();
            mListener.onItemClick(Constant.ALBUM);
        } else if (v.getId() == R.id.btn_cancel) {
            hideDialog();
            mListener.onItemClick(Constant.CANCEL);//点击“取消”关闭dialog
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
