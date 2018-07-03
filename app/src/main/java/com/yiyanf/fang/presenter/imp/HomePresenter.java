package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.api.model.ResHomePage;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.HomeModel;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2018/1/4.
 */

public class HomePresenter extends BasePresenter implements BaseListener<ResHomePage> {

    private HomeModel mModel;
    private IView mView;

    public HomePresenter(IView mView) {
        this.mModel = new HomeModel(this);
        this.mView = mView;
    }

    public void getHomePage(){
        addSubscription(mModel.getHomePage());
    }

    @Override
    public void onSuccess(BaseResponse<ResHomePage> data, int flag) {
        mView.fillData(data,flag);
    }

    @Override
    public void onFailed(Throwable t, int flag) {
        LogUtil.e("hition===",t.getMessage());
    }
}
