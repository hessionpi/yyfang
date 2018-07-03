package com.yiyanf.fang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResAttentionVideos;
import com.yiyanf.fang.api.model.SimpleVideo;
import com.yiyanf.fang.db.DBManager;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.SimpleVideoInfo;
import com.yiyanf.fang.entity.Video;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.service.UploadVODService;
import com.yiyanf.fang.ui.activity.PersonalCenterActivity;
import com.yiyanf.fang.ui.activity.VideoPlayActivity;
import com.yiyanf.fang.ui.adapter.RecommendVideoAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.SpacesItemDecoration;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * 关注视频
 *
 * Created by Hition on 2018/3/5.
 */

public class AttentionVideosFragment extends BaseFragment implements XMBaseAdapter.OnLoadMoreListener, IView {

    @Bind(R.id.refresh_view)
    SwipeRefreshLayout mRefreshView;
    @Bind(R.id.rv_attention)
    RecyclerView mAttentionVideoView;
    @Bind(R.id.tv_empty_view)
    TextView mEmptyView;
    @Bind(R.id.tv_network_bad)
    TextView mBadNetwork;

    private static final int SORT_BY_DEFAULT = 0;
    private AttentionRefreshListener refreshListener;

    private int pageno = 1;
    private VideoPresenter videoPresenter;
    private RecommendVideoAdapter adapter;
    private List<SimpleVideoInfo> allVideos = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPresenter = new VideoPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void processLogic() {
        mRefreshView.setColorSchemeResources(R.color.cl_7a7a7a,R.color.cl_00bceb);
        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAttentionVideoView.setLayoutManager(staggeredLayoutManager);
        mAttentionVideoView.setPadding(0,0,0,10);
        mAttentionVideoView.addItemDecoration(new SpacesItemDecoration(2,2,true));
        adapter = new RecommendVideoAdapter(activity);
        mAttentionVideoView.setAdapter(adapter);
        adapter.setMore(R.layout.view_recyclerview_more, this);
        adapter.setError(R.layout.view_recyclerview_error);

    }

    @Override
    protected int loadViewLayout() {
        return R.layout.fragment_attention_videos;
    }

