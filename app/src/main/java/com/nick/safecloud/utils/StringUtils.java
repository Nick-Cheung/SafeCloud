package com.nick.safecloud.utils;

/**
 * Created by Sparrow on 2017/3/20.
 */

public class StringUtils {

    public static String getCapacityToshow(long size) {
        if(size / 1024 == 0 ) {
            return size + "B";
        }
        if(size / (1024 * 1024) == 0) {
            return (size / 1024) + "KB";
        }

        if(size / (1024 * 1024 * 1024) == 0) {
            return (size / (1024 * 1024)) + "MB";
        }

        return (size / (1024 * 1024 * 1024)) + "GB";
    }
}
