package com.yiyanf.fang.ui.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.umeng.socialize.ShareAction;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Liveinfo;
import com.yiyanf.fang.api.model.Meminfo;
import com.yiyanf.fang.api.model.ResFetchGroupMemberList;
import com.yiyanf.fang.api.model.ResGetLiveDetails;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.im.TCChatEntity;
import com.yiyanf.fang.im.TCChatMsgListAdapter;
import com.yiyanf.fang.im.TCChatRoomMgr;
import com.yiyanf.fang.im.TCSimpleUserInfo;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.LivePresenter;
import com.yiyanf.fang.ui.adapter.TCUserAvatarListAdapter;
import com.yiyanf.fang.ui.widget.TCInputTextMsgDialog;
import com.yiyanf.fang.ui.widget.TCSwipeAnimationController;
import com.yiyanf.fang.ui.widget.danmaku.TCDanmuMgr;
import com.yiyanf.fang.ui.widget.dialog.ShareDialog;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.ui.widget.like.TCHeartLayout;
import com.yiyanf.fang.util.CommonUtils;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.TCFrequeControl;
import com.yiyanf.fang.util.ToastUtils;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;
import master.flame.danmaku.controller.IDanmakuView;

/**
 * 直播播放页
 *
 * Created by Hition on 2017/10/23.
 */