    @Override
    protected void setListener() {
        refreshListener = new AttentionRefreshListener();
        mRefreshView.setOnRefreshListener(refreshListener);

        adapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 开始播放
                SimpleVideoInfo simpleVideo = adapter.getItem(position);
                if(simpleVideo.getStatus() == SimpleVideoInfo.Status.IDLE){
                    VideoPlayActivity.startActivity(activity,simpleVideo.getVideoid(),simpleVideo.getFrontcover());
                }
            }
        });

        adapter.setPublisherClickListener(new RecommendVideoAdapter.OnPublisherClickListener() {
            @Override
            public void onPublisherclick(String publisherId) {
                if(!TextUtils.isEmpty(publisherId)){
                    if(publisherId.equals(UserInfoCenter.getInstance().getUserId())){
                        PersonalCenterActivity.startActivity(activity,publisherId,1);
                    }else{
                        PersonalCenterActivity.startActivity(activity,publisherId,0);
                    }
                }
            }
        });

        adapter.setPublisherClickListener(new RecommendVideoAdapter.OnPublisherClickListener() {
            @Override
            public void onPublisherclick(String publisherId) {
                if(!TextUtils.isEmpty(publisherId)){
                    if(publisherId.equals(UserInfoCenter.getInstance().getUserId())){
                        PersonalCenterActivity.startActivity(activity,publisherId,1);
                    }else{
                        PersonalCenterActivity.startActivity(activity,publisherId,0);
                    }
                }
            }
        });

        adapter.setOnPublisherFailedListener(new RecommendVideoAdapter.OnPublisherFailedListener() {
            @Override
            public void onUploadAgain(SimpleVideoInfo videoInfo) {
                // 重新上传逻辑
                Intent uploadIntent = new Intent(activity, UploadVODService.class);
                uploadIntent.putExtra("video_id", videoInfo.getVideoid());
                uploadIntent.putExtra("video_signature", videoInfo.getSignature());
                uploadIntent.putExtra("upload_again", true);
                uploadIntent.putExtra("video_path", videoInfo.getVideoPath());
                uploadIntent.putExtra("cover_path", videoInfo.getFrontcover());
                uploadIntent.putExtra("title", videoInfo.getTitle());
                activity.startService(uploadIntent);
            }

            @Override
            public void onDeleteFail(int position,long videoid) {
                // 删除本地失败视频逻辑
                DBManager dbManager = new DBManager();
                dbManager.deleteFailVideo(videoid);
                dbManager.close();
                // 删除后刷新列表
                adapter.remove(position);
            }
        });
    }

    private class AttentionRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            allVideos.clear();
            getLocalFailedVideos();
            pageno = 1;
            requestAttentionVideos();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocalFailedVideos();
        // 请求关注视频
        requestAttentionVideos();
    }

    /**
     * 获取本地上传失败的视频
     */
    private void getLocalFailedVideos(){
        DBManager dbManager = new DBManager();
        List<Video> localFailedVideoList = dbManager.getFailVideos();
        dbManager.close();

        if(null == localFailedVideoList || localFailedVideoList.isEmpty()){
            return;
        }
        SimpleVideoInfo simpleVideo ;
        for(Video local : localFailedVideoList){
            simpleVideo = new SimpleVideoInfo();
            simpleVideo.setVideoid(local.getVideoid());
            simpleVideo.setSignature(local.getSignature());
            simpleVideo.setStatus(SimpleVideoInfo.Status.FAILED);
            simpleVideo.setFrontcover(local.getCoverPath());
            simpleVideo.setVideoPath(local.getVideoPath());
            simpleVideo.setCoverwidth(local.getCoverwidth());
            simpleVideo.setCoverheight(local.getCoverheight());
            simpleVideo.setTitle(local.getTitle());
            simpleVideo.setPublisherid(local.getPublisherid());
            simpleVideo.setPublisheravatar(local.getPublisheravatar());
            simpleVideo.setPublishername(local.getPublishername());
            allVideos.add(simpleVideo);
        }

    }

    /**
     * 请求我关注的视频
     */
    private void requestAttentionVideos() {
        if(NetWorkUtil.isNetworkConnected(activity)){
            mBadNetwork.setVisibility(View.GONE);
            videoPresenter.getAttentionVideos(SORT_BY_DEFAULT, pageno, FangConstants.PAGE_SIZE_DEFAULT);
        }else {
            showNetworkToast(R.string.no_network);
            if(adapter.getCount() == 0){
                mBadNetwork.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
            mRefreshView.setRefreshing(false);
            adapter.stopMore();
        }
    }

    @Override
    public void onLoadMore() {
        pageno++;
        requestAttentionVideos();
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }
        switch (flag){
            case FangConstants.VIDEOS_ATTENTION:
                if(0 == data.getReturnValue()){
                    ResAttentionVideos recommendVideos = (ResAttentionVideos) data.getReturnData();
                    if(null != recommendVideos){
                        int totalPage = recommendVideos.getTotalpage();
                        if(pageno > totalPage){
                            adapter.stopMore();
                            return;
                        }

                        List<SimpleVideo> simpleVideos = recommendVideos.getAttentionvideos();
                        if(null !=simpleVideos && !simpleVideos.isEmpty()){
                            mEmptyView.setVisibility(View.GONE);
                            mAttentionVideoView.setVisibility(View.VISIBLE);
                            if(1 == pageno && adapter.getCount()>0){
                                adapter.clear();
                            }

                            List<SimpleVideoInfo> videoInfoList = new ArrayList<>();
                            SimpleVideoInfo videoInfo ;
                            for(SimpleVideo video : simpleVideos){
                                videoInfo = new SimpleVideoInfo();
                                videoInfo.setVideoid(video.getVideoid());
                                videoInfo.setFrontcover(video.getFrontcover());
                                videoInfo.setCoverwidth(video.getCoverwidth());
                                videoInfo.setCoverheight(video.getCoverheight());
                                videoInfo.setTitle(video.getTitle());
                                videoInfo.setPublisherid(video.getPublisherid());
                                videoInfo.setPublisheravatar(video.getPublisheravatar());
                                videoInfo.setPublishername(video.getPublishername());
                                videoInfoList.add(videoInfo);
                            }
                            allVideos.addAll(videoInfoList);
                            adapter.addAll(allVideos);
                        }
                    }else{
                        adapter.stopMore();
                        if(1 == pageno ){
                            mEmptyView.setVisibility(View.VISIBLE);
                            if(adapter.getCount()>0){
                                adapter.clear();
                                mAttentionVideoView.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void showFailedView(int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }
    }

    @Subscribe
    public void onEventUpload(SimpleVideoInfo simpleVideoEvent){
        LogUtil.v("hition==","status value is: "+simpleVideoEvent.getStatus()+",上传进度："+simpleVideoEvent.getProgress()+"%");
        if(null != simpleVideoEvent){
            switch (simpleVideoEvent.getStatus()){
                case START:
                    mRefreshView.setEnabled(false);
                    adapter.add(0,simpleVideoEvent);
                    break;

                case UPLOADING:
                case UPLOADING_AGAIN:
                    mRefreshView.setEnabled(false);
                    adapter.notifyItemChanged(0,simpleVideoEvent);
                    break;

                case IDLE:
                    mRefreshView.setEnabled(true);
                    mRefreshView.setRefreshing(true);
                    refreshListener.onRefresh();
                    break;

                case FAILED:
                    mRefreshView.setEnabled(true);
                    showToast("上传失败了……");
                    adapter.notifyItemChanged(0,simpleVideoEvent);
                    break;

                default:

                    break;
            }
        }
    }

    @Subscribe
    public void onEventVideoDelete(String event){
        if(!TextUtils.isEmpty(event) && event.equals("video_delete")){
            /*pageno = 1;
            requestAttentionVideos();*/
            mRefreshView.setRefreshing(true);
            refreshListener.onRefresh();
        }
    }

    @Override
    protected void unsubscribe() {
        videoPresenter.onUnsubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setStatistical() {

    }


}
