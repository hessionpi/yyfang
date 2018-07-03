package com.yiyanf.fang.ui.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResGetLVBAddr;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.im.TCChatEntity;
import com.yiyanf.fang.im.TCChatMsgListAdapter;
import com.yiyanf.fang.im.TCChatRoomMgr;
import com.yiyanf.fang.im.TCSimpleUserInfo;
import com.yiyanf.fang.model.BaseListener;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.LivePresenter;
import com.yiyanf.fang.ui.adapter.TCUserAvatarListAdapter;
import com.yiyanf.fang.ui.fragment.LiveOrientationFragment;
import com.yiyanf.fang.ui.widget.TCInputTextMsgDialog;
import com.yiyanf.fang.ui.widget.TCSwipeAnimationController;
import com.yiyanf.fang.ui.widget.danmaku.TCDanmuMgr;
import com.yiyanf.fang.ui.widget.dialog.ShareDialog;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.ui.widget.like.TCHeartLayout;
import com.yiyanf.fang.util.BeautyFilterUtil;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * 主播推流页面
 *
 * Created by Hition on 2017/10/18.
 */

public class TCLivePublisherActivity extends TCBaseActivity implements TCChatRoomMgr.TCChatRoomListener, View.OnClickListener,
        ITXLivePushListener, TCInputTextMsgDialog.OnTextSendListener, BeautyDialogFragment.OnBeautyParamsChangeListener {

    private static final String TAG = TCLivePublisherActivity.class.getSimpleName();

    @Bind(R.id.netbusy_tv)
    TextView mNetBusyTips;
    @Bind(R.id.rl_publish_root)
    FrameLayout mPublishRoot;
    @Bind(R.id.video_view)
    TXCloudVideoView mTXCloudVideoView;
    @Bind(R.id.iv_head_icon)
    ImageView mHeadIcon;
    @Bind(R.id.tv_broadcasting_time)
    TextView mBroadcastTime;
    @Bind(R.id.iv_record_ball)
    ImageView mRecordBall;
    @Bind(R.id.tv_member_counts)
    TextView mMemberCount;
    @Bind(R.id.rv_user_avatar)
    RecyclerView mUserAvatarList;
    @Bind(R.id.heart_layout)
    TCHeartLayout mHeartLayout;
    /*@Bind(R.id.btn_message_input)
    Button mMessageInput;*/
    @Bind(R.id.flash_btn)
    Button mFlashView;
    @Bind(R.id.switch_cam)
    Button mSwitchCam;
    @Bind(R.id.beauty_btn)
    Button mBeautyBtn;
    @Bind(R.id.btn_share)
    Button mShare;
    @Bind(R.id.btn_close)
    Button mClose;

    @Bind(R.id.rl_controllLayer)
    RelativeLayout mControllLayer;
    @Bind(R.id.im_msg_listview)
    ListView mListViewMsg;

    private TCInputTextMsgDialog mInputTextMsgDialog;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private TCChatMsgListAdapter mChatMsgListAdapter;

    private long mSecond = 0;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;

    private long lTotalMemberCount = 0;
    private long lMemberCount = 0;
    private long lHeartCount = 0;

    private TCSwipeAnimationController mTCSwipeAnimationController;

    private TCChatRoomMgr mTCChatRoomMgr;
    public TXLivePusher mTXLivePusher;
    protected TXLivePushConfig mTXPushConfig = new TXLivePushConfig();

    protected Handler mHandler = new Handler();
    private Handler mNetBusyHandler;


    private PhoneStateListener mPhoneListener = null;

    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();
    //弹幕
    private TCDanmuMgr mDanmuMgr;

    //头像列表控件适配器
    private TCUserAvatarListAdapter mAvatarListAdapter;

    private boolean mFront = false;
    private boolean mFlashOn = false;
    protected boolean mPasuing = false;

    protected String mPushUrl;
    private String mTitle;
    private String mCoverPicUrl = "";
    private String mHeadPicUrl;
    private String mNickName;
    private String areaName;
    private String buildingName;
    private long applyId;
    private long liveId;
    private ObjectAnimator mObjAnim;

    private LivePresenter presenter;

    int liveOrientation;

    //分享相关
    private ShareAction mShareAction = new ShareAction(this);
    private static final String SHARE_TITLE = "一起看%s-%s直播";
    private static final String SHARE_SUMMARY = "壹眼房—房产界的“今日头条”，最具投资价值的新盘视频集中营！";
    private static final String SHARE_URL = "http://www.baidu.com";


    private SHARE_MEDIA mShare_meidia = SHARE_MEDIA.MORE;
    private String mShareUrl = FangConstants.SVR_LivePlayShare_URL;
    private boolean mSharedNotPublished = false; //分享之后还未开始推流


    public static void startActivity(Context context,long applyId,String title,String headpic,String nickname,String area,String building) {
        Intent intent = new Intent(context, TCLivePublisherActivity.class);
//        intent.putExtra("render_rotation",renderRotation);
        intent.putExtra("apply_id",applyId);
        intent.putExtra("title",title);
        intent.putExtra("headpic",headpic);
        intent.putExtra("nickname",nickname);
        intent.putExtra("area_name",area);
        intent.putExtra("building_name",building);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live_pusher);
        ButterKnife.bind(this);
        parseIntent();
        initView();

        // 打开摄像头预览画面
        mTXLivePusher = new TXLivePusher(this);
        // 手动对焦关闭，采用自动对焦模式
        mTXPushConfig.setTouchFocus(false);
        // 调整摄像头为默认后置，但是可以通过切换按钮进行切换
        mTXPushConfig.setFrontCamera(mFront);
        mTXLivePusher.setConfig(mTXPushConfig);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);

        presenter = new LivePresenter();
        mTCChatRoomMgr = TCChatRoomMgr.getInstance();
        mTCChatRoomMgr.setMessageListener(this);
    }

    private void parseIntent() {
        applyId = getIntent().getLongExtra("apply_id",0);
        mTitle = getIntent().getStringExtra("title");
        mHeadPicUrl = getIntent().getStringExtra("headpic");
        mNickName = getIntent().getStringExtra("nickname");
        areaName = getIntent().getStringExtra("area_name");
        buildingName = getIntent().getStringExtra("building_name");
    }

    private void initView() {
        showLiveScreenMode();

        mBeautyDialogFragment = new BeautyDialogFragment();

        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControllLayer);
        mPublishRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });

        if (mFront) {
            mFlashView.setVisibility(View.GONE);
        } else {
            mFlashView.setVisibility(View.VISIBLE);
        }

        String mUserId = UserInfoCenter.getInstance().getUserId();
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, mUserId);
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        showHeadIcon(mHeadIcon, mHeadPicUrl);

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        DanmakuView danmakuView = ButterKnife.findById(this,R.id.danmakuView);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(danmakuView);


