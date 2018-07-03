package com.yiyanf.fang.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tencent.TIMConversationType;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Discussinfo;
import com.yiyanf.fang.api.model.ResGetDiscussList;
import com.yiyanf.fang.api.model.ResGetVideoDetails;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.CommentPresenter;
import com.yiyanf.fang.presenter.imp.VideoPresenter;
import com.yiyanf.fang.ui.adapter.CommentAdapter;
import com.yiyanf.fang.ui.adapter.CommentDetailsAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.ExpandableTextView;
import com.yiyanf.fang.ui.widget.dialog.AllReplyDialog;
import com.yiyanf.fang.ui.widget.dialog.TipsWith2ButtonDialog;
import com.yiyanf.fang.ui.widget.dialog.WithoutWifiDialog;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.ui.widget.like.TCHeartLayout;
import com.yiyanf.fang.ui.widget.videoview.TCVideoView;
import com.yiyanf.fang.util.CustomToast;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.PixelUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 视频播放页面
 * <p>
 * Created by Hition on 2017/11/20.
 */

public class VideoPlayActivity extends TCBaseActivity implements View.OnClickListener, ITXLivePlayListener,
        IView, CommentDetailsAdapter.OnReplyListener{

    @Bind(R.id.pb_playing_without_time)
    ProgressBar mPlayingWithoutTime;
    @Bind(R.id.iv_back)
    ImageView mBack;
    @Bind(R.id.video_view)
    TCVideoView mVideoView;
    @Bind(R.id.iv_play_pause)
    ImageView mPausePlayView;
    @Bind(R.id.ib_call)
    ImageButton mCall;
    /*@Bind(R.id.iv_video_background)
    ImageView mBgImageView;*/
    @Bind(R.id.pb_play_loading)
    ProgressBar mPlayLoading;
    @Bind(R.id.tv_tips_failed)
    TextView mFailedTips;
    @Bind(R.id.tv_reload)
    TextView mReload;

    @Bind(R.id.iv_publisher_avatar)
    ImageView mHeadpic;
    @Bind(R.id.tv_publisher_name)
    TextView mNickname;
    @Bind(R.id.tv_attention)
    TextView mAttention;
    @Bind(R.id.tv_send_msg)
    TextView mSendMsgView;
    @Bind(R.id.tv_video_description)
    ExpandableTextView mVideoDescription;

    @Bind(R.id.fl_replay)
    LinearLayout flReplay;
    @Bind(R.id.tv_replay)
    TextView tvReplay;
    @Bind(R.id.tv_share_wx_circle)
    TextView tvShareWxCenter;
    @Bind(R.id.tv_share_wx_friend)
    TextView tvShareWxFriend;

    @Bind(R.id.heart_layout)
    TCHeartLayout mHeartLayout;
    @Bind(R.id.ll_video_not_exits)
    LinearLayout videoNotExits;
    @Bind(R.id.tv_write_comment)
    TextView mPublishComment;
    @Bind(R.id.tv_total_comment)
    TextView mTotalComment;
    @Bind(R.id.tv_favorite)
    TextView mFavorite;
    @Bind(R.id.tv_total_share)
    TextView mShare;

    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private CommentAdapter discussAdapter;
    private boolean mVideoPause = false;
    private boolean mPlaying = false;
    private boolean mPausing = false;
    private boolean clickPause = false;
    private TXLivePlayer mTXLivePlayer;
    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();
    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
    private long videoId;
    private int renderRotation;
    private String frontCover;

    private VideoPresenter presenter;
    private CommentPresenter commentPresenter;
    private int discussPageNo = 1;
    private int replyPageNo;

    private long discussId;
    private String publishDisUserid;
    private long replyId;
    private int deletePosition;
    private int comment_action_model = FangConstants.VOD_DISCUSS;

    private int secondProgress;
    private int progress;

    private String mPlayUrl;
    private String videoTitle;
    private String publisherId;
    private String publisherName;
    private String publisherAvatar;
    private int favoriteTotal;
    private int favoriteAction = 1;
    private int shareTotal;
    private boolean isAttention;
    private String visibleMobile;

    private TextView mTotalDiscussView;
    // 评论为空view
    private TextView mEmptyView;
    private RecyclerView mRvDiscuss;

    // 友盟社会化分享
    private ShareAction mShareAction = new ShareAction(this);
    private SHARE_MEDIA sharePlatform = SHARE_MEDIA.WEIXIN;

    private static final String SHARE_TITLE = "%s | 来自：%s";
    private static final String SHARE_SUMMARY = "壹眼房—房产界的“今日头条”，最具投资价值的新盘视频集中营！";

    private GestureDetector gd;
    private boolean videoExits = true;

    public static void startActivity(Context context, long videoid, String frontCover) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("video_id", videoid);
        intent.putExtra("video_cover_url", frontCover);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            Bundle transBundle = ActivityOptionsCompat.makeCustomAnimation(context,R.anim.anim_activity_enter,0).toBundle();
            context.startActivity(intent,transBundle);
        }else{
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        gd = new GestureDetector(this,new OnDoubleClickListener());
        presenter = new VideoPresenter(this);
        commentPresenter = new CommentPresenter(this);
        parseIntent();
        initView();
        requestVideoDetails();
    }

    private void parseIntent() {
        videoId = getIntent().getLongExtra("video_id", 0);
        frontCover = getIntent().getStringExtra("video_cover_url");
    }

    private void requestVideoDetails() {
        if(NetWorkUtil.isNetworkConnected(this)){
            if(NetWorkUtil.NETWORKTYPE_WIFI != NetWorkUtil.getNetWorkType(this)){
                WithoutWifiDialog withoutWifiDialog = new WithoutWifiDialog();
                withoutWifiDialog.setCancelable(true);
                if (withoutWifiDialog.isAdded()){
                    withoutWifiDialog.dismiss();
                }else{
                    withoutWifiDialog.show(getFragmentManager(), "");
                }
                withoutWifiDialog.setPlayContinueListener(new WithoutWifiDialog.OnPlayContinueWithoutWifi() {
                    @Override
                    public void onPlayContinue() {
                        presenter.getVideoDetails(videoId);
                    }
                });
                return ;
            }
            presenter.getVideoDetails(videoId);
        }else{
            mPlayLoading.setVisibility(View.GONE);
            mFailedTips.setVisibility(View.VISIBLE);
            mReload.setVisibility(View.VISIBLE);
            CustomToast.show(this,R.string.no_network);
        }
    }

    private void deleteVideo() {
        if(NetWorkUtil.isNetworkConnected(this)){
            presenter.deleteVideo(videoId,new OnDeleteSuccessListener());
        }else{
            mPlayLoading.setVisibility(View.GONE);
            ToastUtils.show(this,R.string.no_network);
        }
    }

    private void initView() {
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }

        /*ImageLoader.load(this,frontCover,mBgImageView);
        mVideoView.setOnClickListener(this);*/
        mPausePlayView.setOnClickListener(this);
        mReload.setOnClickListener(this);
        mCall.setOnClickListener(this);
        tvShareWxCenter.setOnClickListener(this);
        tvShareWxFriend.setOnClickListener(this);
        tvReplay.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mSendMsgView.setOnClickListener(this);
        mPublishComment.setOnClickListener(this);
        mTotalComment.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mFavorite.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mHeadpic.setOnClickListener(this);
        mNickname.setOnClickListener(this);
    }

    /**
     * 评论列表
     */
    private void getDiscussList(){
        if(NetWorkUtil.isNetworkConnected(this)){
            commentPresenter.getDiscussList(0,String.valueOf(videoId),discussPageNo,FangConstants.PAGE_SIZE_DEFAULT);
        }else{
            ToastUtils.show(this,R.string.no_network);
        }
    }

    /**
     * 发表评论
     */
    private void discuss(String content){
        String userId = UserInfoCenter.getInstance().getUserId();
        if(!TextUtils.isEmpty(userId)){
            commentPresenter.discuss(0,String.valueOf(videoId),content);
        }else{
            LoginActivity.startActivity(VideoPlayActivity.this);
        }

    }

    /**
     * 回复评论/回复
     * @param replyContent
     */
    private void reply(String replyContent){
        LoginModel userInfo = UserInfoCenter.getInstance().getLoginModel();
        if(null != userInfo){
            String nickname = userInfo.getNickname();
            commentPresenter.reply(nickname,discussId,replyId,replyContent);
        }else{
            LoginActivity.startActivity(VideoPlayActivity.this);
        }
    }

    /**
     * 删除自己回复的内容
     */
    private void delReply(long replyId){
        String userId = UserInfoCenter.getInstance().getUserId();
        if(!TextUtils.isEmpty(userId)){
            if(NetWorkUtil.isNetworkConnected(this)){
                commentPresenter.deleteReply(replyId);
            }else{
                ToastUtils.show(this,R.string.no_network);
            }
        }else{
            LoginActivity.startActivity(VideoPlayActivity.this);
        }
    }

    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            ToastUtils.show(this, "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!");
            return false;
        }

        if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
            if (mPlayUrl.contains(".flv")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (mPlayUrl.contains(".m3u8")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                ToastUtils.show(this, "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                return false;
            }
        } else {
            ToastUtils.show(this, "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
            return false;
        }
        return true;
    }

    private void startPlayVideo(){
        if (!checkPlayUrl()) {
            return;
        }

        mVideoView.clearLog();
//        mBgImageView.setVisibility(View.VISIBLE);
        mTXLivePlayer.setPlayerView(mVideoView);
        mTXLivePlayer.setRenderRotation(renderRotation);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

        /*if(videoRotation == FangConstants.VIDEO_LANDSCAPE){
            // 横屏录制的视频，在竖屏状态下模式为自适应
            mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        }else{
            // 竖屏录制的视频
            mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        }*/

        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.setConfig(mTXPlayConfig);

        int result = mTXLivePlayer.startPlay(mPlayUrl, mUrlPlayType);
        if (0 != result) {
//            Intent rstData = new Intent();
            if (-1 == result) {
                LogUtil.d(TAG, FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
//                rstData.putExtra(FangConstants.ACTIVITY_RESULT,FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            } else {
                LogUtil.d(TAG, FangConstants.ERROR_RTMP_PLAY_FAILED);
//                rstData.putExtra(FangConstants.ACTIVITY_RESULT,FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            }
            mVideoView.onPause();
            stopPlay(true);
            finish();
        } else {
            mPlaying = true;
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (null != mTXLivePlayer) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        mVideoView.setLogText(null, param, event);
        switch(event){
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                secondProgress = param.getInt(TXLiveConstants.NET_STATUS_PLAYABLE_DURATION);
                progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                mPlayingWithoutTime.setProgress(progress);
                mPlayingWithoutTime.setSecondaryProgress(secondProgress);
                mPlayingWithoutTime.setMax(duration);
                break;

            case TXLiveConstants.PLAY_WARNING_VIDEO_PLAY_LAG:// 视频卡顿
            case TXLiveConstants.PLAY_WARNING_RECONNECT:// 网络断连, 已启动自动重连
                LogUtil.e("hition==","视频卡顿和重连");
                mVideoView.onPause();
                mTXLivePlayer.pause();
//                mBadNetworkLoading.setVisibility(View.VISIBLE);
                break;

            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT: // 网络断连,且经多次重连亦不能恢复,更多重试请自行重启播放
                LogUtil.e("hition==","视频网络连接失败了……");
                mVideoView.setVisibility(View.GONE);
                showErrorAndQuit(FangConstants.ERROR_MSG_NET_DISCONNECTED);
                break;

            case TXLiveConstants.PLAY_EVT_PLAY_END:
                videoPlayEnd();
                break;

            /*case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                mBgImageView.setVisibility(View.GONE);
                break;*/
        }
    }

    private void videoPlayEnd(){
//        mBadNetworkLoading.setVisibility(View.GONE);
//        mBgImageView.setVisibility(View.GONE);
        stopPlay(false);
        mVideoPause = false;
        mPlayingWithoutTime.setProgress(0);
        // 视频播放完成显示
        flReplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        if(clickPause){
            mPausePlayView.setVisibility(View.GONE);
            clickPause = false;
        }

        if (!mVideoPause) {
            mTXLivePlayer.resume();
        }else{
            if (mPausing) {
                mPausing = false;
                startPlayVideo();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
        mTXLivePlayer.pause();
    }

    @Override
    protected void showErrorAndQuit(String errorMsg) {
        mVideoView.onPause();
        stopPlay(true);
        super.showErrorAndQuit(errorMsg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    /**
     * 动态的请求拨打电话权限
     */
    private void requestPermission(){
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},100);
                return;
            }else{
                callPhone();
            }
        }else{
            callPhone();
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + visibleMobile));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_reload:
                mPlayLoading.setVisibility(View.VISIBLE);
                requestVideoDetails();
                break;

            case R.id.iv_play_pause:
                mPausePlayView.setVisibility(View.GONE);
                // 重新播放视频
                mTXLivePlayer.resume();
                break;

            case R.id.ib_call:
                // 拨打电话逻辑，先验证拨打电话权限
                TipsWith2ButtonDialog mCallTipsDialog = new TipsWith2ButtonDialog();
                Bundle bundle = new Bundle();
                bundle.putString("tips_msg","确定要拨打"+visibleMobile+"吗？");
                bundle.putString("negative_txt",getString(R.string.cancel));
                bundle.putString("positive_txt",getString(R.string.ok));
                mCallTipsDialog.setArguments(bundle);
                mCallTipsDialog.setCancelable(true);
                if (mCallTipsDialog.isAdded()){
                    mCallTipsDialog.dismiss();
                }else{
                    mCallTipsDialog.show(getFragmentManager(), "");
                }
                mCallTipsDialog.setDialogListener(new TipsWith2ButtonDialog.TipsDialogListener() {
                    @Override
                    public void onNegative() {
                        mCallTipsDialog.dismiss();
                    }

                    @Override
                    public void onPositive() {
                        requestPermission();
                        mCallTipsDialog.dismiss();
                    }
                });
                break;

            case R.id.tv_share_wx_circle:
                sharePlatform = SHARE_MEDIA.WEIXIN_CIRCLE;
                startShare();
                break;

            case R.id.tv_share_wx_friend:
                sharePlatform = SHARE_MEDIA.WEIXIN;
                startShare();
                break;

            case R.id.iv_publisher_avatar:
            case R.id.tv_publisher_name:
                if(!TextUtils.isEmpty(publisherId)){
                    if (publisherId .equals( UserInfoCenter.getInstance().getUserId())){
                        PersonalCenterActivity.startActivityForResult(VideoPlayActivity.this, publisherId,1);
                    } else {
                        PersonalCenterActivity.startActivityForResult(VideoPlayActivity.this, publisherId,0);
                    }
                }
                break;

            case R.id.tv_replay:
                flReplay.setVisibility(View.GONE);
                startPlayVideo();
                break;

            case R.id.tv_favorite:
                // 收藏或取消收藏
                if(TextUtils.isEmpty(UserInfoCenter.getInstance().getUserId())){
                    LoginActivity.startActivity(this);
                }else{
                    favoriteOrCancel();
                }
                break;

            case R.id.tv_attention:
                // 关注发布人
                attentionPublisher();
                break;

            case R.id.tv_send_msg:
                if(TextUtils.isEmpty(UserInfoCenter.getInstance().getUserId())){
                    LoginActivity.startActivity(this);
                    return ;
                }

                //  发私信
                if(!TextUtils.isEmpty(publisherId)){
                    ChatActivity.navToChat(this,publisherId, TIMConversationType.C2C);
                }
                break;

            case R.id.tv_total_share:
                if(!videoExits){
                    ToastUtils.show(this,"视频已被删除");
                    return ;
                }
                showShareboard();
                break;

            case R.id.tv_total_comment:
                // 显示全部评论
                if(!videoExits){
                    ToastUtils.show(this,"视频已被删除");
                    return ;
                }
                showDiscussAll();
                discussPageNo = 1;
                getDiscussList();
                break;

            case R.id.tv_write_comment:
                if(!videoExits){
                    ToastUtils.show(this,"视频已被删除");
                    return ;
                }
                // 发表评论
                comment_action_model = FangConstants.VOD_DISCUSS;
                showPublishPop();
                break;
        }
    }

    private PopupWindow showDiscussAll() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_video_discuss_all,null);
        PopupWindow popupwindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        mTotalDiscussView = ButterKnife.findById(contentView,R.id.tv_discuss_total);
        ImageView mCloseView = ButterKnife.findById(contentView,R.id.iv_reply_close);
        mRvDiscuss = ButterKnife.findById(contentView,R.id.rv_discuss);
        LinearLayoutManager discussLayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRvDiscuss.setLayoutManager(discussLayoutmanager);
        discussAdapter = new CommentAdapter(this);
        mRvDiscuss.setAdapter(discussAdapter);
        discussAdapter.setMore(R.layout.view_recyclerview_more, new OnDiscussLoadMoreListener());
        discussAdapter.setError(R.layout.view_recyclerview_error);

        mEmptyView = ButterKnife.findById(contentView,R.id.tv_empty_view);
        EditText mDiscussInput = ButterKnife.findById(contentView,R.id.et_input_comment);
        TextView mTvPublish = ButterKnife.findById(contentView,R.id.tv_publish);

        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow.dismiss();
            }
        });

        discussAdapter.setCommentClickListener(new OnVideoCommentClickListener());
        discussAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 回复评论者发的内容,自己不能回复自己
                Discussinfo disInfo = discussAdapter.getItem(position);
                publishDisUserid = disInfo.getUserid();
                if(publishDisUserid.equals(UserInfoCenter.getInstance().getUserId())){
                    return ;
                }

                // 针对评论内容进行回复
                comment_action_model = FangConstants.VOD_DISCUSS_REPLY;
                if(FangConstants.VOD_DISCUSS_REPLY == comment_action_model){
                    mDiscussInput.setHint("回复@"+disInfo.getNickname()+":");
                }else{
                    mDiscussInput.setHint(R.string.hint_write_comment);
                }

                // 升起软键盘
                mDiscussInput.requestFocus();
                showSoftKeyboard(mDiscussInput);
                discussId = disInfo.getDiscussid();
                replyId = 0;
            }
        });

        mDiscussInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    mTvPublish.setClickable(false);
                    mTvPublish.setTextColor(ContextCompat.getColor(VideoPlayActivity.this,R.color.cl_b2b2b2));
                    mTvPublish.setBackgroundResource(R.drawable.ic_discuss_send_unable);
                }else{
                    mTvPublish.setClickable(true);
                    mTvPublish.setTextColor(ContextCompat.getColor(VideoPlayActivity.this,R.color.white));
                    mTvPublish.setBackgroundResource(R.drawable.ic_discuss_send);
                }
            }
        });
        mTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discussContent = mDiscussInput.getText().toString();
                if(!TextUtils.isEmpty(discussContent)){
                    // 发布评论
                    if(NetWorkUtil.isNetworkConnected(VideoPlayActivity.this)){
                        if(FangConstants.VOD_DISCUSS_REPLY == comment_action_model){
                            reply(discussContent);
                        }else{
                            discuss(discussContent);
                        }
                        popupwindow.dismiss();
                    }else{
                        ToastUtils.show(VideoPlayActivity.this,R.string.no_network);
                    }
                }
            }
        });

        popupwindow.setFocusable(true);
        popupwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupwindow.setOutsideTouchable(true);//点击pop外部是否取消
        popupwindow.setBackgroundDrawable(new ColorDrawable());
        popupwindow.showAtLocation(mPublishComment,Gravity.BOTTOM,0,0);
        return popupwindow;
    }

    private void showSoftKeyboard(View editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    /**
     * 显示发表评论的 Popupwindow
     */
    private PopupWindow showPublishPop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_write_comment,null);
        PopupWindow popupwindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText mInput = ButterKnife.findById(contentView,R.id.et_input_comment);
        TextView mPublish = ButterKnife.findById(contentView,R.id.tv_publish);
        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetWorkUtil.isNetworkConnected(VideoPlayActivity.this)){
                    ToastUtils.show(VideoPlayActivity.this,R.string.no_network);
                    return ;
                }

                String inputText = mInput.getText().toString().trim();
                if(NetWorkUtil.isNetworkConnected(VideoPlayActivity.this)){
                    discuss(inputText);
                    popupwindow.dismiss();
                }else{
                    ToastUtils.show(VideoPlayActivity.this,R.string.no_network);
                }
            }
        });
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mPublish.setClickable(true);
                    mPublish.setTextColor(ContextCompat.getColor(VideoPlayActivity.this,R.color.white));
                    mPublish.setBackgroundResource(R.drawable.ic_discuss_send);
                } else {
                    mPublish.setClickable(false);
                    mPublish.setTextColor(ContextCompat.getColor(VideoPlayActivity.this,R.color.cl_b2b2b2));
                    mPublish.setBackgroundResource(R.drawable.ic_discuss_send_unable);
                }
            }
        });

        popupwindow.setFocusable(true);
        popupwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupwindow.setOutsideTouchable(true);//点击pop外部是否取消
        popupwindow.setBackgroundDrawable(new ColorDrawable());
        popupwindow.showAtLocation(mPublishComment,Gravity.BOTTOM,0,0);
        return popupwindow;
    }

    /**
     * 分享面板
     */
    private void showShareboard() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_share);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView mWXCenter = ButterKnife.findById(dialog,R.id.tv_shareboard_wx_center);
        TextView mWXFriend = ButterKnife.findById(dialog,R.id.tv_shareboard_wx_friend);
        TextView mDelete = ButterKnife.findById(dialog,R.id.tv_shareboard_delete);
        TextView mShareCancel = ButterKnife.findById(dialog,R.id.tv_share_withothers_cancel);
        if(!TextUtils.isEmpty(publisherId) && publisherId.equals(UserInfoCenter.getInstance().getUserId())){
            mDelete.setVisibility(View.VISIBLE);
        }

        mWXFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 微信好友分享
                sharePlatform = SHARE_MEDIA.WEIXIN;
                startShare();
                dialog.dismiss();
            }
        });
        mWXCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 微信朋友圈分享
                sharePlatform = SHARE_MEDIA.WEIXIN_CIRCLE;
                startShare();
                dialog.dismiss();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除自己上传的视频
                TipsWith2ButtonDialog mDeleteTipsDialog = new TipsWith2ButtonDialog();
                Bundle bundle = new Bundle();
                bundle.putString("tips_msg",getString(R.string.tips_delete_video_has_publish));
                bundle.putString("negative_txt",getString(R.string.cancel));
                bundle.putString("positive_txt",getString(R.string.ok));
                mDeleteTipsDialog.setArguments(bundle);
                mDeleteTipsDialog.setCancelable(true);
                if (mDeleteTipsDialog.isAdded()){
                    mDeleteTipsDialog.dismiss();
                }else{
                    mDeleteTipsDialog.show(getFragmentManager(), "delete_video");
                }
                mDeleteTipsDialog.setDialogListener(new TipsWith2ButtonDialog.TipsDialogListener() {
                    @Override
                    public void onNegative() {
                        mDeleteTipsDialog.dismiss();
                    }

                    @Override
                    public void onPositive() {
                        mPlayLoading.setVisibility(View.VISIBLE);
                        deleteVideo();
                        mDeleteTipsDialog.dismiss();
                    }
                });
                dialog.dismiss();
            }
        });
        mShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width =  PixelUtil.getScreenWidth(this);
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    /**
     *
     * 分享到微信朋友圈
     */
    private void startShare(){
        UMVideo umVideo = new UMVideo(FangConstants.VEDIO_WEB+videoId);
        umVideo.setTitle(String.format(SHARE_TITLE,videoTitle,publisherName));
        UMImage thumb = new UMImage(this, publisherAvatar);
        umVideo.setThumb(thumb);
        umVideo.setDescription(SHARE_SUMMARY);

        mShareAction.setPlatform(sharePlatform)
                .withMedia(umVideo)
                .setCallback(umShareListener)
                .share();
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(0 == data.getReturnValue()){
            switch (flag){
                case FangConstants.VOD_VIDEO_DETAILS:
                    mPlayLoading.setVisibility(View.GONE);
                    if(null != data && 0 == data.getReturnValue()){
                        mFailedTips.setVisibility(View.GONE);
                        mReload.setVisibility(View.GONE);
                        ResGetVideoDetails details = (ResGetVideoDetails) data.getReturnData();
                        publisherId = details.getPublisherid();
                        publisherName = details.getNickname();
                        publisherAvatar = details.getThumbnail();
                        mPlayUrl = details.getPlayurl();
                        videoTitle = details.getTitle();
                        favoriteTotal = details.getFavoritecount();
                        shareTotal = details.getSharecount();
                        isAttention = details.getIsattention();
                        visibleMobile = details.getPublishermobile();

                        mVideoDescription.setText(details.getVideodesc());
                        ImageLoader.loadTransformImage(this,details.getThumbnail(),mHeadpic,0);
                        mNickname.setText(details.getNickname());
                        if(!TextUtils.isEmpty(publisherId)){
                            if(!publisherId.trim().equals(UserInfoCenter.getInstance().getUserId())){
                                mShare.setText(String.valueOf(shareTotal));
                                mShare.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_share_comment,0,0,0);
                                if(!TextUtils.isEmpty(visibleMobile)){
                                    mCall.setVisibility(View.VISIBLE);
                                }
                                if(!isAttention){
                                    mAttention.setVisibility(View.VISIBLE);
                                }
                                mSendMsgView.setVisibility(View.VISIBLE);
                            }else{
                                // 是自己发的视频，则会出现更多，里面有删除操作
                                mShare.setText(null);
                                mShare.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play_more,0,0,0);
                            }
                        }
                        mTotalComment.setText(String.valueOf(details.getDiscusscount()));
                        mFavorite.setText(String.valueOf(favoriteTotal));
                        if(details.getIsfavorite()){
                            favoriteAction = 0;
                            mFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite,0,0,0);
                        }else{
                            favoriteAction = 1;
                            mFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_not,0,0,0);
                        }

                        if(TextUtils.isEmpty(mPlayUrl)){
                            videoExits = false;
                            videoNotExits.setVisibility(View.VISIBLE);
                            return ;
                        }
                        // 判断视频是宽屏还是竖屏，调整模仿模式
                        int width = details.getVideowidth();
                        int height = details.getVideoheight();
