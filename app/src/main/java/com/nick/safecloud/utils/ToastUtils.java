package com.nick.safecloud.utils;

import android.widget.Toast;

import com.nick.safecloud.base.BaseApplication;

/**
 * Toast工具类
 */

public class ToastUtils {

    public static void showText(String msg) {
        Toast.makeText(BaseApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
