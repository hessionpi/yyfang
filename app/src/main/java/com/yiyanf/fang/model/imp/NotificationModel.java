package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResGetAreas;
import com.yiyanf.fang.api.model.ResGetInteractiveMsgs;
import com.yiyanf.fang.api.model.ResGetNotifications;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hition on 2018/1/19.
 */

public class NotificationModel {

    private BaseListener listener;

    public NotificationModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 获取所有通知消息
     * @param pageNo            分页号
     * @param pageSize          每页大小
     */
    public Subscription getNotifications(int pageNo, int pageSize){
        Observable<BaseResponse<ResGetNotifications>> observable = ApiManager.getInstance().getNotifications(pageNo,pageSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetNotifications>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.GET_NOTIFICATION);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetNotifications> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.GET_NOTIFICATION);
                    }
                });
    }


    /**
     * 获取所有互动消息
     *
     * @param pageNo            分页号
     * @param pageSize          每页大小
     */
    public Subscription getInteractiveMsgs(int pageNo, int pageSize){
        Observable<BaseResponse<ResGetInteractiveMsgs>> observable = ApiManager.getInstance().getInteractiveMsgs(pageNo,pageSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetInteractiveMsgs>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.GET_INTERACTIVE_MSG);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetInteractiveMsgs> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.GET_INTERACTIVE_MSG);
                    }
                });
    }



}