public class TCLivePlayerActivity extends TCBaseActivity implements View.OnClickListener,ITXLivePlayListener,TCInputTextMsgDialog.OnTextSendListener,
        TXRecordCommon.ITXVideoRecordListener, TCChatRoomMgr.TCChatRoomListener {
    private static final String TAG = TCLivePlayerActivity.class.getSimpleName();

    @Bind(R.id.rl_play_root)
    FrameLayout mPlayRoot;
    @Bind(R.id.video_view_play)
    TXCloudVideoView mTXCloudVideoView;
    @Bind(R.id.pause_background)
    ImageView mPauseBgView;
    @Bind(R.id.iv_head_icon)
    ImageView mHeadIcon;
    @Bind(R.id.iv_record_ball)
    ImageView mRecordBall;
    @Bind(R.id.tv_broadcasting_time)
    TextView mtvPuserName;
    @Bind(R.id.tv_attention)
    TextView mAttention;
    @Bind(R.id.tv_building_info)
    TextView mBuildingInfo;
    @Bind(R.id.tv_pusher_id)
    TextView mPusherFangId;
    @Bind(R.id.ll_event)
    LinearLayout mEventLayout;

    @Bind(R.id.danmakuView)
    IDanmakuView mDanmuView;
    @Bind(R.id.record_layout)
    RelativeLayout rcord;
    @Bind(R.id.record)
    ImageView liveRecord;
    @Bind(R.id.record_progress)
    ProgressBar mRecordProgress;
    @Bind(R.id.tool_bar)
    LinearLayout tool;

    @Bind(R.id.tv_member_counts)
    TextView mMemberCount;
    @Bind(R.id.rv_user_avatar)
    RecyclerView mUserAvatarList;
    @Bind(R.id.heart_layout)
    TCHeartLayout mHeartLayout;
    @Bind(R.id.btn_message_input)
    Button mMessageInput;
    @Bind(R.id.btn_record)
    Button mRecord;
    @Bind(R.id.btn_share)
    Button mShare;
    @Bind(R.id.btn_like)
    Button mLike;
    @Bind(R.id.btn_close)
    Button mClose;

    @Bind(R.id.rl_controllLayer)
    RelativeLayout mControllLayer;
    @Bind(R.id.im_msg_listview)
    ListView mListViewMsg;

    //分享相关
    private ShareAction mShareAction = new ShareAction(this);
    private static final String SHARE_TITLE = "一起看%s-%s直播";
    private static final String SHARE_SUMMARY = "壹眼房—房产界的“今日头条”，最具投资价值的新盘视频集中营！";
    private static final String SHARE_URL = "http://www.baidu.com";

    private int mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;      //根据mIsLivePlay和url判断出的播放类型，更加具体

    private int mPageNum;
    private int totalPage;

    private long mLiveId;
    private boolean isFetchlist = true;
    private String mEventid;
    private int mAreaid;
    private String mArea;
    private int mBuildingid;
    private String mBuilding;
    private boolean isAgent;
    private String mPusherNickname;
    private String mPusherAvatar;
    protected String mPusherId;
    protected String mPlayUrl = "";
    private String mGroupId = "";
    private String mCoverUrl = "";
    private String mTitle = ""; //标题
    private boolean hasAttention;
    private int orientation = 1;
    protected String mNickname = "";
    protected String mHeadPic = "";

    private long mCurrentMemberCount = 0;
    private long mTotalMemberCount = 0;
    private long mHeartCount = 0;

    private TCSwipeAnimationController mTCSwipeAnimationController;
    private TCChatRoomMgr mTCChatRoomMgr;

    private LoginModel mUserinfo;
    //头像列表控件适配器
    private TCUserAvatarListAdapter mAvatarListAdapter;

    //点赞频率控制
    private TCFrequeControl mLikeFrequeControl;

    //弹幕
    private TCDanmuMgr mDanmuMgr;

    private TCInputTextMsgDialog mInputTextMsgDialog;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private TCChatMsgListAdapter mChatMsgListAdapter;

    private TXLivePlayer mTXLivePlayer;
    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();

    private PhoneStateListener mPhoneListener = null;

    protected boolean mPausing = false;
    private boolean mPlaying = false;

    //录制相关
    private boolean mRecording = false;
    private long mStartRecordTimeStamp = 0;

    protected Handler mHandler = new Handler();
    private LivePresenter presenter;

    public static void startActivity(Context context, String pusherId, long liveId,boolean isAgent,String playUrl, String groupId,
                                     String pusherName,String pusherAvatar,String coverPic,String roomTitle,int areaid,String area,
                                     int buildingid,String building,String eventid,boolean isattention,int orientation) {
        Intent intent = new Intent(context, TCLivePlayerActivity.class);
        intent.putExtra("pusher_id",pusherId);
        intent.putExtra("live_id",liveId);
        intent.putExtra("is_agent",isAgent);
        intent.putExtra("play_url",playUrl);
        intent.putExtra("group_id",groupId);
        intent.putExtra("pusher_name",pusherName);
        intent.putExtra("pusher_avatar",pusherAvatar);
        intent.putExtra("cover_pic",coverPic);
        intent.putExtra("room_title",roomTitle);
        intent.putExtra("area_id",areaid);
        intent.putExtra("area",area);
        intent.putExtra("building_id",buildingid);
        intent.putExtra("building",building);
        intent.putExtra("event_id",eventid);
        intent.putExtra("is_attention",isattention);
        intent.putExtra("orientation",orientation);
        context.startActivity(intent);
    }

    /**
     * 通过通知栏点击观看直播
     * @param liveId                直播id
     */
    public static void startActivity(Context context,long liveId,boolean openway) {
        Intent intent = new Intent(context, TCLivePlayerActivity.class);
        intent.putExtra("live_id",liveId);
        intent.putExtra("open_way",openway);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live_play);
        ButterKnife.bind(this);

        mUserinfo = UserInfoCenter.getInstance().getLoginModel();
        mNickname = mUserinfo.getNickname();
        mHeadPic = mUserinfo.getHeadpic();
        parseIntent();

        initView();
        presenter = new LivePresenter();
        if(!isFetchlist){
            // 调用获取视频详情接口获取视频详情后在调用加群操作
            presenter.getLiveDetails(mLiveId,new GetLiveDetailListener());
            return ;
        }

        // 如何是横屏的话就旋转屏幕
        if(orientation == TXLiveConstants.VIDEO_ANGLE_HOME_DOWN){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if(orientation == TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        joinRoom();
    }

    private void parseIntent() {
        Intent intent = getIntent();

        mLiveId = intent.getLongExtra("live_id",0);
        isFetchlist = intent.getBooleanExtra("open_way",true);
        orientation = getIntent().getIntExtra("orientation",TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        mPusherId = intent.getStringExtra("pusher_id");
        isAgent = intent.getBooleanExtra("is_agent",false);
        mPlayUrl = intent.getStringExtra("play_url");
        mGroupId = intent.getStringExtra("group_id");
        mPusherNickname = intent.getStringExtra("pusher_name");
        mPusherAvatar = intent.getStringExtra("pusher_avatar");
        mCoverUrl = intent.getStringExtra("cover_pic");
        mTitle = intent.getStringExtra("room_title");
        mAreaid = intent.getIntExtra("area_id",0);
        mArea = intent.getStringExtra("area");
        mBuildingid = intent.getIntExtra("building_id",0);
        mBuilding = intent.getStringExtra("building");
        mEventid = intent.getStringExtra("event_id");
        hasAttention = intent.getBooleanExtra("is_attention",false);
    }

    private void initLivePusherinfo(){
        mtvPuserName.setText(CommonUtils.getLimitString(mPusherNickname, 10));
        if(isAgent){
            mtvPuserName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_certification,0);
        }
        if(!hasAttention){
            mAttention.setVisibility(View.VISIBLE);
        }
        mPusherFangId.setText("壹眼房ID：" + mPusherId);
        mBuildingInfo.setText(mArea+"-"+mBuilding);
        if(!TextUtils.isEmpty(mEventid) && !"0".equals(mEventid)){
            mEventLayout.setVisibility(View.VISIBLE);
            mEventLayout.setOnClickListener(this);
        }

        showHeadIcon(mHeadIcon, mPusherAvatar);
    }

    private void initView() {
        mPlayRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });
        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControllLayer);

        mRecordBall.setVisibility(View.GONE);
        /*mtvPuserName.setText(CommonUtils.getLimitString(mPusherNickname, 10));
        if(isAgent){
            mtvPuserName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_certification,0);
        }
        if(!hasAttention){
            mAttention.setVisibility(View.VISIBLE);
        }
        mPusherFangId.setText("壹眼房ID：" + mPusherId);
        mBuildingInfo.setText(mArea+"-"+mBuilding);
        if(!TextUtils.isEmpty(mEventid) && !"0".equals(mEventid)){
            mEventLayout.setVisibility(View.VISIBLE);
            mEventLayout.setOnClickListener(this);
        }
        showHeadIcon(mHeadIcon, mPusherAvatar);*/

        initLivePusherinfo();

        mAvatarListAdapter = new TCUserAvatarListAdapter(this, mPusherId);
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mCurrentMemberCount++;
        mMemberCount.setText(String.format(Locale.CHINA,"%d",mCurrentMemberCount)+"人在观看直播");
        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mDanmuView.setVisibility(View.VISIBLE);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(mDanmuView);

        mAttention.setOnClickListener(this);
        mEventLayout.setOnClickListener(this);
        mMessageInput.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mLike.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }

        if (mPausing) {
            mPausing = false;
            startPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
        mPausing = true;
        stopPlay(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecord(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
            mTXCloudVideoView = null;
        }

        stopPlay(true);
        quitRoom();
        mTXLivePlayer = null;

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
        mPhoneListener = null;
    }

    private void showHeadIcon(ImageView view, String avatar) {
        ImageLoader.loadTransformImage(this, avatar,R.drawable.icon_man,R.drawable.icon_man,view,0);
    }

    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mPlayUrl.startsWith("rtmp://")) {
            mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        } else if ((mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://"))&& mPlayUrl.contains(".flv")) {
            mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        } else {
            Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected void startPlay() {
        if (!checkPlayUrl()) {
            return;
        }

        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.clearLog();
        }
        mPauseBgView.setVisibility(View.VISIBLE);
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);

        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.setConfig(mTXPlayConfig);

        int result;
        result = mTXLivePlayer.startPlay(mPlayUrl, mUrlPlayType);
        if (0 != result) {
            Intent rstData = new Intent();
            if (-1 == result) {
                Log.d(TAG, FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
                rstData.putExtra(FangConstants.ACTIVITY_RESULT,FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            } else {
                Log.d(TAG, FangConstants.ERROR_RTMP_PLAY_FAILED);
                rstData.putExtra(FangConstants.ACTIVITY_RESULT,FangConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            }

            mTXCloudVideoView.onPause();
            stopPlay(true);
//            setResult(TCLiveListFragment.START_LIVE_PLAY,rstData);
            finish();
        } else {
            mPlaying = true;
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }

    public void quitRoom() {
        if(null != mTCChatRoomMgr){
            mTCChatRoomMgr.quitGroup(mGroupId);
            mTCChatRoomMgr.removeMsgListener();
            presenter.quitGroup(mLiveId, mGroupId, new PlayerListener());
//        mTCPlayerMgr.quitGroup(mUserId, mPusherId, mGroupId, 0);
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        mTXCloudVideoView.setLogText(null,param,event);
    }

    @Override
    public void onNetStatus(Bundle status) {
        Log.d(TAG, "Current status: " + status.toString());
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(status,null,0);
        }

        /*if(status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
            if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        }
        else if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);*/
    }

    private void notifyMsg(final TCChatEntity entity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mArrayListChatEntity.size() > 1000)
                {
                    while (mArrayListChatEntity.size() > 900)
                    {
                        mArrayListChatEntity.remove(0);
                    }
                }

                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onJoinGroupCallback(int code, String msg) {
        if(code == 0){
            Log.d(TAG, "onJoin group success" + msg);
        } else if (FangConstants.NO_LOGIN_CACHE == code) {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(FangConstants.ERROR_MSG_NO_LOGIN_CACHE);
        } else {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(FangConstants.ERROR_MSG_JOIN_GROUP_FAILED + code);
        }
    }

    public void onSendMsgCallback(int errCode, TIMMessage timMessage) {
        //消息发送成功后回显
        if(errCode == 0) {
            TIMElemType elemType =  timMessage.getElement(0).getType();
            if(elemType == TIMElemType.Text) {
                Log.d(TAG, "onSendTextMsgsuccess:" + errCode);
            } else if(elemType == TIMElemType.Custom) {
                //custom消息存在消息回调,此处显示成功失败
                Log.d(TAG, "onSendCustomMsgsuccess:" + errCode);
            }
        } else {
            Log.d(TAG, "onSendMsgfail:" + errCode);
        }

    }

    @Override
    public void onReceiveMsg(int type, TCSimpleUserInfo userInfo, String content) {
        switch (type) {
            case FangConstants.IMCMD_ENTER_LIVE:
                handleMemberJoinMsg(userInfo);
                break;
            case FangConstants.IMCMD_EXIT_LIVE:
                handleMemberQuitMsg(userInfo);
                break;
            case FangConstants.IMCMD_PRAISE:
                handlePraiseMsg(userInfo);
                break;
            case FangConstants.IMCMD_PAILN_TEXT:
                handleTextMsg(userInfo, content);
                break;

            case FangConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, content);
                break;

            default:
                break;
        }
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {

        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

        mCurrentMemberCount++;
        mTotalMemberCount++;
        mMemberCount.setText(String.format(Locale.CHINA,"%d",mCurrentMemberCount)+"人在观看直播");

        //左下角显示用户加入消息
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals("")){
            entity.setContext(userInfo.userid + "加入直播");
        }else{
            entity.setContext(userInfo.nickname + "加入直播");
        }

        entity.setType(FangConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    public void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {

        if(mCurrentMemberCount > 0){
            mCurrentMemberCount--;
        }else{
            Log.d(TAG, "接受多次退出请求，目前人数为负数");
        }

        mMemberCount.setText(String.format(Locale.CHINA,"%d",mCurrentMemberCount)+"人在观看直播");
        mAvatarListAdapter.removeItem(userInfo.userid);

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "退出直播");
        else
            entity.setContext(userInfo.nickname + "退出直播");
        entity.setType(FangConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }


    @Override
    public void onGroupDelete() {
        showErrorAndQuit(FangConstants.ERROR_MSG_LIVE_STOPPED);
    }

    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();

        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "点了个赞");
        else
            entity.setContext(userInfo.nickname + "点了个赞");
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        mHeartCount++;

        entity.setType(FangConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    public void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(FangConstants.TEXT_TYPE);

        notifyMsg(entity);
        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.headpic, userInfo.nickname, text);
        }
    }

    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(FangConstants.TEXT_TYPE);

        notifyMsg(entity);
    }

    /**
     * 拉取用户头像列表
     */
    public void getGroupMembersList() {
        presenter.fetchGroupmemberlist(mLiveId, mGroupId, mPageNum, new BaseListener<ResFetchGroupMemberList>() {
            @Override
            public void onSuccess(BaseResponse<ResFetchGroupMemberList> data, int flag) {
                int retCode = data.getReturnValue();
                if (retCode == 0) {
                    ResFetchGroupMemberList groupMemberList = data.getReturnData();
                    mTotalMemberCount = groupMemberList.getTotalcount();
                    mCurrentMemberCount = groupMemberList.getTotalcount();
                    mMemberCount.setText(mTotalMemberCount+"人在观看直播");
                    for (Meminfo userInfo : groupMemberList.getMemberlist()){
                        TCSimpleUserInfo info = new TCSimpleUserInfo(userInfo.getUserid(),userInfo.getNickname(),userInfo.getHeadpic());
                        mAvatarListAdapter.addItem(info);
                    }
                } else {
                    TXLog.d(TAG, "getGroupMembersList failed");
                }
            }

            @Override
            public void onFailed(Throwable e, int flag) {

            }
        });
    }

    /**
     * TextInputDialog发送回调
     * @param msg 文本信息
     * @param danmuOpen 是否打开弹幕
     */
    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        //消息回显
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContext(msg);
        entity.setType(FangConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(mHeadPic, mNickname, msg);
            }
            mTCChatRoomMgr.sendDanmuMessage(msg);
        } else {
            mTCChatRoomMgr.sendTextMessage(msg);
        }
    }

   private static class TXPhoneStateListener extends PhoneStateListener {
       WeakReference<TXLivePlayer> mPlayer;

       TXPhoneStateListener(TXLivePlayer player) {
            mPlayer = new WeakReference<>(player);
       }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePlayer player = mPlayer.get();
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (player != null) player.setMute(true);
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (player != null) player.setMute(true);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (player != null) player.setMute(false);
                    break;
            }
        }
    }

    /**
     * 观众加入房间操作
     */
    public void joinRoom() {
        //初始化消息回调，当前存在：获取文本消息、用户加入/退出消息、群组解散消息、点赞消息、弹幕消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance();
        mTCChatRoomMgr.setMessageListener(this);
        mTCChatRoomMgr.joinGroup(mGroupId);

        // 观众进群
        presenter.enterGroup(mLiveId, mGroupId, mNickname, mHeadPic, new PlayerListener());
        startPlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vod_back:
                finish();
                break;

            case R.id.btn_close:
                /*Intent rstData = new Intent();
                long memberCount = mCurrentMemberCount - 1;
                rstData.putExtra(FangConstants.MEMBER_COUNT, memberCount>=0 ? memberCount:0);
                rstData.putExtra(FangConstants.HEART_COUNT, mHeartCount);
                rstData.putExtra(FangConstants.PUSHER_ID, mPusherId);
                setResult(0,rstData);*/
                finish();
                break;

            case R.id.btn_like:
                if (mHeartLayout != null) {
                    mHeartLayout.addFavor();
                }

                //点赞发送请求限制
                if (mLikeFrequeControl == null) {
                    mLikeFrequeControl = new TCFrequeControl();
                    mLikeFrequeControl.init(2, 1);
                }

                if (mLikeFrequeControl.canTrigger()) {
                    mHeartCount++;
                    //向后台发送点赞信息
                    presenter.addLikecount(mLiveId,new PlayerListener());
                    //向ChatRoom发送点赞消息
                    mTCChatRoomMgr.sendPraiseMessage();
                }
                break;

            case R.id.btn_message_input:
                showInputMsgDialog();
                break;

            case R.id.tv_attention:
                presenter.attentionOrCancel(mPusherId, 1, new BaseListener() {
                    @Override
                    public void onSuccess(BaseResponse data, int flag) {
                        if(0 == data.getReturnValue()){
                            mAttention.setVisibility(View.GONE);
                            // 发送一条关注的消息

                        }
                    }

                    @Override
                    public void onFailed(Throwable e, int flag) {

                    }
                });
                break;

            case R.id.ll_event:
                ToastUtils.show(this,"报名免费看房班车……");
                break;


            /*case R.id.play_btn: {
                if (mPlaying) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        if (mPlayIcon != null) {
                            mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                        }
                    } else {
                        mTXLivePlayer.pause();
                        if (mPlayIcon != null) {
                            mPlayIcon.setBackgroundResource(R.drawable.play_start);
                        }
                    }
                    mVideoPause = !mVideoPause;
                } else {
                    if (mPlayIcon != null) {
                        mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                    }
                    startPlay();
                }
            }
            break;*/

            case R.id.btn_share:
            case R.id.btn_vod_share:
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.showShareboard(this,mShareAction,ShareDialog.SHARE_VIDEO,SHARE_URL,String.format(SHARE_TITLE,mArea,mBuilding),SHARE_SUMMARY,mPusherAvatar);
                break;

            case R.id.btn_record:
                showRecordUI();
                break;

            case R.id.record:
                switchRecord();
                break;

            case R.id.retry_record:
                retryRecord();
                break;

            case R.id.close_record:
                closeRecord();
                break;

            default:
                break;
        }
    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = display.getWidth(); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    protected void showRecordUI() {
        tool.setVisibility(View.GONE);
        rcord.setVisibility(View.VISIBLE);
        mDanmuView.setVisibility(View.GONE);
        mUserAvatarList.setVisibility(View.GONE);
        mHeartLayout.setVisibility(View.GONE);
        mListViewMsg.setVisibility(View.GONE);

        mPlayRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return v.onTouchEvent(event);
            }
        });
    }

    public void hideRecordUI() {
        tool.setVisibility(View.VISIBLE);
        mRecordProgress.setProgress(0);

        rcord.setVisibility(View.GONE);
        mDanmuView.setVisibility(View.VISIBLE);
        mUserAvatarList.setVisibility(View.VISIBLE);
        mHeartLayout.setVisibility(View.VISIBLE);
        mListViewMsg.setVisibility(View.VISIBLE);

        mPlayRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mTCSwipeAnimationController != null) {
                    return mTCSwipeAnimationController.processEvent(event);
                } else {
                    return v.onTouchEvent(event);
                }
            }
        });
    }

    private void switchRecord() {
        if (mRecording) {
            stopRecord(true);
        } else {
            startRecord();
        }
    }

    private void retryRecord() {
        if (mRecording) {
            if (mTXLivePlayer != null) {
                mTXLivePlayer.setVideoRecordListener(null);

            }
            if (mTXLivePlayer != null) {
                mTXLivePlayer.stopRecord();
            }

            liveRecord.setBackgroundResource(R.drawable.ic_record_start);
            mRecording = false;

            if (mRecordProgress != null) {
                mRecordProgress.setProgress(0);
            }
        }
    }

    private void closeRecord() {
        if (mRecording && mTXLivePlayer != null) {
            mTXLivePlayer.stopRecord();
            mTXLivePlayer.setVideoRecordListener(null);
        }
        hideRecordUI();
        liveRecord.setBackgroundResource(R.drawable.ic_record_start);
        mRecording = false;
    }

    private void stopRecord(boolean showToast) {
        // 录制时间要大于5s
        if (System.currentTimeMillis() <= mStartRecordTimeStamp + 5*1000) {
            if (showToast) {
                showTooShortToast();
                return;
            } else {
                if (mTXLivePlayer != null) {
                    mTXLivePlayer.setVideoRecordListener(null);
                }
            }
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.stopRecord();
        }
        liveRecord.setBackgroundResource(R.drawable.ic_record_start);
        mRecording = false;
    }

    private void startRecord() {
        mTXLivePlayer.setVideoRecordListener(this);
        mTXLivePlayer.startRecord(TXRecordCommon.RECORD_TYPE_STREAM_SOURCE);
        liveRecord.setBackgroundResource(R.drawable.stop_record);
        mRecording = true;
        mStartRecordTimeStamp = System.currentTimeMillis();
    }

    private void showTooShortToast() {
        if (mRecordProgress != null) {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            int[] position = new int[2];
            mRecordProgress.getLocationOnScreen(position);
            Toast toast = Toast.makeText(this, "至少录到这里", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, position[0], position[1] - statusBarHeight - 110);
            toast.show();
        }
    }

    @Override
    public void onRecordEvent(int event, Bundle param) {

    }

    @Override
    public void onRecordProgress(long milliSecond) {
        if (mRecordProgress != null) {
            float progress = milliSecond / 60000.0f;
            mRecordProgress.setProgress((int) (progress * 100));
            if (milliSecond >= 60000.0f) {
                stopRecord(true);
            }
        }
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        /*Intent intent = new Intent(this, TCVideoPublisherActivity.class);
        intent.putExtra(FangConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PLAY);
        intent.putExtra(FangConstants.VIDEO_RECORD_RESULT, result.retCode);
        intent.putExtra(FangConstants.VIDEO_RECORD_DESCMSG, result.descMsg);
        intent.putExtra(FangConstants.VIDEO_RECORD_VIDEPATH, result.videoPath);
        intent.putExtra(FangConstants.VIDEO_RECORD_COVERPATH, result.coverPath);
        startActivity(intent);*/
        hideRecordUI();
    }

    /**
     * 进群 退群监听
     */
    private class PlayerListener implements BaseListener {

        @Override
        public void onSuccess(BaseResponse data, int flag) {
            int errorCode = data.getReturnValue();
            switch (errorCode){
                case 0:
                    if (null != mTXLivePlayer) {
                        getGroupMembersList();
                    }
                    break;

                case FangConstants.ERROR_GROUP_NOT_EXIT:
                    showErrorAndQuit(FangConstants.ERROR_MSG_GROUP_NOT_EXIT);
                    break;

                case FangConstants.ERROR_QALSDK_NOT_INIT:
                    ((FangApplication)getApplication()).initIMSDK();
                    joinRoom();
                    break;

                case 500 :
                    LogUtil.e("hition==","服务器异常："+data.getReturnMsg());
                    break;

                default:
                    showErrorAndQuit(FangConstants.ERROR_MSG_JOIN_GROUP_FAILED + errorCode);
                    ToastUtils.show(TCLivePlayerActivity.this,data.getReturnMsg());
                    break;
            }

        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }

    private class GetLiveDetailListener implements BaseListener<ResGetLiveDetails>{

        @Override
        public void onSuccess(BaseResponse<ResGetLiveDetails> data, int flag) {
            if(0 == data.getReturnValue()){
                ResGetLiveDetails liveDetails = data.getReturnData();
                Liveinfo liveInfomation = liveDetails.getLiveinfo();
                orientation =liveInfomation.getOrientation();
                mPusherId = liveInfomation.getUserid();
                isAgent = 1 == liveInfomation.getType();
                mPlayUrl = liveInfomation.getPlayurl();
                mGroupId = liveInfomation.getGroupid();
                mPusherNickname = liveInfomation.getNickname();
                mPusherAvatar = liveInfomation.getThumbnail();
                mCoverUrl = liveInfomation.getFrontcover();
                mTitle = liveInfomation.getTitle();
                mAreaid = liveInfomation.getAreaid();
                mArea = liveInfomation.getArea();
                mBuildingid = liveInfomation.getBuildingid();
                mBuilding = liveInfomation.getBuilding();
                mEventid = liveInfomation.getEventid();
                hasAttention = 1 == liveInfomation.getIsattention();

                // 如何是横屏的话就旋转屏幕
                if(orientation == TXLiveConstants.VIDEO_ANGLE_HOME_DOWN){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else if(orientation == TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                initLivePusherinfo();
                joinRoom();
            }else{
                LogUtil.w(TAG,data.getReturnMsg()+" "+data.getReturnValue());
            }
        }

        @Override
        public void onFailed(Throwable e, int flag) {

        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

