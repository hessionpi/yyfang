package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResHomePage;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hition on 2018/1/4.
 */

public class HomeModel {

    private BaseListener listener;

    public HomeModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 获取首页大集合数据
     *
     * @return
     */
    public Subscription getHomePage(){
        Observable<BaseResponse<ResHomePage>> observable = ApiManager.getInstance().getHomePage();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResHomePage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.DEFAULT);
                    }

                    @Override
                    public void onNext(BaseResponse<ResHomePage> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.DEFAULT);
                    }
                });
    }


}
