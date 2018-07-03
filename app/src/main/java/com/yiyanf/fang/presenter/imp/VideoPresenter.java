package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.api.model.ReqPublishVOD;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.VideoModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2018/03/06.
 */

public class VideoPresenter extends BasePresenter implements BaseListener {

    private VideoModel mModel;
    private IView mView;

    public VideoPresenter(IView mView) {
        this.mModel = new VideoModel(this);
        this.mView = mView;
    }

    public void getRecommendVideos(int sortBy,int pNo,int pSize){
        addSubscription(mModel.getRecommendVideos(sortBy, pNo, pSize));
    }

    public void getAttentionVideos(int sortBy,int pNo,int pSize){
        addSubscription(mModel.getAttentionVideos(sortBy, pNo, pSize));
    }

    public void getVideoClassification(){
        addSubscription(mModel.getVideoClassification());
    }

    public void publishVideo(ReqPublishVOD reqPublishVOD){
        addSubscription(mModel.publishVideo(reqPublishVOD));
    }

    public void finishPublishVOD(long videoId, String frontCover,String videoURL, String fileId,BaseListener onCompleteListener){
        addSubscription(mModel.finishPublishVOD(videoId,frontCover,videoURL,fileId,onCompleteListener));
    }

    public void getVideoDetails(long videoId){
        addSubscription(mModel.getVideoDetails(videoId));
    }

    public void attention(String followedUid,int flag,final BaseListener listener){
        addSubscription(mModel.attention(followedUid,flag,listener));
    }

    public void favorite(String videoId,int flag,final BaseListener listener){
        addSubscription(mModel.favorite(videoId,flag,listener));
    }

    public void makeShare(String shareUrl,String videoId,int channel,final BaseListener sharelistener){
        addSubscription(mModel.makeShare(shareUrl,videoId,channel,sharelistener));
    }

    public void deleteVideo(long videoId,final BaseListener sharelistener){
        addSubscription(mModel.deleteVideo(videoId,sharelistener));
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
