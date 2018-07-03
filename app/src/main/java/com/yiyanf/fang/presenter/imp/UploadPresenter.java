package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.UploadModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2017/11/6.
 */

public class UploadPresenter extends BasePresenter implements BaseListener{

    private UploadModel mModel;
    private IView mView;

    public UploadPresenter(IView view) {
        this.mView = view;
        this.mModel = new UploadModel();
    }

    public void getCOSSign() {
        addSubscription(mModel.getCOSSign(this));
    }

    @Override
    public void onSuccess(BaseResponse data, int flag) {
        mView.fillData(data,flag);
    }

    @Override
    public void onFailed(Throwable e, int flag) {
        mView.showFailedView(flag);
    }
}
