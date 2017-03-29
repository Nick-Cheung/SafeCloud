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
import com.nick.safecloud.utils.CookieUtils;
import com.nick.safecloud.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * 上传的Service
 */

public class UploadService extends Service {

    public static final String UPLOAD_URL = "http://c.pcs.baidu.com/rest/2.0/pcs/file?method=upload&ondup=newcopy&app_id=250528&path=%s";

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



    public static void startMe(Context context, String filePath, String remotePath, boolean encode) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra(PARAM_FILE_PATH, filePath);
        intent.putExtra(PARAM_REMOTE_PATH, remotePath);
        intent.putExtra(PARAM_ENCODE, encode);
        context.startService(intent);
    }


    private void init(Intent intent) {
        filePath = intent.getStringExtra(PARAM_FILE_PATH);

        File file = new File(filePath);
        fileName = file.getName();

        remotePath = intent.getStringExtra(PARAM_REMOTE_PATH) + "/" + fileName;
        encode = intent.getBooleanExtra(PARAM_ENCODE, false);


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

        if(encode) {
            encodeFile();
        } else {
            startUpload(filePath);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 加密文件
     */
    private void encodeFile() {
        Observable
                .create(new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        //加密的地方
                    }
                }).compose(new ApiScheduler<String>())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                        startUpload(s);          //加密成功后开始上传
                    }
                });
    }


    /**
     * 上传文件
     * @param filePath 文件路径
     */
    void startUpload(String filePath) {

        ToastUtils.showText(filePath);
        File file = new File(filePath);
        OkHttpUtils.post()
                .addFile("filename", file.getName(), file)
                .addHeader("cookie", CookieUtils.getCookie())
                .url(String.format(UPLOAD_URL, remotePath))
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
                        ToastUtils.showText("上传" + fileName + "成功");
                    }
                });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
