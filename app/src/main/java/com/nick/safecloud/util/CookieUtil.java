package com.nick.safecloud.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.nick.safecloud.base.BaseApplication;

/**
 * 保存Cookie的工具类
 */

public class CookieUtil {


    public static final String KEY_COOKIE= "cookie";
    public static final String FILE_NAME = "cookies";

    public static String getCookie() {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COOKIE, "");
    }



    public static void putCookie(String cookie) {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COOKIE, cookie);
        editor.commit();
    }
}
