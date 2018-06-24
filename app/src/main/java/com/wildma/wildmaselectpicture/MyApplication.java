package com.wildma.wildmaselectpicture;


import android.app.Application;
import android.content.Context;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2017/9/12
 * Desc	        ${TODO}
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
