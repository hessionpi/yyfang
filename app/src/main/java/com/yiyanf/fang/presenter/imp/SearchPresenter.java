package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.SearchModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SearchPresenter extends BasePresenter implements BaseListener {

    private SearchModel mModel;
    private IView mView;

    public SearchPresenter(IView iView) {
        this.mView = iView;
        this.mModel = new SearchModel(this);
    }
    /**
     * 热门搜索
     */
    public void getHotSearch() {
        addSubscription(mModel.getHotSearch());
    }
    /**
     * 搜索结果
     */
    public void getSearchResult(String condition, int pageno, int pagesize) {
        addSubscription(mModel.getSearchResult(condition,  pageno, pagesize));
    }


    @Override
    public void onSuccess(BaseResponse data, int flag) {
        mView.fillData(data, flag);
    }

    @Override
    public void onFailed(Throwable e, int flag) {
        mView.showFailedView(flag);
    }
}