//        mMessageInput.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        mSwitchCam.setOnClickListener(this);
        mBeautyBtn.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    public void onJoinGroupCallback(int code, String msg) {
        if (0 == code) {
            //获取推流地址
            LogUtil.d(TAG, "onJoin group success" + msg);
            presenter.requestLVBAddr(msg, mTitle, mNickName, mHeadPicUrl, mCoverPicUrl, "",liveOrientation, new BaseListener<ResGetLVBAddr>() {
                @Override
                public void onSuccess(BaseResponse<ResGetLVBAddr> data, int flag) {
                    if(0 == data.getReturnValue()){
                        ResGetLVBAddr lvbAddress = data.getReturnData();
                        mPushUrl = lvbAddress.getPushurl();
                        liveId = lvbAddress.getLiveid();
                        startRecordAnimation();
                        // 友盟的分享组件并不完善，为了各种异常情况下正常推流，要多做一些事情
                        if (mShare_meidia == SHARE_MEDIA.MORE) {
                            startPublish();
                        } else {
                            /*startShare(timeStamp);
                            boolean isSupportShare = false;
                            if (mShare_meidia == SHARE_MEDIA.SINA) {
                                isSupportShare = true;
                            } else if (mShare_meidia == SHARE_MEDIA.QZONE) {
                                if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ) || UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QZONE)) {
                                    isSupportShare = true;
                                }
                            } else if (UMShareAPI.get(this).isInstall(this, mShare_meidia)) {
                                isSupportShare = true;
                            }
                            if (isSupportShare) {
                                startPublish();
                            }*/
                        }

                    }else if(500 == data.getReturnValue()){
                        LogUtil.e(TAG,"服务器错误："+data.getReturnMsg());
                    }else{
                        ToastUtils.show(TCLivePublisherActivity.this,data.getReturnMsg());
                    }
                }

                @Override
                public void onFailed(Throwable e, int flag) {

                }
            });
//            mTCPusherMgr.getPusherUrl(mUserId, msg, mTitle, mCoverPicUrl, mNickName, mHeadPicUrl, mLocation);
        } else if (FangConstants.NO_LOGIN_CACHE == code) {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(FangConstants.ERROR_MSG_NO_LOGIN_CACHE);
        } else {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(FangConstants.ERROR_MSG_JOIN_GROUP_FAILED + code);
        }
    }

    private void startPublishImpl() {
        mSharedNotPublished = false;
        mSharedNotPublished = false;

        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, this);
        mTXLivePusher.setPushListener(TCLivePublisherActivity.this);
        mTXPushConfig.setAutoAdjustBitrate(false);

        //设置视频水印
        mTXPushConfig.setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_watermark), 0.78f, 0.04f,0.2f);

        //切后台推流图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
        mTXPushConfig.setPauseImg(bitmap);
        mTXPushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mTXPushConfig.setBeautyFilter(mBeautyParams.mBeautyProgress, mBeautyParams.mWhiteProgress, mBeautyParams.mRuddyProgress);
            /*mTXPushConfig.setFaceSlimLevel(mBeautyParams.mFaceLiftProgress);
            mTXPushConfig.setEyeScaleLevel(mBeautyParams.mBigEyeProgress);*/
        mTXPushConfig.enableHighResolutionCaptureMode(false);

        /*// 调整摄像头为默认后置，但是可以通过切换按钮进行切换
        mTXPushConfig.setFrontCamera(mFront);
        // 手动对焦关闭，采用自动对焦模式
        mTXPushConfig.setTouchFocus(false);*/

        mTXLivePusher.setConfig(mTXPushConfig);
        mPhoneListener = new TXPhoneStateListener(mTXLivePusher);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);


