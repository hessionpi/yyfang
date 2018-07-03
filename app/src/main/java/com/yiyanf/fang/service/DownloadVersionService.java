package com.yiyanf.fang.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yiyanf.fang.R;
import java.io.File;

/**
 * 下载新版本安装的Service
 * @author Hition
 *
 * Created by Hition on 2018/2/1.
 */

public class DownloadVersionService extends Service {

    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews rv;
    private HttpHandler<File> downloadHandler;
    private File apkFileDir;
    private String filename = "";


    @Override
    public void onCreate() {
        super.onCreate();
        File storageDirectory = Environment.getExternalStorageDirectory();
        apkFileDir = new File(storageDirectory,"/yiyanf/download");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downloadVersion = intent.getStringExtra("downloading_version_name");
        String downloadUrl = intent.getStringExtra("downloading_url");

        if(!TextUtils.isEmpty(downloadUrl)){
            createNotification(downloadVersion);
            downLoadAPK(downloadUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化通知对象
     * @param version            新版本名
     */
    public void createNotification(String version) {
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.tickerText = version + getString(R.string.version_downloading);
        notification.icon = R.mipmap.ic_launcher;
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_INSISTENT;
        // 自定义  Notification 的显示
        rv = new RemoteViews(getPackageName(),R.layout.notification_for_version);
        rv.setTextViewText(R.id.notification_title, version +" "+ getString(R.string.downloading));
        rv.setProgressBar(R.id.pb_notificationProgress, 100, 1, false);
        notification.contentView = rv;
        notificationManager.notify(0, notification);
    }

    /**
     * 多线程断点实现下载安装包
     * @param downLoadUrl         下载地址
     */
    private void downLoadAPK(String downLoadUrl) {
        HttpUtils utils = new HttpUtils();
        //创建保存地址
        filename = downLoadUrl.substring(downLoadUrl.lastIndexOf("/")+1,downLoadUrl.length());
        downloadHandler = utils.download(downLoadUrl, apkFileDir+"/"+filename,
                true,   //开启断点下载
                false, //如果响应头中有文件名，就使用响应头的文件名
                new RequestCallBack<File>() {

                    @Override
                    public void onLoading(long total, long current,boolean isUploading) {
                        // 改变通知栏
                        rv.setTextViewText(R.id.notification_title, getString(R.string.app_name)+getString(R.string.downloading)+current*100/total + "%");
                        rv.setProgressBar(R.id.pb_notificationProgress, 100,(int)(current*100/total), false);
                        notification.contentView = rv;
                        notificationManager.notify(0, notification);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        notificationManager.cancel(0);
                        downloadHandler.cancel();
                        installApk();
                        stopSelf();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        //  文件已经下载完成,无需重复下载,直接安装
                        if(error.getExceptionCode() == 416){
                            notificationManager.cancel(0);
                            installApk();
                        }else{
                            //下载失败
                            downloadHandler.cancel();
                            rv.setTextViewText(R.id.notification_title,getString(R.string.app_name)+getString(R.string.down_fail));
                            notification.contentView = rv;
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            notificationManager.notify(0, notification);
                        }
                    }
                });
    }


    /**
     * 下载完成自动安装apk
     */
    private void installApk() {
        // 下载完成，点击安装
        File file = new File(apkFileDir +"/"+filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri apkUri;
        //判读版本是否在7.0以上
        if(Build.VERSION.SDK_INT >= 24) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            apkUri = FileProvider.getUriForFile(this, "com.yiyanf.fang.InstallProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            apkUri = Uri.fromFile(file);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