//                    renderMode = height > width ? TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN:TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
                        renderRotation = width > height ? TXLiveConstants.RENDER_ROTATION_LANDSCAPE:TXLiveConstants.RENDER_ROTATION_PORTRAIT;
                        startPlayVideo();
                    }else{
                        // 显示加载失败
                        mFailedTips.setVisibility(View.VISIBLE);
                        mReload.setVisibility(View.VISIBLE);
                    }

                    break;

                case FangConstants.VOD_DISCUSS_LIST:
                    ResGetDiscussList resData = (ResGetDiscussList) data.getReturnData();
                    if(null != resData){
                        int totalPage = resData.getTotalpage();
                        long totalDiscuss = resData.getTotalcount();
                        if(0 < totalDiscuss){
                            mTotalDiscussView.setText("("+totalDiscuss+")");
                        }

                        if(0 == totalPage){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRvDiscuss.setVisibility(View.GONE);
                            return;
                        }

                        List<Discussinfo> discussinfoList = resData.getDiscusslist();
                        if(null!=discussinfoList && !discussinfoList.isEmpty()){
                            mEmptyView.setVisibility(View.GONE);
                            mRvDiscuss.setVisibility(View.VISIBLE);
                            discussAdapter.addAll(discussinfoList);
                        }else{
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRvDiscuss.setVisibility(View.GONE);
                        }

                        if(discussPageNo == totalPage){
                            // 停止上拉加载
                            discussAdapter.stopMore();
                        }
                    }
                    break;

                case FangConstants.VOD_DISCUSS_REPLY_LIST:
                    /*ResGetReplyList replyData = (ResGetReplyList) data.getReturnData();
                    if(null != replyData){
                        int totalPage = replyData.getTotalpage();
                        if(replyPageNo <= totalPage){
                            List<Recentreplyinfo> replyList = replyData.getReplylist();
                            if(null != replyList && !replyList.isEmpty()){
                                if(1 == replyPageNo && mReplyAdapter.getCount() > 0){
                                    mReplyAdapter.clear();
                                }
                                mReplyAdapter.addAll(replyList);
                            }
                        }else{
                            mReplyAdapter.stopMore();
                        }

                    }*/
                    break;

                case FangConstants.VOD_DISCUSS:
                    ToastUtils.show(this,"评论成功");
                    int beforeCount = Integer.parseInt(mTotalComment.getText().toString());
                    beforeCount++;
                    mTotalComment.setText(String.valueOf(beforeCount));
                    break;

                case FangConstants.VOD_DISCUSS_REPLY:
                    ToastUtils.show(this,"回复成功");
                    // 发表回复成功后记得刷新评论列表
                    /*if(null != discussAdapter){
                        discussAdapter.clear();
                    }
                    discussPageNo = 1;
                    getDiscussList();

                    // 发布一条回复后更新全部回复内容
                    if(replyId > 0){
                        LogUtil.e("hition==","刷新所有回复列表……");
                        replyPageNo = 1;
                        getReplyList();
                    }*/

                    break;

                case FangConstants.VOD_DISCUSS_REPLY_DELETE:
                    // 删除回复成功，刷新所有回复列表和评论列表
