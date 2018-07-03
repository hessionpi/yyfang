package com.yiyanf.fang.presenter.imp;

import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.imp.CommentModel;
import com.yiyanf.fang.view.IView;

/**
 * Created by Administrator on 2017/11/29.
 */

public class CommentPresenter extends BasePresenter implements BaseListener {
    private CommentModel mModel;
    private IView mView;

    public CommentPresenter(IView mView) {
        this.mModel = new CommentModel(this);
        this.mView = mView;
    }
    public void discuss(int objecttype,String videoId,String discussContent){
        addSubscription(mModel.discuss(objecttype,videoId,discussContent));
    }

    public void reply(String nickname,long discussid,long replyid,String replyContent){
        addSubscription(mModel.reply(nickname,discussid,replyid,replyContent));
    }

    public void getDiscussList( int objecttype,String videoId,int pageNo,int pageSize){
        addSubscription(mModel.getDiscussList(objecttype,videoId,pageNo,pageSize));
    }

    public void getReplyList(long discussid, int pageNo, int pageSize){
        addSubscription(mModel.getReplyList(discussid,pageNo,pageSize));
    }

    public void deleteDiscuss(long discussid){
        addSubscription(mModel.deleteDiscuss(discussid));
    }

    public void deleteReply(long replyid){
        addSubscription(mModel.deleteReply(replyid));
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
