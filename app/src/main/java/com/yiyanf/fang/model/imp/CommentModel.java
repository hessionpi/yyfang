package com.yiyanf.fang.model.imp;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.api.model.ResGetDiscussList;
import com.yiyanf.fang.api.model.ResGetReplyList;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.BaseListener;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/29.
 */

public class CommentModel {
    private BaseListener listener;
    public CommentModel(BaseListener listener) {
        this.listener = listener;
    }

    /**
     *  评论
     * @param videoId                  评论的对象的id
     * @param discussContent           评论内容
     */
    public Subscription discuss(int objecttype,String videoId, String discussContent){
        Observable<BaseResponse> observable = ApiManager.getInstance().discuss(objecttype,videoId,discussContent);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS);
                    }
                });
    }

    /**
     * 回复评论
     * @param nickname                      回复人昵称
     * @param discussid                     评论id
     * @param replyid                       回复id
     * @param replyContent                  回复内容
     */
    public Subscription reply(String nickname,long discussid,long replyid,String replyContent){
        Observable<BaseResponse> observable = ApiManager.getInstance().reply(nickname,discussid,replyid,replyContent);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS_REPLY);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS_REPLY);
                    }
                });
    }

    /**
     * 获取评论列表
     * @param pageNo                        页码
     * @param pageSize                      分页大小
     */
    public Subscription getDiscussList(int objecttype,String videoId,int pageNo,int pageSize){
        Observable<BaseResponse<ResGetDiscussList>> observable = ApiManager.getInstance().getDiscussList(objecttype,videoId,pageNo,pageSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetDiscussList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetDiscussList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS_LIST);
                    }
                });
    }


    /**
     * 获取回复列表
     * @param discussid                     评论内容id
     * @param pageNo                        页码
     * @param pageSize                      分页大小
     */
    public Subscription getReplyList(long discussid, int pageNo, int pageSize){
        Observable<BaseResponse<ResGetReplyList>> observable = ApiManager.getInstance().getReplyList(discussid,pageNo,pageSize);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<ResGetReplyList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS_REPLY_LIST);
                    }

                    @Override
                    public void onNext(BaseResponse<ResGetReplyList> baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS_REPLY_LIST);
                    }
                });
    }

    /**
     * 删除评论
     * @param discussid 评论id
     */
    public Subscription deleteDiscuss(long discussid){
        Observable<BaseResponse> observable = ApiManager.getInstance().deleteDiscuss(discussid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS_DELETE);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS_DELETE);
                    }
                });
    }

    /**
     * 删除回复
     * @param replyid  回复id
     */
    public Subscription deleteReply(long replyid){
        Observable<BaseResponse> observable = ApiManager.getInstance().deleteReply(replyid);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        listener.onFailed(t, FangConstants.VOD_DISCUSS_REPLY_DELETE);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        listener.onSuccess(baseResponse, FangConstants.VOD_DISCUSS_REPLY_DELETE);
                    }
                });
    }

}
