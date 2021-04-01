package com.month.markethelper.utils;

import android.widget.Toast;

import com.month.markethelper.base.BaseApplication;

public class ToastUtils {

    private static Toast mToast;
    private static Toast mLongToast;

    public static void showToast(String msg){

        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showLongToast(String msg) {
        if (mLongToast == null) {
            mLongToast = Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        } else {
            mLongToast.setText(msg);
        }
        mLongToast.show();
    }
}
