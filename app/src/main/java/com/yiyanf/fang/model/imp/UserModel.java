package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResGetAttentionList;
import com.yiyanf.fang.api.model.ResGetCertificationData;
import com.yiyanf.fang.api.model.ResGetFansList;
import com.yiyanf.fang.api.model.ResGetIsCertificationUser;
import com.yiyanf.fang.api.model.ResGetLivePlay;
import com.yiyanf.fang.api.model.ResGetLoginUserinfo;
import com.yiyanf.fang.api.model.ResGetMyVideos;
import com.yiyanf.fang.api.model.ResGetPublishVideos;
import com.yiyanf.fang.api.model.ResGetUserSign;
import com.yiyanf.fang.api.model.ResGetVideoPublisherInfo;
import com.yiyanf.fang.api.model.ResLogin;
import com.yiyanf.fang.api.model.ResMineLiveReservation;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户相关处理模型  包括诸恶、登录、获取用户信息等
 *
 * Created by Hition on 2017/9/30.
 */

public class UserModel {

    private BaseListener listener;

    public UserModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 发送短信验证码
     * @param mobile                手机号
     * @return Subscription
     */
    public Subscription sendCode(String mobile){
        Observable<BaseResponse> observable = ApiManager.getInstance().sendCode(mobile);
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
     * 新用户注册
     * @param mobile                        手机号
     * @param password                      密码
     * @param sendcode                      短信验证码
     * @return Subscription
     */
    public Subscription register(String mobile,String password,String sendcode){
        Observable<BaseResponse<ResLogin>> observable = ApiManager.getInstance().register(mobile,password,sendcode);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.USER_REGISTER);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLogin> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.USER_REGISTER);
                    }
                });
    }

    /**
     * 用户登录
     * @param mobile                    用户手机号
     * @param password                  密码
     * @return Subscription
     */
    public Subscription login(String mobile,String password){
        Observable<BaseResponse<ResLogin>> observable = ApiManager.getInstance().login(mobile,password);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.UESR_LOGIN);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLogin> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.UESR_LOGIN);
                    }
                });
    }
    /**
     * 忘记密码
     * @param mobile                    用户手机号
     * @param password                  密码
     * @param sendcode                  验证码
     * @return Subscription
     */
    public Subscription findPassword(String mobile,String password,String sendcode){
        Observable<BaseResponse<ResLogin>> observable = ApiManager.getInstance().findPassword(mobile,password,sendcode);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.USER_FIND_PASSWORD);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLogin> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.USER_FIND_PASSWORD);
                    }
                });
    }
    /**
     * 完善资料
     * @param headpic  头像地址
     * @param nickname 昵称
     * @param sex      性别
     * @return Subscription
     */
    public Subscription updateUserinfo(String headpic, String nickname, int sex,String ecompany,String signature){
        Observable<BaseResponse<ResLogin>> observable = ApiManager.getInstance().updateUserinfo(headpic,nickname,sex,ecompany,signature);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.USER_PERFECT_DATA);
                    }

                    @Override
                    public void onNext(BaseResponse<ResLogin> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.USER_PERFECT_DATA);
                    }
                });
    }

    /**
     * 实名认证
     * @return Subscription
     */
    public Subscription iDCertification(String idcard, String name){
        Observable<BaseResponse> observable = ApiManager.getInstance().iDCertification(idcard,name);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 实名认证资料
     * @return Subscription
     */
    public Subscription agentCertification(String headpic, String agentcompany, ArrayList<Integer> buildings , String certificateurl , String visitcardurl){
        Observable<BaseResponse> observable = ApiManager.getInstance().agentCertification(headpic, agentcompany,  buildings ,  certificateurl , visitcardurl);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.AUTH_DATA);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.AUTH_DATA);
                    }
                });
    }

    /**
     * 是否是经纪人
     * @return Subscription
     */
    public Subscription getIsCertificationUser(int userid){
        Observable<BaseResponse<ResGetIsCertificationUser>> observable = ApiManager.getInstance().getIsCertificationUser(userid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetIsCertificationUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 获取登录用户基本资料
     *
     * @return Subscription
     */
    public Subscription getLoginUserinfo(){
        Observable<BaseResponse<ResGetLoginUserinfo>> observable = ApiManager.getInstance().getLoginUserinfo();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetLoginUserinfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.ATTENTION_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetLoginUserinfo> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.ATTENTION_LIST);
                    }
                });
    }

    /**
     * 修改密码
     * @param old_pwd       旧密码
     * @param new_pwd       新密码
     */
    public Subscription changePassword(String old_pwd, String new_pwd){
        Observable<BaseResponse> observable = ApiManager.getInstance().changePassword(old_pwd,new_pwd);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.USER_CHANGE_PASSWORD);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.USER_CHANGE_PASSWORD);
                    }
                });
    }



    /**
     * 获取游客签名，用户登录im云通讯
     * @param visitorid          游客id
     */
    public Subscription getUserSign(String visitorid){
        Observable<BaseResponse<ResGetUserSign>> observable = ApiManager.getInstance().getUserSign(visitorid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetUserSign>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetUserSign> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 关注列表
     */
    public Subscription getAttentionList(int regionflag, int buildingflag, int userflag){
        Observable<BaseResponse<ResGetAttentionList>> observable = ApiManager.getInstance().getAttentionList(regionflag,  buildingflag,  userflag);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetAttentionList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.ATTENTION_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetAttentionList> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.ATTENTION_LIST);
                    }
                });
    }

    /**
     * 粉丝列表
     */
    public Subscription getFansList(int pageno, int pagesize){
        Observable<BaseResponse<ResGetFansList>> observable = ApiManager.getInstance().getFansList(pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetFansList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.FANS_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetFansList> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.FANS_LIST);
                    }
                });
    }
    /**
     * 直播列表
     */
    public Subscription getLivePlay(int pageno, int pagesize){
        Observable<BaseResponse<ResGetLivePlay>> observable = ApiManager.getInstance().getLivePlay(pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetLivePlay>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetLivePlay> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 预约列表
     */
    public Subscription mineLiveReservation(int pageno, int pagesize){
        Observable<BaseResponse<ResMineLiveReservation>> observable = ApiManager.getInstance().mineLiveReservation(pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResMineLiveReservation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResMineLiveReservation> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 获取认证人资料
     */
    public Subscription getCertificationData(){
        Observable<BaseResponse<ResGetCertificationData>> observable = ApiManager.getInstance().getCertificationData();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetCertificationData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetCertificationData> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }
    /**
     * 获取视频发布者信息
     */
    public Subscription getVideoPublisherInfo(String publisherid){
        Observable<BaseResponse<ResGetVideoPublisherInfo>> observable = ApiManager.getInstance().getVideoPublisherInfo(publisherid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetVideoPublisherInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.VIDEO_PUBLISHER_INFO);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetVideoPublisherInfo> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.VIDEO_PUBLISHER_INFO);
                    }
                });
    }
    /**
     * 获取视频发布者视频
     */
    public Subscription getPublishVideos(String publisherid,int pageno, int pagesize){
        Observable<BaseResponse<ResGetPublishVideos>> observable = ApiManager.getInstance().getPublishVideos(publisherid,pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetPublishVideos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.PUBLISHER_VIDEOS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetPublishVideos> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.PUBLISHER_VIDEOS);
                    }
                });
    }
    //是否公开手机号
    public Subscription exposureMobile(int visible){
        Observable<BaseResponse> observable = ApiManager.getInstance().exposureMobile(visible);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.DEFAULT);
                    }
                });
    }

    /**
     * 获取我的视频
     * @param pageno
     * @param pagesize
     * @return
     */
    public Subscription  getMyVideos (int pageno, int pagesize){
        Observable<BaseResponse<ResGetMyVideos>> observable = ApiManager.getInstance().getMyVideos(pageno,pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetMyVideos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t,FangConstants.MY_VIDEOS);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetMyVideos> baseResponse) {
                        listener.onSuccess(baseResponse,FangConstants.MY_VIDEOS);
                    }
                });
    }
}
