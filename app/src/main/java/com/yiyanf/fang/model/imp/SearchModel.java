package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResGetHotSearch;
import com.yiyanf.fang.api.model.ResGetSearchResult;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SearchModel {
    private BaseListener listener;
    public SearchModel(BaseListener listener) {
        this.listener = listener;
    }


    /**
     * 热门搜索
     */
    public Subscription getHotSearch(){
        Observable<BaseResponse<ResGetHotSearch>> observable = ApiManager.getInstance().getHotSearch();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.HOT_REGION);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.HOT_REGION);
                    }
                });
    }

    /**
     * 搜索结果
     */
    public Subscription getSearchResult(String condition, int pageno, int pagesize){
        Observable<BaseResponse<ResGetSearchResult>> observable = ApiManager.getInstance().getSearchResult(condition,  pageno, pagesize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.SEARCH_RESULT);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.SEARCH_RESULT);
                    }
                });
    }


}
