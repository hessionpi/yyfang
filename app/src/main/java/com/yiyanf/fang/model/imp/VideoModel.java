package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ReqPublishVOD;
import com.yiyanf.fang.api.model.ResAttentionVideos;
import com.yiyanf.fang.api.model.ResGetVideoDetails;
import com.yiyanf.fang.api.model.ResPublishVOD;
import com.yiyanf.fang.api.model.ResRecommendVideos;
import com.yiyanf.fang.api.model.ResVideoClassify;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hition on 2018/3/6.
 */

public class VideoModel {

    private BaseListener listener;

    public VideoModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 获取推荐视频列表
     * @param sortBy            排序方式
     * @param pNo               页码
     * @param pSize             每页多少条
     */
    public Subscription getRecommendVideos(int sortBy,int pNo,int pSize){
        Observable<BaseResponse<ResRecommendVideos>> observable = ApiManager.getInstance().getRecommendVideos(sortBy,pNo,pSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResRecommendVideos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VIDEOS_RECOMMEND);
                    }

                    @Override
                    public void onNext(BaseResponse<ResRecommendVideos> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VIDEOS_RECOMMEND);
                    }
                });
    }

    /**
     * 获取关注视频列表
     * @param sortBy                排序方式
     * @param pNo                   页码
     * @param pSize                 每页多少条
     */
    public Subscription getAttentionVideos(int sortBy,int pNo,int pSize){
        Observable<BaseResponse<ResAttentionVideos>> observable = ApiManager.getInstance().getAttentionVideos(sortBy,pNo,pSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResAttentionVideos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VIDEOS_ATTENTION);
                    }

                    @Override
                    public void onNext(BaseResponse<ResAttentionVideos> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VIDEOS_ATTENTION);
                    }
                });
    }

    /**
     * 获取视频分类
     *
     */
    public Subscription getVideoClassification(){
        Observable<BaseResponse<ResVideoClassify>> observable = ApiManager.getInstance().getVideoClassification();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResVideoClassify>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VIDEO_CATEGORY);
                    }

                    @Override
                    public void onNext(BaseResponse<ResVideoClassify> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VIDEO_CATEGORY);
                    }
                });
    }

    /**
     * 发布视频
     * @param reqPublishVOD   发布视频请求实体
     *
     */
    public Subscription publishVideo(ReqPublishVOD reqPublishVOD){
        Observable<BaseResponse<ResPublishVOD>> observable = ApiManager.getInstance().publishVOD(reqPublishVOD);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResPublishVOD>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_PUBLISH);
                    }

                    @Override
                    public void onNext(BaseResponse<ResPublishVOD> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_PUBLISH);
                    }
                });
    }

    public Subscription finishPublishVOD(long videoId, String frontCover, String videoURL, String fileId,final BaseListener onCompleteListener){
        Observable<BaseResponse> observable = ApiManager.getInstance().finishPublishVOD(videoId,frontCover,videoURL,fileId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        onCompleteListener.onFailed(t, FangConstants.VOD_PUBLISH);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        onCompleteListener.onSuccess(baseResponse, FangConstants.VOD_PUBLISH);
                    }
                });
    }

    /**
     * 获取视频详情
     * @param videoId               视频id
     *
     */
    public Subscription getVideoDetails(long videoId){
        Observable<BaseResponse<ResGetVideoDetails>> observable = ApiManager.getInstance().getVideoDetails(videoId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetVideoDetails>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_VIDEO_DETAILS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetVideoDetails> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_VIDEO_DETAILS);
                    }
                });
    }

    /**
     * 关注/取消关注
     * @param attentionUserId             被关注人id
     * @param flag                    1 关注    0 取消关注
     */
    public Subscription attention(String attentionUserId,int flag,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().attention(attentionUserId,0,flag);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 收藏/取消收藏视频
     * @param videoId           视频id
     * @param flag              1 收藏    0 取消收藏
     * @param listener          收藏或取消收藏成功监听
     */
    public Subscription favorite(String videoId,int flag,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().favorite(videoId,1,flag);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 分享
     * @param shareUrl                      分享地址
     * @param videoId                       视频id
     * @param channel                       分享渠道
     */
    public Subscription makeShare(String shareUrl,String videoId,int channel,final BaseListener sharelistener){
        Observable<BaseResponse> observable = ApiManager.getInstance().makeShare(shareUrl,videoId,2,channel);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        sharelistener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        sharelistener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 删除我发布的视频
     * @param videoid                   视频id
     */
    public Subscription deleteVideo(long videoid,final BaseListener sharelistener){
        Observable<BaseResponse> observable = ApiManager.getInstance().deleteVideo(videoid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        sharelistener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        sharelistener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

}
