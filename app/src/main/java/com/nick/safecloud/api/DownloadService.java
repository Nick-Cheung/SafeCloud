package com.nick.safecloud.api;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.nick.safecloud.R;
import com.nick.safecloud.base.BaseApplication;
import com.nick.safecloud.utils.CookieUtils;
import com.nick.safecloud.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

import static com.nick.safecloud.utils.FileUtils.getDownloadFolder;

/**
 * Created by Sparrow on 2017/3/18.
 */

public class DownloadService extends Service{

    public  static String DOWNLAOD_FILE_URL_FORMAT = "http://c.pcs.baidu.com/rest/2.0/pcs/file?method=download&app_id=250528&path=%s";


    public static final String PARAM_URL = "url";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_DECODE = "decode";



    NotificationManager mNotificationManager;
    NotificationCompat.Builder mNotificationBuilder;
    int notificationID;

    String url;
    String filename;
    boolean decode;



    public static void startMe(Context context, String url, String fileName, boolean decode) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_FILENAME, fileName);
        intent.putExtra(PARAM_DECODE, decode);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void init(Intent intent) {
        url = intent.getStringExtra(PARAM_URL);
        filename = intent.getStringExtra(PARAM_FILENAME);
        decode = intent.getBooleanExtra(PARAM_DECODE, false);

        notificationID = url.hashCode();

        mNotificationBuilder = new NotificationCompat.Builder(BaseApplication.getInstance().getApplicationContext());
        mNotificationBuilder.setSmallIcon(R.drawable.ic_cloud_download);
        mNotificationBuilder.setContentTitle(filename);
        mNotificationBuilder.setContentText("正在下载");
        mNotificationManager = (NotificationManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder.setProgress(100, 0, false);
        mNotificationManager.notify(notificationID, mNotificationBuilder.build());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init(intent);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }



    private void startDownload() {
        final String savePathFolder = getDownloadFolder();
        OkHttpUtils.get().url(String.format(DOWNLAOD_FILE_URL_FORMAT, url))
                .addHeader("cookie", CookieUtils.getCookie())
                .build()
                .execute(new FileCallBack(savePathFolder, filename) {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        mNotificationBuilder.setContentText("正在下载" + (int) (progress * 100) + "%");
                        mNotificationBuilder.setProgress(100, (int) (progress * 100), false);
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        Log.i("progress", "" + progress * 100);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        mNotificationBuilder.setContentText("下载失败");
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        ToastUtils.showText("下载失败");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        mNotificationBuilder.setContentText("下载成功");
                        mNotificationManager.notify(notificationID, mNotificationBuilder.build());
                        ToastUtils.showText("下载成功,文件路径为" + savePathFolder + filename);
                    }
                });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
