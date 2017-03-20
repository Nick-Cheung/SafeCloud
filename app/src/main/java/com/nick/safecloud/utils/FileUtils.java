package com.nick.safecloud.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.net.URISyntaxException;

/**
 * Created by Sparrow on 2017/3/20.
 */

public class FileUtils {

    public static final String DOWNLOAD_URL = "/SafeCloud/download/";



    public static String getDownloadFolder() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOAD_URL;
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
