package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResCheckVersion;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hition on 2018/2/1.
 */

public class CheckVersionModel {

    private BaseListener listener;

    public CheckVersionModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     * 版本检查更新
     * @param packageName                   应用包名
     * @param versionCode                   版本号
     */
    public Subscription checkVersion(String packageName, int versionCode){
        Observable<BaseResponse<ResCheckVersion>> observable = ApiManager.getInstance().checkVersion(packageName,versionCode);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResCheckVersion>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.CHECK_VERSION_UPDATE);
                    }

                    @Override
                    public void onNext(BaseResponse<ResCheckVersion> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.CHECK_VERSION_UPDATE);
                    }
                });
    }
}
