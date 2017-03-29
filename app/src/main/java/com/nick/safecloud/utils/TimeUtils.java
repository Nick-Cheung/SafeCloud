package com.nick.safecloud.utils;

import java.text.SimpleDateFormat;

/**
 * 时间显示工具类
 */

public class TimeUtils {


    public static String stamp2Date(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(time * 1000);

    }

}
