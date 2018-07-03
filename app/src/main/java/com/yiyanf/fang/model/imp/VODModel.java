package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResAttachedArea;
import com.yiyanf.fang.api.model.ResAttachedBuilding;
import com.yiyanf.fang.api.model.ResFeaturedList;
import com.yiyanf.fang.api.model.ResFetchVodList;
import com.yiyanf.fang.api.model.ResGetVODSign;
import com.yiyanf.fang.api.model.ResGetVodDetails;
import com.yiyanf.fang.api.model.ResPublishVOD;
import com.yiyanf.fang.api.model.ResPublishVideoCategory;
import com.yiyanf.fang.api.model.ResRelatedVideos;
import com.yiyanf.fang.api.model.ResStartPlay;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.VideoFilter;
import com.yiyanf.fang.entity.VideoSort;
import com.yiyanf.fang.model.BaseListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hition on 2017/10/31.
 */

public class VODModel {
    private BaseListener listener;

    public VODModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 获取上传视频签名
     * @return   视频上传签名
     */
    public Subscription getVODSign(long videoId){
        Observable<BaseResponse<ResGetVODSign>> observable = ApiManager.getInstance().getVODSign(videoId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetVODSign>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_SIGN);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetVODSign> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_SIGN);
                    }
                });
    }


    /**
     * 发布视频
     * @param title                             标题
     * @param areaid                            区域id
     * @param buildingid                        楼盘id
     * @param cateid                            类别id
     * @param desc                              视频描述
     */
    /*public Subscription publishVOD(String title,int areaid, String areaName,int buildingid,String buildingName,int cateid,int houseTypeid,String desc,long size){
        Observable<BaseResponse<ResPublishVOD>> observable = ApiManager.getInstance().publishVOD(title,areaid,areaName,buildingid,buildingName,cateid,houseTypeid,desc,size);
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
    }*/

    /**
     * 拉取视频（点播）列表
     * @param filter                筛选条件
     * @param sort                  排序方式
     * @param pageno                当前页码
     * @param pagesize              每页大小
     */
    public Subscription fetchVodList(VideoFilter filter, VideoSort sort, int pageno, int pagesize){
        Observable<BaseResponse<ResFetchVodList>> observable = ApiManager.getInstance().fetchVodList(filter,sort,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_FETCHVODLIST);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_FETCHVODLIST);
                    }
                });
    }

    /**
     * 获取精选视频列表
     */
    public Subscription featuredList(){
        Observable<BaseResponse<ResFeaturedList>> observable = ApiManager.getInstance().featuredList();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResFeaturedList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_FEATUREDLIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResFeaturedList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_FEATUREDLIST);
                    }
                });
    }

    /**
     * 开始播放视频
     * @param videoid                  视频 id
     */
    public Subscription startPlayVideo(long videoid){
        Observable<BaseResponse<ResStartPlay>> observable = ApiManager.getInstance().startPlay(videoid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResStartPlay>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_PLAY_START);
                    }

                    @Override
                    public void onNext(BaseResponse<ResStartPlay> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_PLAY_START);
                    }
                });
    }

    /**
     * 退出播放视频
     * @param videoid               视频id
     * @param playid                播放记录id
     */
    public Subscription exitPlayVideo(long videoid, long playid){
        Observable<BaseResponse> observable = ApiManager.getInstance().stopPlay(videoid,playid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_PLAY_EXIT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_PLAY_EXIT);
                    }
                });
    }

    /**
     * 相关视频
     * @param videoid    视频id
     */
    public Subscription relatedVideos(long videoid){
        Observable<BaseResponse<ResRelatedVideos>> observable = ApiManager.getInstance().relatedVideos(videoid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResRelatedVideos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_RELATED_VIDEOS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResRelatedVideos> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_RELATED_VIDEOS);
                    }
                });
    }

    /**
     * 视频所属楼盘
     * @param videoid 视频id
     */
    public Subscription attachedBuilding(long videoid){
        Observable<BaseResponse<ResAttachedBuilding>> observable = ApiManager.getInstance().attachedBuilding(videoid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResAttachedBuilding>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_ATTACHED_BUILDING);
                    }

                    @Override
                    public void onNext(BaseResponse<ResAttachedBuilding> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_ATTACHED_BUILDING);
                    }
                });
    }

    /**
     * 所属区域
     * @param videoid    视频id
     *
     */
    public Subscription attachedArea(long videoid){
        Observable<BaseResponse<ResAttachedArea>> observable = ApiManager.getInstance().attachedArea(videoid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResAttachedArea>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_ATTACHED_AREA);
                    }

                    @Override
                    public void onNext(BaseResponse<ResAttachedArea> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_ATTACHED_AREA);
                    }
                });
    }

    /**
     *  获取视频资源分类
     *
     */
    public Subscription publishVideoCategory(){
        Observable<BaseResponse<ResPublishVideoCategory>> observable = ApiManager.getInstance().publishVideoCategory();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResPublishVideoCategory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VIDEO_CATEGORY);
                    }

                    @Override
                    public void onNext(BaseResponse<ResPublishVideoCategory> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VIDEO_CATEGORY);
                    }
                });
    }

    /**
     *  获取我的收藏
     *
     */
    public Subscription getMineFavoriteVod(int pageno, int pagesize){
        Observable<BaseResponse<ResFetchVodList>> observable = ApiManager.getInstance().getMineFavoriteVod(pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResFetchVodList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResFetchVodList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }
}
