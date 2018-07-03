package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.UserModel;
import com.yiyanf.fang.view.IView;

import java.util.ArrayList;

/**
 * Created by Hition on 2017/9/30.
 */

public class UserPresenter extends BasePresenter implements BaseListener {

    private IView mView;
    private UserModel mModel;

    public UserPresenter(IView view) {
        this.mView = view;
        this.mModel = new UserModel(this);
    }

    public void sendCode(String mobile) {
        addSubscription(mModel.sendCode(mobile));
    }

    public void register(String mobile, String password, String sendcode) {
        addSubscription(mModel.register(mobile, password, sendcode));
    }

    public void login(String username, String password) {
        addSubscription(mModel.login(username, password));
    }

    public void findPassword(String mobile, String password, String sendcode) {
        addSubscription(mModel.findPassword(mobile, password, sendcode));
    }

    public void updateUserinfo(String headpic, String nickname, int sex,String ecompany,String signature) {
        addSubscription(mModel.updateUserinfo(headpic, nickname, sex,ecompany,signature));
    }

    public void iDCertification(String userid, String idcard, String name) {
        addSubscription(mModel.iDCertification(idcard, name));
    }

    public void agentCertification(String headpic, String agentcompany, ArrayList<Integer> buildings, String certificateurl, String visitcardurl) {
        addSubscription(mModel.agentCertification(headpic, agentcompany, buildings, certificateurl, visitcardurl));
    }

    public void changePassword(String old_pwd, String new_pwd) {
        addSubscription(mModel.changePassword(old_pwd, new_pwd));
    }

    public void getAttentionList(int regionflag, int buildingflag, int userflag) {
        addSubscription(mModel.getAttentionList(regionflag, buildingflag, userflag));
    }

    public void getFansList(int pageno, int pagesize) {
        addSubscription(mModel.getFansList(pageno, pagesize));
    }

    public void mineLiveReservation(int pageno, int pagesize) {
        addSubscription(mModel.mineLiveReservation(pageno, pagesize));
    }

    public void getLivePlay(int pageno, int pagesize) {
        addSubscription(mModel.getLivePlay(pageno, pagesize));
    }

    public void getCertificationData() {
        addSubscription(mModel.getCertificationData());
    }

    public void getLoginUserinfo() {
        addSubscription(mModel.getLoginUserinfo());
    }

    public void getIsCertificationUser(int userid) {
        addSubscription(mModel.getIsCertificationUser(userid));
    }
    public void getVideoPublisherInfo(String publisherid) {
        addSubscription(mModel.getVideoPublisherInfo(publisherid));
    }
    public void getPublishVideos(String publisherid,int pageno, int pagesize) {
        addSubscription(mModel.getPublishVideos(publisherid,pageno,pagesize));
    }
    public void exposureMobile(int visible) {
        addSubscription(mModel.exposureMobile(visible));
    }
    public void getMyVideos (int pageno, int pagesize) {
        addSubscription(mModel.getMyVideos(pageno,pagesize));
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
