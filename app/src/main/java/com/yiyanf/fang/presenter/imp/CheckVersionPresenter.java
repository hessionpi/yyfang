package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.api.model.ResCheckVersion;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.CheckVersionModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2018/2/1.
 */

public class CheckVersionPresenter extends BasePresenter implements BaseListener<ResCheckVersion>{

    private CheckVersionModel mModel;
    private IView<ResCheckVersion> mView;

    public CheckVersionPresenter(IView<ResCheckVersion> iView) {
        this.mView = iView;
        this.mModel = new CheckVersionModel(this);
    }

    public void checkVersion(String packageName, int versionCode) {
        addSubscription(mModel.checkVersion(packageName,versionCode));
    }

    @Override
    public void onSuccess(BaseResponse<ResCheckVersion> data, int flag) {
        mView.fillData(data,flag);
    }

    @Override
    public void onFailed(Throwable e, int flag) {
        mView.showFailedView(flag);
    }
}
