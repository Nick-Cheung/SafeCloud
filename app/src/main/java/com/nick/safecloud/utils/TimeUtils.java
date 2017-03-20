package com.nick.safecloud.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Sparrow on 2017/3/20.
 */

public class TimeUtils {


    public static String stamp2Date(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(time * 1000);

    }

}
