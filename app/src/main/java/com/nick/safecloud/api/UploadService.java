package com.nick.safecloud.api;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.nick.safecloud.R;
import com.nick.safecloud.base.BaseApplication;
import com.nick.safecloud.util.CookieUtil;
import com.nick.safecloud.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Sparrow on 2017/3/18.
 */

public class UploadService extends Service {

    public static final String UPLOAD_URL = "http://c.pcs.baidu.com/rest/2.0/pcs/file";

    public static final String PARAM_FILE_PATH = "filepath";
    public static final String PARAM_REMOTE_PATH = "remotepath";
    public static final String PARAM_ENCODE = "encode";

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mNotificationBuilder;
    int notificationID;

    String filePath;
    String fileName;
    String remotePath;
    boolean encode;
    File uploadFile;


    public static void startMe(Context context, String filePath, String remotePath, boolean encode) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra(PARAM_FILE_PATH, filePath);
        intent.putExtra(PARAM_REMOTE_PATH, remotePath);
        intent.putExtra(PARAM_ENCODE, encode);
        context.startService(intent);
    }


    private void init(Intent intent) {
        filePath = intent.getStringExtra(PARAM_FILE_PATH);
        remotePath = intent.getStringExtra(PARAM_REMOTE_PATH);
        encode = intent.getBooleanExtra(PARAM_ENCODE, false);


        uploadFile = new File(filePath);
        fileName = uploadFile.getName();

        notificationID = filePath.hashCode();
        mNotificationBuilder = new NotificationCompat.Builder(BaseApplication.getInstance().getApplicationContext());
        mNotificationBuilder.setSmallIcon(R.drawable.ic_cloud_download);
        mNotificationBuilder.setContentTitle(fileName);
        mNotificationBuilder.setContentText("正在上传");
        mNotificationManager = (NotificationManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder.setProgress(100, 0, false);
        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        init(intent);

        startUpload();
        return super.onStartCommand(intent, flags, startId);
    }


    void startUpload() {

        OkHttpUtils.post()
                .addFile(fileName, fileName, uploadFile)
                .addParams("method", "upload")
                .addParams("ondup", "overwrite")
                .addParams("app_id", "250528")
                .addParams("path", remotePath)
                .addHeader("cookie", CookieUtil.getCookie())
                .url(UPLOAD_URL)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        mNotificationBuilder.setContentText("正在上传" + (int) (progress * 100) + "%");
                        mNotificationBuilder.setProgress(100, (int) (progress * 100), false);
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        super.inProgress(progress, total, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mNotificationBuilder.setContentText("上传失败");
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mNotificationBuilder.setContentText("上传成功");
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        ToastUtil.showText("上传" + fileName + "成功");
                    }
                });


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
