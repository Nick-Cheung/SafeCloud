package com.nick.safecloud.util;

import android.widget.Toast;

import com.nick.safecloud.base.BaseApplication;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class ToastUtil {

    public static void showText(String msg) {
        Toast.makeText(BaseApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
