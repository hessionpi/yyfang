package com.yiyanf.fang.ui.fragment;

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
import com.yiyanf.fang.api.model.ResRecommendVideos;
import com.yiyanf.fang.api.model.SimpleVideo;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.SimpleVideoInfo;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.ui.activity.PersonalCenterActivity;
import com.yiyanf.fang.ui.activity.VideoPlayActivity;
import com.yiyanf.fang.ui.adapter.RecommendVideoAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.SpacesItemDecoration;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * 推荐视频列表
 *
 * Created by Hition on 2018/3/5.
 */

public class RecommendVideosFragment extends BaseFragment implements IView ,XMBaseAdapter.OnLoadMoreListener{

    /*@Bind(R.id.refresh_view)
    UltimateRefreshView mRefreshView;*/
    @Bind(R.id.refresh_view)
    SwipeRefreshLayout mRefreshView;
    @Bind(R.id.rv_recommend)
    RecyclerView mRecommendVideoView;
    @Bind(R.id.tv_network_bad)
    TextView mBadNetwork;

    private static final int SORT_BY_DEFAULT = 0;

    private int pageno = 1;
    private VideoPresenter videoPresenter;
    private RecommendVideoAdapter adapter;
    private View moreView;

    private long startTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        videoPresenter = new VideoPresenter(this);
    }

    @Override
    protected int loadViewLayout() {
        return R.layout.fragment_recommend_videos;
    }

    @Override
    protected void processLogic() {
        /*FrameAnimationHeader mBaseHeaderAdapter = new FrameAnimationHeader(getContext());
        mRefreshView.setBaseHeaderAdapter(mBaseHeaderAdapter);
        mRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshView.headerRefreshing();
            }
        });*/

        mRefreshView.setColorSchemeResources(R.color.cl_7a7a7a,R.color.cl_main);

        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecommendVideoView.setLayoutManager(staggeredLayoutManager);
        mRecommendVideoView.setPadding(0,0,0,0);
        mRecommendVideoView.addItemDecoration(new SpacesItemDecoration(4,2,false));
        adapter = new RecommendVideoAdapter(activity);
        mRecommendVideoView.setAdapter(adapter);
        moreView = adapter.setMore(R.layout.view_recyclerview_more, this);
//        adapter.setNoMore(R.layout.view_recyclerview_nomore);
        adapter.setError(R.layout.view_recyclerview_error);
    }

    @Override
    protected void setListener() {
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageno = 1;
                requestRecommendVideos();
            }
        });

        adapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 开始播放
                SimpleVideoInfo simpleVideo = adapter.getItem(position);
                VideoPlayActivity.startActivity(activity,simpleVideo.getVideoid(),simpleVideo.getFrontcover());
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestRecommendVideos();
    }

    private void requestRecommendVideos() {
        if(NetWorkUtil.isNetworkConnected(activity)){
            moreView.setVisibility(View.VISIBLE);
            mBadNetwork.setVisibility(View.GONE);
            videoPresenter.getRecommendVideos(SORT_BY_DEFAULT, pageno, FangConstants.PAGE_SIZE_DEFAULT);
        }else {
            showNetworkToast(R.string.no_network);
            if(adapter.getCount() == 0){
                mBadNetwork.setVisibility(View.VISIBLE);
            }
            mRefreshView.setRefreshing(false);
            moreView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMore() {
        pageno++;
        requestRecommendVideos();
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }
        switch (flag){
            case FangConstants.VIDEOS_RECOMMEND:
                if(0 == data.getReturnValue()){
                    ResRecommendVideos recommendVideos = (ResRecommendVideos) data.getReturnData();
                    if(null != recommendVideos){
                        int totalPage = recommendVideos.getTotalpage();
                        if(pageno <= totalPage){
                            List<SimpleVideo> simpleVideos = recommendVideos.getRecommendvideos();
                            if(null !=simpleVideos && !simpleVideos.isEmpty()){
                                if(1 == pageno && adapter.getCount()>0){
                                    adapter.clear();
                                }
                                List<SimpleVideoInfo> videoInfoList = new ArrayList<>();
                                SimpleVideoInfo videoInfo;
                                for(SimpleVideo video : simpleVideos){
                                    videoInfo = new SimpleVideoInfo();
                                    videoInfo.setVideoid(video.getVideoid());
                                    videoInfo.setFrontcover(video.getFrontcover());
                                    videoInfo.setCoverwidth(video.getCoverwidth());
                                    videoInfo.setCoverheight(video.getCoverheight());
                                    videoInfo.setTitle(video.getTitle());
                                    videoInfo.setCategory(video.getCategory());
                                    videoInfo.setIsagent(video.getIsagent());
                                    videoInfo.setPublisherid(video.getPublisherid());
                                    videoInfo.setPublisheravatar(video.getPublisheravatar());
                                    videoInfo.setPublishername(video.getPublishername());
                                    videoInfoList.add(videoInfo);
                                }
                                adapter.addAll(videoInfoList);
                            }
                        }else{
                            adapter.stopMore();
                        }
                    }else{
                        adapter.stopMore();
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
    public void onEventVideoDelete(String event){
        if(!TextUtils.isEmpty(event) && event.equals("video_delete")){
            pageno = 1;
            requestRecommendVideos();
        }
    }

    @Override
    protected void unsubscribe() {
        EventBus.getDefault().unregister(this);
        videoPresenter.onUnsubscribe();
    }

    @Override
    public void setStatistical() {

    }


}
