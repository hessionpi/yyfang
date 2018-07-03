package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.api.model.ResGetLVBAddr;
import com.yiyanf.fang.api.model.ResGetLiveDetails;
import com.yiyanf.fang.api.model.ResSponsorLive;
import com.yiyanf.fang.entity.LiveFilter;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.LiveModel;


/**
 * Created by Hition on 2017/9/30.
 */

public class LivePresenter extends BasePresenter{

    private LiveModel mModel;

    public LivePresenter() {
        this.mModel = new LiveModel();
    }

    public void sponsorLive(BaseListener<ResSponsorLive> listener){
        addSubscription(mModel.sponsorLive(listener));
    }

    public void getLiveApplyList(BaseListener listener){
        addSubscription(mModel.getLiveApplyList(listener));
    }

    public void applyLive(int areaid,String areaName, int buildingid,String buildingName, String startlivetime, String title, String description, BaseListener listener){
        addSubscription(mModel.applyLive(areaid,areaName,buildingid,buildingName,startlivetime,title,description,listener));
    }

    public void getLiveDetails(long liveId,BaseListener<ResGetLiveDetails> listener){
        addSubscription(mModel.getLiveDetails(liveId,listener));
    }

    public void requestLVBAddr(String groupId, String title, String nickName, String avatar, String frontCover, String location,int liveOrientation,BaseListener<ResGetLVBAddr> listener){
        addSubscription(mModel.requestLVBAddr(groupId,title,nickName,avatar,frontCover,location,liveOrientation,listener));
    }

    public void changeLiveStatus(long applyId,long liveId,String status,BaseListener listener){
        addSubscription(mModel.changeLiveStatus(applyId,liveId,status,listener));
    }

    public void fetchLiveList(LiveFilter filter, int pageno, int pagesize, BaseListener listener){
        addSubscription(mModel.fetchLiveList(filter,pageno,pagesize,listener));
    }

    public void enterGroup(long liveid,String groupid,String nickname,String headpic, BaseListener listener){
        addSubscription(mModel.enterGroup(liveid,groupid,nickname,headpic,listener));
    }

    public void quitGroup(long liveid,String groupid,BaseListener listener){
        addSubscription(mModel.quitGroup(liveid,groupid,listener));
    }

    public void fetchGroupmemberlist(long liveid,String groupid,int pageno,BaseListener listener){
        addSubscription(mModel.fetchGroupmemberlist(liveid,groupid,pageno,listener));
    }

    public void addLikecount(long liveid,BaseListener listener){
        addSubscription(mModel.addLikecount(liveid,listener));
    }

    public void getLatestLiveForecast(BaseListener listener){
        addSubscription(mModel.getLatestLiveForecast(listener));
    }

    public void getAllLiveForecast(int userid,int pageno, int pagesize,BaseListener listener){
        addSubscription(mModel.getAllLiveForecast(userid,pageno,pagesize,listener));
    }

    public void reserve(long applyid, int flag,BaseListener listener){
        addSubscription(mModel.reserve(applyid,flag,listener));
    }






    public void attentionOrCancel(String objectId,int flag,BaseListener listener){
        addSubscription(mModel.attention(objectId,flag,listener));
    }
}