//        mAudioCtrl.setPusher(mTXLivePusher);
        mTXCloudVideoView.setVisibility(View.VISIBLE);
        mTXCloudVideoView.clearLog();
        //mBeautySeekBar.setProgress(100);

        //设置视频质量：高清
        mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
        mTXCloudVideoView.enableHardwareDecode(true);
//        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
//        mTXLivePusher.setMirror(true);
        mTXLivePusher.startPusher(mPushUrl);
    }

    protected void startPublish() {
        if (checkPermission()) {
            startPublishImpl();
        }
    }

    protected void stopPublish() {
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopPusher();
        }
        /*if (mAudioCtrl != null) {
            mAudioCtrl.unInit();
            mAudioCtrl = null;
        }*/
    }



    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        ImageLoader.loadTransformImage(this, avatar,R.drawable.icon_man,R.drawable.icon_man,view,0);
    }


    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {
        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();

        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {
        if (null != mObjAnim)
            mObjAnim.cancel();

        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }

    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                ToastUtils.show(this, "请输入内容");
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContext(msg);
        entity.setType(FangConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(mHeadPicUrl, mNickName, msg);
            }
            mTCChatRoomMgr.sendDanmuMessage(msg);
        } else {
            mTCChatRoomMgr.sendTextMessage(msg);
        }
    }

    /**
     * 消息发送回调
     *
     * @param errCode    错误码，0代表发送成功
     * @param timMessage 发送的TIM消息
     */
    @Override
    public void onSendMsgCallback(int errCode, TIMMessage timMessage) {
        if (timMessage != null)
            if (errCode == 0) {
                TIMElemType elemType = timMessage.getElement(0).getType();
                if (elemType == TIMElemType.Text) {
                    //发送文本消息成功
                    LogUtil.d(TAG, "onSendTextMsgsuccess:" + errCode);
                } else if (elemType == TIMElemType.Custom) {
                    LogUtil.d(TAG, "onSendCustomMsgsuccess:" + errCode);
                }

            } else {
                LogUtil.d(TAG, "onSendMsgfail:" + errCode + " msg:" + timMessage.getMsgId());
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
           /* case FangConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, content);
                break;*/
            default:
                break;
        }
    }

    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(FangConstants.TEXT_TYPE);

        notifyMsg(entity);
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo)){
            return;
        }

        lTotalMemberCount++;
        lMemberCount++;
        mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount)+"人在观看直播");

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
        if (lMemberCount > 0){
            lMemberCount--;
        }else{
            LogUtil.d(TAG, "接受多次退出请求，目前人数为负数");
        }
        mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));
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

    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "点了个赞");
        else
            entity.setContext(userInfo.nickname + "点了个赞");

        mHeartLayout.addFavor();
        lHeartCount++;

        //todo：修改显示类型
        entity.setType(FangConstants.PRAISE);
        notifyMsg(entity);
    }

    private void notifyMsg(final TCChatEntity entity) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                if(entity.getType() == TCConstants.PRAISE) {
//                    if(mArrayListChatEntity.contains(entity))
//                        return;
//                }

                if (mArrayListChatEntity.size() > 1000){
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
    public void onGroupDelete() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.switch_cam:
                mFront = !mFront;
                mFlashOn = false;
                if(mFront){
                    mFlashView.setVisibility(View.GONE);
                }else{
                    mFlashView.setVisibility(View.VISIBLE);
                }

                if (mTXLivePusher != null) {
                    mTXLivePusher.switchCamera();
                }
                break;

            case R.id.flash_btn:
                if (!mTXLivePusher.turnOnFlashLight(!mFlashOn)) {
                    ToastUtils.show(this,"打开闪光灯失败");
                    return;
                }
                mFlashOn = !mFlashOn;
                mFlashView.setBackgroundResource(mFlashOn ? R.drawable.icon_flash_pressed : R.drawable.selector_flash);
                break;

            case R.id.beauty_btn:
                if (mBeautyDialogFragment.isAdded()){
                    mBeautyDialogFragment.dismiss();
                }else{
                    mBeautyDialogFragment.show(getFragmentManager(), "beauty");
                }
                break;

            case R.id.btn_share:
                // 弹出分享面板，进行分享直播地址
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.showShareboard(this,mShareAction,ShareDialog.SHARE_VIDEO,SHARE_URL,String.format(SHARE_TITLE,areaName,buildingName),SHARE_SUMMARY,mHeadPicUrl);
                break;

            case R.id.btn_close:
                showComfirmDialog(FangConstants.TIPS_MSG_STOP_PUSH, false);
                break;

            /*case R.id.btn_message_input:
                showInputMsgDialog();
                break;*/

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

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * 播放结束，显示直播信息
     */
    public void showDetailDialog() {
        //确认则显示观看detail
        stopRecordAnimation();

        String time = DateUtil.formatSecond(mSecond*1000);
        String heartCount = String.format(Locale.CHINA, "%d", lHeartCount);
        String totalMemberCount = String.format(Locale.CHINA, "%d", lTotalMemberCount);
        LiveFinishShowDetailActivity.startActivity(this,time,heartCount,totalMemberCount);
        finish();
    }

    /**
     * 直播之前显示横竖屏模式
     */
    public void showLiveScreenMode() {
        mControllLayer.setVisibility(View.GONE);
        //确认则显示观看detail
        stopRecordAnimation();
        LiveOrientationFragment liveOrientationFragment = new LiveOrientationFragment();
        liveOrientationFragment.setListener(new LiveOrientationFragment.OnLiveStartListener() {
            @Override
            public void onLiveStart(int orientation) {
                // 点击开始直播触发事件
                liveOrientation = orientation;
                mControllLayer.setVisibility(View.VISIBLE);
                mTCChatRoomMgr.createGroup();   // 主播创建群
            }
        });
        liveOrientationFragment.setCancelable(false);
        if (liveOrientationFragment.isAdded()){
            liveOrientationFragment.dismiss();
        }else{
            liveOrientationFragment.show(getFragmentManager(), "");
        }
}

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showComfirmDialog(String msg, Boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
                    quitRoom();
                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            quitRoom();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null,bundle,event);
        }
        if (event < 0) {
            if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {//网络断开，弹对话框强提醒，推流过程中直播中断需要显示直播信息后退出
                showComfirmDialog(FangConstants.ERROR_MSG_NET_DISCONNECTED, true);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {//未获得摄像头权限，弹对话框强提醒，并退出
                showErrorAndQuit(FangConstants.ERROR_MSG_OPEN_CAMERA_FAIL);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL || event == TXLiveConstants.PUSH_ERR_MIC_RECORD_FAIL) { //未获得麦克风权限，弹对话框强提醒，并退出
                ToastUtils.show(this, bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
                showErrorAndQuit(FangConstants.ERROR_MSG_OPEN_MIC_FAIL);
            } else {
                //其他错误弹Toast弱提醒，并退出
                ToastUtils.show(this, bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

                mTXCloudVideoView.onPause();
//                TCPusherMgr.getInstance().changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Offline);
                presenter.changeLiveStatus(applyId,liveId,FangConstants.LIVE_STATUS_OFFLINE, new OnChangeLiveStatusListener());
                stopRecordAnimation();
                finish();
            }
        }

        if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            LogUtil.d(TAG, "当前机型不支持视频硬编码");
            mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mTXPushConfig.setVideoBitrate(700);
            mTXPushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mTXPushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mTXPushConfig.enableHighResolutionCaptureMode(false);
            mTXLivePusher.setConfig(mTXPushConfig);
        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            showNetBusyTips();
        }

        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
//            TCPusherMgr.getInstance().changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Online);
            presenter.changeLiveStatus(applyId,liveId,FangConstants.LIVE_STATUS_ONLINE,new OnChangeLiveStatusListener());
        }
    }

    /**
     * 退出房间
     * 包含后台退出与IMSDK房间退出操作
     */
    public void quitRoom() {
        mTCChatRoomMgr.deleteGroup();
//        mTCPusherMgr.changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Offline);
        presenter.changeLiveStatus(applyId,liveId,FangConstants.LIVE_STATUS_OFFLINE,new OnChangeLiveStatusListener());
    }

    private void showNetBusyTips() {
        if (null == mNetBusyHandler) {
            mNetBusyHandler = new Handler(Looper.getMainLooper());
        }
        if (mNetBusyTips.isShown()) {
            return;
        }
        mNetBusyTips.setVisibility(View.VISIBLE);
        mNetBusyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        showComfirmDialog(FangConstants.TIPS_MSG_STOP_PUSH, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
        mTXCloudVideoView.onResume();

        if (mPasuing) {
            mPasuing = false;

            if (mTXLivePusher != null) {
                mTXLivePusher.resumePusher();
            }
        }

        if (mTXLivePusher != null) {
            mTXLivePusher.resumeBGM();
        }

        if (mSharedNotPublished)
            startPublish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
        mTXCloudVideoView.onPause();
        if (mTXLivePusher != null) {
            mTXLivePusher.pauseBGM();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        mPasuing = true;
        if (mTXLivePusher != null) {
            mTXLivePusher.pausePusher();
        }
    }


    @Override
    public void onNetStatus(Bundle bundle) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(bundle,null,0);
        }
    }

    @Override
    public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
        switch (key){
            case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
            case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setBeautyFilter(params.mBeautyStyle, params.mBeautyProgress, params.mWhiteProgress, params.mRuddyProgress);
                }
                break;

            case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setFilter(BeautyFilterUtil.getFilterBitmap(getResources(), params.mFilterIdx));
                }
                break;

            default:
                break;
        }
    }

    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mTCSwipeAnimationController.isMoving()){
                        mBroadcastTime.setText(DateUtil.formattedTime(mSecond));
                    }
                }
            });
