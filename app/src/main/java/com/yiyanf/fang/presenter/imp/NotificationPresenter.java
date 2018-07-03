package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.model.ResGetNotifications;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.NotificationModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2018/1/19.
 */

public class NotificationPresenter extends BasePresenter implements BaseListener {

    private NotificationModel mModel;
    private IView mView;

    public NotificationPresenter(IView mView) {
        this.mModel = new NotificationModel(this);
        this.mView = mView;
    }

    public void getNotifications(int pageNo){
        addSubscription(mModel.getNotifications(pageNo, FangConstants.PAGE_SIZE_DEFAULT));
    }

    public void getInteractiveMsgs(int pageNo){
        addSubscription(mModel.getInteractiveMsgs(pageNo, FangConstants.PAGE_SIZE_DEFAULT));
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
