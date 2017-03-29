package com.nick.safecloud.utils;

/**
 * 字符串工具类
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
