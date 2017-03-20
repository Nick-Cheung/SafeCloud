package com.nick.safecloud.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nick.safecloud.base.BaseApplication;

/**
 * 保存Cookie的工具类
 */

public class CookieUtils {


    public static final String KEY_COOKIE= "cookie";
    public static final String FILE_NAME = "cookies";

    public static String getCookie() {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString(KEY_COOKIE, "");
        Log.i("cookie", cookie);
        return cookie;
    }



    public static void putCookie(String cookie) {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("cookie", cookie);
        editor.putString(KEY_COOKIE, cookie);
        editor.commit();
    }
}