//                    mReplyAdapter.remove(deletePosition);
                    break;
            }
        }else{
            LogUtil.e("hition===",data.getReturnMsg()+":"+data.getReturnValue());
        }

    }

    @Override
    public void showFailedView(int flag) {
        switch (flag){
            case FangConstants.VOD_VIDEO_DETAILS:
                mPlayLoading.setVisibility(View.GONE);
                mFailedTips.setVisibility(View.VISIBLE);
                mReload.setVisibility(View.VISIBLE);
                break;

                default:

                    break;
        }
    }

    @Override
    public void publishReply(long replyId) {
        // 针对回复内容再回复
        comment_action_model = FangConstants.VOD_DISCUSS_REPLY;
        /*mWriteComment.setVisibility(View.VISIBLE);
        mInput.requestFocus();*/
        this.replyId = replyId;
        showPublishPop();
    }

    @Override
    public void deleteReply(long replyId,int position) {
        // 删除回复
        this.deletePosition = position;
        delReply(replyId);
    }

    private class OnVideoCommentClickListener implements CommentAdapter.OnCommentClickListener{

        @Override
        public void onUserClick(String userid,Discussinfo data) {
            if (userid .equals( UserInfoCenter.getInstance().getUserId())){
                PersonalCenterActivity.startActivity(VideoPlayActivity.this, data.getUserid(),1);
            } else {
                PersonalCenterActivity.startActivity(VideoPlayActivity.this, data.getUserid(),0);
            }
        }

        @Override
        public void onMoreReply(Discussinfo discuss) {
            // 获取更多回复
            AllReplyDialog allReplyDialog = new AllReplyDialog();
            Bundle args = new Bundle();
            args.putString("talk_publisher_uid",discuss.getUserid());
            args.putString("talk_avatar",discuss.getThumbnail());
            args.putString("talk_publisher_uname",discuss.getNickname());
            args.putLong("discuss_id",discuss.getDiscussid());
            args.putString("talk_publish_time",discuss.getDiscusstime());
            args.putString("talk_content",discuss.getContent());
            allReplyDialog.setArguments(args);
            allReplyDialog.setCancelable(true);
            if (allReplyDialog.isAdded()){
                allReplyDialog.dismiss();
            }else{
                allReplyDialog.show(getFragmentManager(), "all_reply");
            }
        }
    }

    private class OnDiscussLoadMoreListener implements XMBaseAdapter.OnLoadMoreListener{

        @Override
        public void onLoadMore() {
            discussPageNo++ ;
            getDiscussList();
        }
    }

    private void attentionPublisher(){
        if(TextUtils.isEmpty(UserInfoCenter.getInstance().getUserId())){
            LoginActivity.startActivity(this);
        }else {
            if(TextUtils.isEmpty(publisherId)){
                return ;
            }
            if(NetWorkUtil.isNetworkConnected(this)){
                presenter.attention(publisherId, 1, new OnPublisherAttentionListener());
            }else{
                ToastUtils.show(this,R.string.no_network);
            }
        }
    }

    private class OnPublisherAttentionListener implements BaseListener{
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                ToastUtils.show(VideoPlayActivity.this,"已关注");
                mAttention.setVisibility(View.GONE);
                isAttention = true;
            }else{
                LogUtil.e("hition==",data.getReturnMsg()+":"+data.getReturnValue());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    private void favoriteOrCancel(){
        if(NetWorkUtil.isNetworkConnected(this)){
            presenter.favorite(String.valueOf(videoId),favoriteAction,new OnFavoriteListener());
        }else{
            ToastUtils.show(this,R.string.no_network);
        }
    }

    private class OnFavoriteListener implements BaseListener {

        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                if(1 == favoriteAction){
                    mFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite,0,0,0);
                    favoriteTotal++;
                    favoriteAction = 0;
                }else{
                    mFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_not,0,0,0);
                    favoriteTotal--;
                    favoriteAction = 1;
                }
                mFavorite.setText(String.valueOf(favoriteTotal));
            }else{
                if(!videoExits){
                    ToastUtils.show(VideoPlayActivity.this,"视频已被删除");
                    return ;
                }
                LogUtil.e("hition==","收藏或者取消失败啦 "+data.getReturnValue()+" "+data.getReturnMsg());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 友盟社会化分享
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            int attention = data.getIntExtra("attention",0);
            if(1 == attention){
                mAttention.setVisibility(View.GONE);
            }else{
                mAttention.setVisibility(View.VISIBLE);
            }
            isAttention = attention==1;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
            String shareUrl = FangConstants.VEDIO_WEB+videoId;
            int sharePf = platform==SHARE_MEDIA.WEIXIN_CIRCLE ? 0: 1;
            presenter.makeShare(shareUrl,String.valueOf(videoId),sharePf,new OnShareSuccessListener());
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform+"分享成功了");

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtil.e("share", platform + " 分享失败啦");
            if (t != null) {
                LogUtil.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LogUtil.e("share", platform + " 分享取消了");
        }
    };

    private class OnShareSuccessListener implements BaseListener{
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                shareTotal++;
                mShare.setText(String.valueOf(shareTotal));
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    private class OnDeleteSuccessListener implements BaseListener{
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            mPlayLoading.setVisibility(View.GONE);
            // 删除视频成功，通知关注列表，推荐列表，我的视频刷新
            if(0 == data.getReturnValue()){
                finish();
                EventBus.getDefault().postSticky("video_delete");
            }else{
                ToastUtils.show(VideoPlayActivity.this,"删除失败");
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    /**
     * 双击事件
     */
    private class OnDoubleClickListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mHeartLayout.addFavor();
            if(isAttention){
                return false;
            }
            attentionPublisher();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // 这里是处理单击事件的哦
            clickPause = true;
            mPausePlayView.setVisibility(View.VISIBLE);
            mTXLivePlayer.pause();
            return false;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.anim_activity_exit);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXLivePlayer.stopPlay(true); // true代表清除最后一帧画面
        mVideoView.onDestroy();

        if(null != presenter){
            presenter.onUnsubscribe();
        }

        if(null!=commentPresenter){
            commentPresenter.onUnsubscribe();
        }

        UMShareAPI.get(this).release();
    }

}
