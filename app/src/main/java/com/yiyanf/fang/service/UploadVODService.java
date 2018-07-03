package com.yiyanf.fang.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoInfoReader;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.model.ReqPublishVOD;
import com.yiyanf.fang.api.model.ResGetCOSSign;
import com.yiyanf.fang.api.model.ResPublishVOD;
import com.yiyanf.fang.db.DBManager;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.entity.SimpleVideoInfo;
import com.yiyanf.fang.entity.Video;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UploadPresenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.util.CosUtil;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.videoupload.TXUGCPublish;
import com.yiyanf.fang.videoupload.TXUGCPublishTypeDef;
import com.yiyanf.fang.view.IView;
import org.greenrobot.eventbus.EventBus;

/**
 * 发布视频后台服务
 *
 * Created by Hition on 2017/11/6.
 */

public class UploadVODService extends Service implements IView, TXUGCPublishTypeDef.ITXVideoPublishListener {

    private String mVideoPath;
    private String title;
    private String coverPath;
    private long videoid;
    String vodSignature;
    /*private long duration;
    private long fileSize;*/

    private UploadPresenter uploadPresenter;
    private VideoPresenter presnter;

    private Handler mHandler = new Handler();
    private TXUGCPublish mVideoPublish = null;
    private NetchangeReceiver mNetchangeReceiver;
    private TXVideoInfoReader mTXVideoInfoReader;
    private int videoWidth;
    private int videoHeight;
    private Bitmap coverImg;
    private LoginModel userInfo;

    private SimpleVideoInfo uploadEvent = new SimpleVideoInfo();
    private CosUtil uploadUtils;
    private String cosCover;
    private ReqPublishVOD request;
    private boolean uploadAgain;

    @Override
    public void onCreate() {
        super.onCreate();
        userInfo = UserInfoCenter.getInstance().getLoginModel();
        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        uploadPresenter = new UploadPresenter(this);
        presnter = new VideoPresenter(this);
        uploadUtils = new CosUtil(this,new OnUploadCoverListener());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploadAgain = intent.getBooleanExtra("upload_again",false);
        mVideoPath = intent.getStringExtra("video_path");
        coverPath = intent.getStringExtra("cover_path");
        if(uploadAgain){
            videoid = intent.getLongExtra("video_id",0);
            vodSignature = intent.getStringExtra("video_signature");
            if(!TextUtils.isEmpty(vodSignature) && videoid>0){
                startPublish(vodSignature);
            }
        }else{
            uploadEvent.setStatus(SimpleVideoInfo.Status.START);
            title = intent.getStringExtra("title");
            String vodDesc = intent.getStringExtra("videodesc");
            double lng = intent.getDoubleExtra("longitude",0.0);
            double lat = intent.getDoubleExtra("latitude",0.0);

            request = new ReqPublishVOD();
            request.setTitle(title);
            request.setLng(lng);
            request.setLat(lat);
            if(!TextUtils.isEmpty(mVideoPath)){
                TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(mVideoPath);
                videoWidth = videoInfo.width;
                videoHeight = videoInfo.height;
                coverImg = videoInfo.coverImage;
                request.setWidth(videoWidth);
                request.setHeight(videoHeight);
                request.setFilesize(videoInfo.fileSize);
                request.setDuration(videoInfo.duration);
            }
            if(!TextUtils.isEmpty(vodDesc)){
                request.setVideodesc(vodDesc);
            }

            if(videoWidth > videoHeight){
                uploadPresenter.getCOSSign();
            }else{
                presnter.publishVideo(request);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(0 == data.getReturnValue()){
            switch (flag){
                case FangConstants.VOD_PUBLISH:
                    ResPublishVOD resPublish = (ResPublishVOD) data.getReturnData();
                    if(null!=resPublish){
                        videoid = resPublish.getVideoid();
                        vodSignature = resPublish.getVodsign();
                        // 通知关注开始上传了
                        uploadEvent.setVideoid(videoid);
                        uploadEvent.setCoverImg(coverImg);
                        uploadEvent.setCoverwidth(videoWidth);
                        uploadEvent.setCoverheight(videoHeight);
                        uploadEvent.setTitle(title);
                        if(userInfo != null){
//                            uploadEvent.setIsagent(1 == userInfo.getRole());
                            uploadEvent.setPublisherid(userInfo.getUserid());
                            uploadEvent.setPublisheravatar(userInfo.getThumbnail());
                            uploadEvent.setPublishername(userInfo.getNickname());
                        }
                        EventBus.getDefault().post(uploadEvent);

                        startPublish(vodSignature);
                    }
                    break;

                case FangConstants.COS_SIGN:
                    if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
                        if (data.getReturnData() instanceof ResGetCOSSign) {
                            ResGetCOSSign returnData = (ResGetCOSSign) data.getReturnData();
                            String sign = returnData.getSign();
                            uploadUtils.upLoad(sign,coverPath,false);
                        }
                    }
                /*case FangConstants.VOD_SIGN:
                    ResGetVODSign signData = (ResGetVODSign) data.getReturnData();
                    if(null!=signData){
                        String signature = signData.getVodsign();
                        startPublish(signature);
                    }
                    break;*/
            }

        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    /**
     * 取消发布视频
     */
    private boolean cancelPublish(){
        if(null != mVideoPublish){
            return mVideoPublish.canclePublish();
        }
        return false;
    }

    public void startPublish(final String mCosSignature){
        if(uploadAgain){
            uploadEvent.setStatus(SimpleVideoInfo.Status.UPLOADING_AGAIN);
        }else{
            uploadEvent.setStatus(SimpleVideoInfo.Status.UPLOADING);
        }
        uploadEvent.setProgress(0);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mVideoPublish == null){
                    mVideoPublish = new TXUGCPublish(UploadVODService.this, UserInfoCenter.getInstance().getUserId());
                }
                mVideoPublish.setListener(UploadVODService.this);

                TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
                param.signature = mCosSignature;
                param.videoPath = mVideoPath;
                param.coverPath = coverPath;
                int publishCode = mVideoPublish.publishVideo(param);
                if (publishCode != 0) {
                    ToastUtils.show(UploadVODService.this,"发布失败，错误码：" + publishCode);
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                if (null == mNetchangeReceiver) {
                    mNetchangeReceiver = new NetchangeReceiver();
                }
                registerReceiver(mNetchangeReceiver, intentFilter);
            }
        });
    }

