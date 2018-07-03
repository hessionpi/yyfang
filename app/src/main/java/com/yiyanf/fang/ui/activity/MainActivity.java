package com.yiyanf.fang.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.tencent.TIMCallBack;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.ui.adapter.FragmentsViewPagerAdapter;
import com.yiyanf.fang.ui.fragment.AttentionVideosFragment;
import com.yiyanf.fang.ui.fragment.BaseFragment;
import com.yiyanf.fang.ui.fragment.RecommendVideosFragment;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.CheckVersionUtils;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.SPUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    @Bind(R.id.iv_user)
    ImageView mUserView;
    @Bind(R.id.iv_msg)
    ImageView mMessageView;
    @Bind(R.id.iv_attentions_unread)
    ImageView mUnreadAttentionView;
    @Bind(R.id.tv_unread)
    TextView mUnreadMsg;
    @Bind(R.id.rg_video)
    RadioGroup mRgVideo;
    @Bind(R.id.rb_recommend_video)
    RadioButton mRbRecommend;
    @Bind(R.id.rb_attention_video)
    RadioButton mRbAttention;

    @Bind(R.id.video_pager)
    ViewPager mVideoPager;
    @Bind(R.id.fb_publish)
    ImageButton mShootVideo;

    private static final int PAGE_RECOMMEND = 0;
    private static final int PAGE_ATTENTION = 1;

    private static final int MAX_MESSAGE_SHOW = 9;

    private List<BaseFragment> videoFragmentList = new ArrayList<>();


    /**
     * 第一次按下back键时间
     */
    long firstTimePressBack = 0;

    public static final String DELETE_VIDEO = "force_delete_video";
    public static final String DELETE_DISCUSS = "force_delete_discuss";
    public static final String ACTIVITY = "activity";
    public static final String PUBLISH_DISCUSS = "discuss_publish";
    public static final String REPLY_DISCUSS = "discuss_replied";
    public static final String ATTENTIONS = "attentions";


    private LoginModel userinfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        getPushExtra(getIntent());

        // Android 6.0 以后动态赋予权限   随便给一个requestCode就行，因为当前activity不关心回调函数
        checkPermission();

        // 检查新版本更新
        // 一天内只弹出一次提示升级
        if (DateUtil.getDay() > (int) SPUtils.get("update_ignore_time", 0)) {
            CheckVersionUtils versionUtils = new CheckVersionUtils();
            versionUtils.checkUpdate(this, false);
        }

        // 注册广播接收者
        registerBroadcast();

        /*String liveSDKVersion = TXLiveBase.getSDKVersionStr();
        LogUtil.v("hition==", "腾讯云直播sdk版本：" + liveSDKVersion);
        String imSDKVersion = TIMManager.getInstance().getVersion();
        LogUtil.v("hition==", "腾讯云通讯SDK版本：" + imSDKVersion);*/
    }

    private void initView() {
        int unread = (int) SPUtils.get("unread_msg",0);
        if(unread > 0){
            mUnreadMsg.setVisibility(View.VISIBLE);
            if(unread > MAX_MESSAGE_SHOW){
                mUnreadMsg.setText(MAX_MESSAGE_SHOW+"+");
            }else{
                mUnreadMsg.setText(String.valueOf(unread));
            }
        }

        // 未读的关注小红点提示
        int unreadAttentions = (int) SPUtils.get("unread_silence",0);
        if(unreadAttentions>0){
            mUnreadAttentionView.setVisibility(View.VISIBLE);
        }else{
            mUnreadAttentionView.setVisibility(View.GONE);
        }

        // init the video pager
        RecommendVideosFragment recommendFragment = new RecommendVideosFragment();
        AttentionVideosFragment attentionFragment = new AttentionVideosFragment();
        videoFragmentList.add(recommendFragment);
        videoFragmentList.add(attentionFragment);
        FragmentsViewPagerAdapter fragmentAdapter = new FragmentsViewPagerAdapter(getSupportFragmentManager(), videoFragmentList);
        mVideoPager.setAdapter(fragmentAdapter);
        mVideoPager.setOffscreenPageLimit(1);

        mVideoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case PAGE_RECOMMEND:
                        mRbRecommend.setChecked(true);
                        break;

                    case PAGE_ATTENTION:
                        mRbAttention.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mUserView.setOnClickListener(this);
        mMessageView.setOnClickListener(this);
        mShootVideo.setOnClickListener(this);
        mRgVideo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_recommend_video:
                        mVideoPager.setCurrentItem(PAGE_RECOMMEND);
                        break;

                    case R.id.rb_attention_video:
                        // 未读消息置为不显示
                        mUnreadAttentionView.setVisibility(View.GONE);
                        SPUtils.put("unread_silence",0);
                        mVideoPager.setCurrentItem(PAGE_ATTENTION);
                        break;
                }
            }
        });

    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();

            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
                return false;
            }
        }
        return true;
    }

    /**
     * 动态检查相机和麦克风权限
     *
     */
    private boolean checkRecordPermission() {
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
                ActivityCompat.requestPermissions(this,permissions.toArray(new String[0]),200);
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPushExtra(intent);
    }

    private void getPushExtra(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            String jsonExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(jsonExtra);
                String pushTag = jsonObject.optString("tag");
                switch (pushTag) {
                    case ACTIVITY:
                        // 平台活动内容，打开活动网页地址
                        String address = jsonObject.optString("url");
                        if(!TextUtils.isEmpty(address)){
                            WebActivity.startActivity(this,address);
                        }
                        break;

                    case DELETE_DISCUSS:
                    case PUBLISH_DISCUSS:
                    case REPLY_DISCUSS:
                        String videoId = jsonObject.optString("videoid");
                        VideoPlayActivity.startActivity(this,Long.parseLong(videoId),null);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果是发布视频，每次回来都回到视频导航页
        if (FangConstants.isRecordFinish) {
            mVideoPager.setCurrentItem(PAGE_ATTENTION);
            FangConstants.isRecordFinish = false;
        }

        UserInfoCenter uCenter = UserInfoCenter.getInstance();
        userinfo = uCenter.getLoginModel();
        if (null != userinfo) {
            ImageLoader.loadTransformImage(this, userinfo.getHeadpic(), R.drawable.nav_icon_unlogin, R.drawable.nav_icon_unlogin, mUserView, 0);
            if (!uCenter.isIMLogin()) {
                UserInfoCenter.getInstance().imLogin(userinfo.getUserid(), userinfo.getUsersign(), new UserInfoCenter.OnIMLoginListener() {
                    @Override
                    public void onLoginSuccess() {

                        FriendshipManagerPresenter.setMyNick(userinfo.getNickname(), new TIMCallBack() {
                            @Override
                            public void onError(int code, String msg) {
                                LogUtil.e("hition==", "设置昵称失败：" + code + " " + msg);
                            }

                            @Override
                            public void onSuccess() {
                                LogUtil.v("hition==", "set the nickname success ……");
                            }
                        });
                        FriendshipManagerPresenter.setMyFaceUrl(userinfo.getHeadpic(), new TIMCallBack() {
                            @Override
                            public void onError(int code, String msg) {
                                LogUtil.e("hition==", "设置头像失败：" + code + " " + msg);
                            }

                            @Override
                            public void onSuccess() {
                                LogUtil.v("hition==", "set the avatar success ……");

                            }
                        });

                    }

                    @Override
                    public void onLoginFail(int code, String desc) {
                        LogUtil.d("hition==", "login failed. code: " + code + " errmsg: " + desc);
                    }
                });
            }
        } else {
            mUserView.setImageResource(R.drawable.nav_icon_unlogin);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user:
              /*  if (null == userinfo) {
                    LoginActivity.startActivity(this);
                } else {*/
                    MineActivity.startActivity(this);
               // }
                break;

            case R.id.iv_msg:
                SPUtils.put("unread_msg",0);
                mUnreadMsg.setVisibility(View.GONE);
                MessageActivity.startActivity(this);
                break;

            case R.id.fb_publish:
                if (null == userinfo) {
                    LoginActivity.startActivity(this);
                } else {
                    boolean hasPermission = checkRecordPermission();
                    if(hasPermission){
                        RecordVideoActivity.startActivity(this);
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                for (int i=0;i < grantResults.length;i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                    // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                    /*if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){
                        // 解释原因，并且引导用户至设置页手动授权
                        DialogManager.showSelectDialog(this, R.string.tips_permission,
                                R.string.settings, R.string.cancel,
                                false, new DialogManager.DialogListener() {
                            @Override
                            public void onNegative() {

                            }

                            @Override
                            public void onPositive() {
                                //引导用户至设置页手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });
                        return ;
                    }*/
                }
                RecordVideoActivity.startActivity(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //两秒内再次按下则退出
        long delta = System.currentTimeMillis() - firstTimePressBack;
        if (delta > FangConstants.QUIT_INTERVAL) {
            showToast(R.string.press_again_exit);
            firstTimePressBack = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void registerBroadcast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yiyanf.fang.msg");
        filter.addAction("com.yiyanf.fang.attentions");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                case "com.yiyanf.fang.msg":
                    int unRead = intent.getIntExtra("msg_unread",0);
                    if(unRead > 0){
                        mUnreadMsg.setVisibility(View.VISIBLE);
                        if(unRead > MAX_MESSAGE_SHOW ){
                            mUnreadMsg.setText(MAX_MESSAGE_SHOW+"+");
                        }else{
                            mUnreadMsg.setText(String.valueOf(unRead));
                        }
                    }
                    break;

                case "com.yiyanf.fang.attentions":
                    int unReadVideo = intent.getIntExtra("attention_videos_unread",0);
                    if(unReadVideo > 0){
                        // 关注小红点显示出来
                        mUnreadAttentionView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            abortBroadcast();   //接收到广播后中断广播
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
