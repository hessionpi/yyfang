package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResFetchVodList;
import com.yiyanf.fang.api.model.VODinfo;
import com.yiyanf.fang.db.DBManager;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.Video;
import com.yiyanf.fang.entity.VideoFilter;
import com.yiyanf.fang.entity.VideoSort;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.LivePresenter;
import com.yiyanf.fang.presenter.imp.VODPresenter;
import com.yiyanf.fang.service.UploadVODService;
import com.yiyanf.fang.ui.adapter.FailVideoAdapter;
import com.yiyanf.fang.ui.adapter.MyVideoAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.MyItemDecoration;
import com.yiyanf.fang.ui.widget.ScrollLinearLayoutManager;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MineVideoActivity extends BaseActivity implements IView, XMBaseAdapter.OnLoadMoreListener, View.OnClickListener {

    /*@Bind(R.id.titlebar)
    ToolbarView titlebar;
    @Bind(R.id.sv_upload)
    FrameLayout mUploadVideoView;*/

    @Bind(R.id.rv_fail_videos)
    RecyclerView rvFailVideo;
    @Bind(R.id.rv_mine_video)
    RecyclerView rvMineVideo;


    FailVideoAdapter failVideoAdapter;
    MyVideoAdapter mineVideoAdapter;

    @Bind(R.id.ll_video_empty)
    LinearLayout llVideoEmpty;

    private VODPresenter vodPresenter;
    private LivePresenter livePresenter;
    int pageno = 1;


    private DBManager manager;
    private int uploadingPosition = -1;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_video);
        ButterKnife.bind(this);
        vodPresenter = new VODPresenter(this);
        livePresenter = new LivePresenter();
        manager = new DBManager();
        initView();

        loadYiyanVOD();
        EventBus.getDefault().register(this);
    }

    /**
     * 调用接口获取普通壹眼视频
     */
    private void loadYiyanVOD() {
        if (NetWorkUtil.isNetworkConnected(this)) {
            VideoFilter filter = new VideoFilter();
            String id = UserInfoCenter.getInstance().getUserId();
            if (!TextUtils.isEmpty(id)) {
                filter.setUserid(new Integer(id));
                vodPresenter.fetchVodList(filter, VideoSort.SORT_DEFAULT, pageno, FangConstants.PAGE_SIZE_DEFAULT);
            }
        } else {
            showToast(R.string.no_network);
        }
    }


    private void initView() {
        /*titlebar.setRightBtnVisible(View.VISIBLE);
        titlebar.setOnRightClickListener(new ToolbarView.OnRightClickListener() {
            @Override
            public void onRightImgClick() {

            }

            @Override
            public void onRightBtnClick() {
                // 发布视频
                RecordVideoActivity.startActivity(MineVideoActivity.this);
            }
        });*/

        MyItemDecoration itemDecoration = new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL, 1, R.color.colorLine);
        rvFailVideo.addItemDecoration(itemDecoration);
        ScrollLinearLayoutManager failVideoLayoutManager = new ScrollLinearLayoutManager(this);
        failVideoLayoutManager.setScrollEnabled(false);
        rvFailVideo.setLayoutManager(failVideoLayoutManager);

        List<Video> videos = manager.getFailVideos();
        failVideoAdapter = new FailVideoAdapter(this, videos);
        rvFailVideo.setAdapter(failVideoAdapter);
        failVideoAdapter.setSlidingMenuClickListener(new OnSlideMenuClickListener());

        ScrollLinearLayoutManager yiyanLayoutManager = new ScrollLinearLayoutManager(this);
        yiyanLayoutManager.setScrollEnabled(false);
        rvMineVideo.setLayoutManager(yiyanLayoutManager);
        mineVideoAdapter = new MyVideoAdapter(this);
        rvMineVideo.setAdapter(mineVideoAdapter);
        mineVideoAdapter.setMore(R.layout.view_recyclerview_more, new XMBaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageno++;
                loadYiyanVOD();
            }
        });
        mineVideoAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        mineVideoAdapter.setError(R.layout.view_recyclerview_error);
        mineVideoAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 普通视频item点击事件
                VODinfo videoInfo = mineVideoAdapter.getItem(position);
                VideoPlayActivity.startActivity(MineVideoActivity.this,videoInfo.getVideoid(),videoInfo.getFrontcover());
            }
        });

    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        switch (flag) {
            case FangConstants.VOD_FETCHVODLIST:
                if (0 == data.getReturnValue()) {
                    Object returnData = data.getReturnData();
                    if (returnData != null) {
                        ResFetchVodList fetchVodData = (ResFetchVodList) returnData;
                        List<VODinfo> fetchVideos = fetchVodData.getVodlist();
                        if (null != fetchVideos && !fetchVideos.isEmpty()) {
                            //  LogUtil.e("zsj","+++++++++++++"+pageno);
                            int totalPage = fetchVodData.getTotalpage();
                            if (pageno <= totalPage) {
                                if(1 == pageno && mineVideoAdapter.getCount()>0){
                                    mineVideoAdapter.clear();
                                }
                                mineVideoAdapter.addAll(fetchVideos);
                                mineVideoAdapter.notifyDataSetChanged();
                            } else {
                                mineVideoAdapter.stopMore();
                            }
                        } else {

                            if (pageno == 1)
                                llVideoEmpty.setVisibility(View.VISIBLE);
                            mineVideoAdapter.stopMore();
                        }
                    } else {
                        if (pageno == 1)
                            llVideoEmpty.setVisibility(View.VISIBLE);
                        mineVideoAdapter.stopMore();
                    }
                }

        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_close:
                finish();
                break;
        }
    }

    private class OnSlideMenuClickListener implements FailVideoAdapter.IonSlidingViewClickListener {

        @Override
        public void onDeleteBtnCilck(long videoid, int position) {
            // 删除视频源文件,更新界面，同时删除持久化的数据
            failVideoAdapter.remove(position);
            manager.deleteFailVideo(videoid);
        }

        @Override
        public void onReuploadBtnCilck(Video video, int position) {
            // 重新上传视频内容，调用后台进程重新上传
            // 没上只能够上传一个视频，需要判断当已经有时间在上传时候
            uploadingPosition = position;
            Intent uploadIntent = new Intent(MineVideoActivity.this, UploadVODService.class);
            uploadIntent.putExtra("video_id", video.getVideoid());
            uploadIntent.putExtra("video_signature", video.getSignature());
            uploadIntent.putExtra("upload_again", true);
            uploadIntent.putExtra("video_path", video.getVideoPath());
            uploadIntent.putExtra("cover_path", video.getCoverPath());
            uploadIntent.putExtra("title", video.getTitle());
            /*uploadIntent.putExtra("video_duration", video.getDuration());
            uploadIntent.putExtra("filesize", video.getFileSize());*/
            startService(uploadIntent);
        }
    }

    //  事件没有被订阅时候调用
    /*@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventUploadSuccess(PublishVideoEvent event) {
        if (event.getUploadStatus() == PublishVideoEvent.Status.COMPLETE) {
            manager.deleteFailVideo(event.getVideoid());
            if (null != failVideoAdapter) {
                if (uploadingPosition >= 0) {
                    failVideoAdapter.remove(uploadingPosition);
                } else {
                    // 数据适配删除传完的视频数据，更新界面
                    int position = -1;
                    long completeVideoId = event.getVideoid();
                    List<Video> datas = failVideoAdapter.getAllData();
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getVideoid() == completeVideoId) {
                            position = i;
                            break;
                        }
                    }
                    if (position >= 0) {
                        failVideoAdapter.remove(position);
                    }
                }
            }
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventVideoDelete(String event){
        if(!TextUtils.isEmpty(event) && event.equals("video_delete")){
            pageno = 1;
            loadYiyanVOD();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != livePresenter) {
            livePresenter.onUnsubscribe();
        }

        if (null != vodPresenter) {
            vodPresenter.onUnsubscribe();
        }

        manager.close();
    }
}