//            if (MySelfInfo.getInstance().getIdStatus() == TCConstants.HOST)
//                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    private class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null){
                        pusher.pausePusher();
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) {
                        pusher.resumePusher();
                    }
                    break;
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                startPublishImpl();
                break;
            default:
                break;
        }
    }

    private class OnChangeLiveStatusListener implements BaseListener {
        @Override
        public void onSuccess(BaseResponse data, int flag) {
            if(0 == data.getReturnValue()){
                LogUtil.d(TAG,"call change live status success ……");
            }else if(500 == data.getReturnValue()){
                LogUtil.e(TAG,"服务器错误："+data.getReturnMsg());
            }else{
                ToastUtils.show(TCLivePublisherActivity.this,data.getReturnMsg());
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        onActivityRotation();
        super.onConfigurationChanged(newConfig);
    }

    private void onActivityRotation() {
        // 自动旋转打开，Activity随手机方向旋转之后，只需要改变推流方向
        int mobileRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default :
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
        }

        LogUtil.e("hition==","the push rotation is:"+pushRotation);
        //因为activity也旋转了，本地渲染相对正方向的角度为0。
        mTXLivePusher.setRenderRotation(0);
        //通过设置config是设置生效（可以不用重新推流，腾讯云是少数支持直播中热切换分辨率的云商之一）
        mTXPushConfig.setHomeOrientation(pushRotation);
        mTXLivePusher.setConfig(mTXPushConfig);
        mTXLivePusher.stopCameraPreview(true);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            presenter.onUnsubscribe();
        }

        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        stopRecordAnimation();
        mTXCloudVideoView.onDestroy();

        stopPublish();
        mTCChatRoomMgr.removeMsgListener();
//        mTCPusherMgr.setPusherListener(null);

        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

}
