package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResAllLiveForecast;
import com.yiyanf.fang.api.model.ResFetchGroupMemberList;
import com.yiyanf.fang.api.model.ResFetchLiveList;
import com.yiyanf.fang.api.model.ResGetLVBAddr;
import com.yiyanf.fang.api.model.ResGetLiveDetails;
import com.yiyanf.fang.api.model.ResLatestLiveForecast;
import com.yiyanf.fang.api.model.ResLiveApplyList;
import com.yiyanf.fang.api.model.ResSponsorLive;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LiveFilter;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 直播相关的model处理
 *
 * Created by Hition on 2017/9/19.
 */

public class LiveModel {


    /**
     * 发起直播
     *
     */
    public Subscription sponsorLive(final BaseListener<ResSponsorLive> listener) {
        Observable<BaseResponse<ResSponsorLive>> observable = ApiManager.getInstance().sponsorLive();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResSponsorLive>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResSponsorLive> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }



    /**
     * 获取申请直播列表
     * @param listener          监听
     */
    public Subscription getLiveApplyList(final BaseListener listener){
        Observable<BaseResponse<ResLiveApplyList>> observable = ApiManager.getInstance().getLiveApplyList();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLiveApplyList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLiveApplyList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 申请直播
     * @param areaid                    区域id
     * @param buildingid                楼盘id
     * @param startlivetime             直播开始时间
     * @param title                     直播标题
     * @param description               直播内容描述
     * @param listener                  成功回调监听
     */
    public Subscription applyLive(int areaid,String areaName, int buildingid,String buildingName, String startlivetime, String title, String description, final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().applyLive(areaid,areaName,buildingid,buildingName,startlivetime,title,description);
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
     * 根据直播id 获取直播详细信息
     * @param liveId                            直播id
     *
     */
    public Subscription getLiveDetails(long liveId,final BaseListener<ResGetLiveDetails> listener){
        Observable<BaseResponse<ResGetLiveDetails>> observable = ApiManager.getInstance().getLiveDetails(liveId);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetLiveDetails>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetLiveDetails> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 请求直播推流地址
     * @param groupId           群组id
     * @param title             直播标题
     * @param nickName          主播昵称
     * @param avatar            主播头像
     * @param frontCover        封面
     * @param location          位置信息
     * @param listener          监听
     * @param liveOrientation   直播方向
     */
    public Subscription requestLVBAddr(String groupId, String title, String nickName, String avatar, String frontCover, String location,
                                       int liveOrientation,final BaseListener<ResGetLVBAddr> listener){
        Observable<BaseResponse<ResGetLVBAddr>> observable = ApiManager.getInstance().getLVBAddress(groupId,title,nickName,avatar,frontCover,location,liveOrientation);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetLVBAddr>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetLVBAddr> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 更改主播在线状态
     * @param status                状态 在线/离线
     * @param listener              监听
     */
    public Subscription changeLiveStatus(long applyId,long liveId,String status,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().changeLiveStatus(applyId,liveId,status);
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
     * 拉取直播列表
     * @param pageno                    分页号
     * @param listener                  响应监听
     */
    public Subscription fetchLiveList(LiveFilter filter, int pageno, int pagesize, final BaseListener listener){
        Observable<BaseResponse<ResFetchLiveList>> observable = ApiManager.getInstance().fetchLiveList(filter,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResFetchLiveList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResFetchLiveList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 通知服务器有群成员进入
     * @param liveid                 主播id
     * @param groupid                    群组id
     * @param nickname                   观众昵称
     * @param headpic                    观众头像
     */
    public Subscription enterGroup(long liveid,String groupid,String nickname,String headpic,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().enterGroup(liveid,groupid,nickname,headpic);
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
     * 通知服务器有群成员退出
     *
     * @param liveid                        直播id
     * @param groupid                       群组id
     */
    public Subscription quitGroup(long liveid,String groupid,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().quitGroup(liveid,groupid);
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
     * 拉取群成员列表
     *
     * @param liveid                        主播id
     * @param groupid                       群组id
     * @param pageno                        页码
     */
    public Subscription fetchGroupmemberlist(long liveid,String groupid,int pageno,final BaseListener listener){
        Observable<BaseResponse<ResFetchGroupMemberList>> observable = ApiManager.getInstance().fetchGroupmemberlist(liveid,groupid,pageno,20);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResFetchGroupMemberList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResFetchGroupMemberList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 修改点赞计数器
     *
     * @param liveid                    主播id
     * @return
     */
    public Subscription addLikecount(long liveid,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().addLikecount(liveid);
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
     * 关注/取消关注
     * @param objectId          对象id
     * @param flag              1 关注    0 取消关注
     */
    public Subscription attention(String objectId,int flag,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().attention(objectId,0,flag);
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
     *  获取最新直播预告
     * @param listener          响应监听
     */
    public Subscription getLatestLiveForecast(final BaseListener<ResLatestLiveForecast> listener){
        Observable<BaseResponse<ResLatestLiveForecast>> observable = ApiManager.getInstance().getLatestLiveForecast();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLatestLiveForecast>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLatestLiveForecast> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 获取最新直播预告
     * @param pageno                页码
     * @param pagesize              每页大小
     */
    public Subscription getAllLiveForecast(int userid,int pageno, int pagesize,final BaseListener<ResAllLiveForecast> listener){
        Observable<BaseResponse<ResAllLiveForecast>> observable = ApiManager.getInstance().getAllLiveForecast(userid,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResAllLiveForecast>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResAllLiveForecast> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }


    /**
     * 预约、取消预约观看直播
     * @param applyid
     * @param flag
     */
    public Subscription reserve(long applyid, int flag,final BaseListener listener){
        Observable<BaseResponse> observable = ApiManager.getInstance().reserve(applyid,flag);
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



}
