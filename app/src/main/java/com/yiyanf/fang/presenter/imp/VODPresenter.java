package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.VideoFilter;
import com.yiyanf.fang.entity.VideoSort;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.VODModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Hition on 2017/10/31.
 */

public class VODPresenter extends BasePresenter implements BaseListener {

    private VODModel mModel;
    private IView mView;

    public VODPresenter(IView mView) {
        this.mModel = new VODModel(this);
        this.mView = mView;
    }

    public void getgetVODSign(long videoId){
        addSubscription(mModel.getVODSign(videoId));
    }

    public void fetchVodList(VideoFilter filter, VideoSort sort, int pageno, int pagesize){
        addSubscription(mModel.fetchVodList(filter,sort,pageno,pagesize));
    }

    public void featuredList(){
        addSubscription(mModel.featuredList());
    }

    public void relatedVideos(long videoid){
        addSubscription(mModel.relatedVideos(videoid));
    }

    public void attachedBuilding(long videoid){
        addSubscription(mModel.attachedBuilding(videoid));
    }

    public void attachedArea(long videoid){
        addSubscription(mModel.attachedArea(videoid));
    }

    public void publishVideoCategory(){
        addSubscription(mModel.publishVideoCategory());
    }

    public void startPlayVideo(long videoid){
        addSubscription(mModel.startPlayVideo(videoid));
    }

    public void exitPlayVideo(long videoid, long playid){
        addSubscription(mModel.exitPlayVideo(videoid,playid));
    }
    public void getMineFavoriteVod( int pageno, int pagesize){
        addSubscription(mModel.getMineFavoriteVod(pageno,pagesize));
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