    @Override
    public void onPublishProgress(long current, long max) {
//        uploadEvent.setStatus(SimpleVideoInfo.Status.UPLOADING);
        long persent = (100*current)/max;
        Long progress = Long.valueOf(persent);
        uploadEvent.setProgress(progress.intValue());
        EventBus.getDefault().post(uploadEvent);
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult txPublishResult) {
        int retCode = txPublishResult.retCode;
        if(0 == retCode){// 发布成功
            String fileid = txPublishResult.videoId;
            String frontcover;
            if(videoWidth > videoHeight){
                frontcover = cosCover;
            }else{
                frontcover = txPublishResult.coverURL;
            }
            String videoUrl = txPublishResult.videoURL;
            presnter.finishPublishVOD(videoid,frontcover,videoUrl,fileid,new OnPublishCompleteListener());
        }else{
            uploadEvent.setStatus(SimpleVideoInfo.Status.FAILED);
            EventBus.getDefault().post(uploadEvent);
            // 发布失败了，持久化到本地数据库
            Video mVideo = new Video(videoid,title);
            mVideo.setVideoPath(mVideoPath);
            mVideo.setCoverPath(coverPath);
            mVideo.setCoverwidth(uploadEvent.getCoverwidth());
            mVideo.setCoverheight(uploadEvent.getCoverheight());
            mVideo.setPublisherid(uploadEvent.getPublisherid());
            mVideo.setPublisheravatar(uploadEvent.getPublisheravatar());
            mVideo.setPublishername(uploadEvent.getPublishername());
            mVideo.setSignature(vodSignature);
            DBManager dbManager = new DBManager();
            dbManager.addFailVideo(mVideo);
            dbManager.close();
        }
        // 停止service
//        stopSelf();
    }

    private class OnUploadCoverListener implements CosUtil.OnUploadListener {

        @Override
        public void onSuccess(int code, String url) {
            // 头像上传COS成功
            if (0 == code) {
                cosCover = url;
                presnter.publishVideo(request);
            }
        }

        @Override
        public void onFailed(int errorCode, String msg) {

        }
    }

    private class OnPublishCompleteListener implements BaseListener{

        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                LogUtil.v("hition==","视频上传至腾讯云成功,已通知业务服务器!");
                // 上传成功了就删除之前本地保存的数据
                if(uploadAgain){
                    DBManager dbManager = new DBManager();
                    dbManager.deleteFailVideo(videoid);
                    dbManager.close();
                }
                uploadEvent.setStatus(SimpleVideoInfo.Status.IDLE);
                EventBus.getDefault().post(uploadEvent);
            }else{
                LogUtil.e("hition====",data.getReturnMsg()+"  :  "+data.getReturnValue());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    public class NetchangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (!NetWorkUtil.isNetworkConnected(UploadVODService.this)) {
                    ToastUtils.show(UploadVODService.this,"网络连接断开，视频上传失败");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presnter.onUnsubscribe();
        if (mNetchangeReceiver != null) {
            this.getApplicationContext().unregisterReceiver(mNetchangeReceiver);
        }
    }


}
