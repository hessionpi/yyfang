package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.AppVersionUtil;
import com.yiyanf.fang.util.CheckVersionUtils;
import com.yiyanf.fang.util.CommonUtils;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.SPUtils;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 设置界面
 * <p>
 * Created by Hition on 2017/12/28.
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener ,IView{


    @Bind(R.id.fl_user_info)
    FrameLayout flUserinfo;
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.tv_change_info)
    TextView tvChangeInfo;
    @Bind(R.id.tv_change_pwd)
    TextView tvChangePwd;
    @Bind(R.id.ct_msg_remind)
    CheckedTextView ctMsgRemind;
    @Bind(R.id.fl_clear_cache)
    FrameLayout flClearCache;
    @Bind(R.id.tv_cache_size)
    TextView mCacheSize;
    @Bind(R.id.tv_praise)
    TextView tvPraise;
    @Bind(R.id.fl_check_version)
    FrameLayout flCheckUpdate;
    @Bind(R.id.tv_version_current)
    TextView mCurrentVersion;
    @Bind(R.id.tv_about_us)
    TextView tvAboutUs;
    @Bind(R.id.btn_exit_login)
    Button btnExitLogin;
    @Bind(R.id.ct_open_phone)
    CheckedTextView ctOpenPhone;
    LoginModel loginInfo;
    UserPresenter presenter;
    public static void startActivity(Context context, String avatar) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra("user_avatar", avatar);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        presenter=new UserPresenter(this);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
       loginInfo = UserInfoCenter.getInstance().getLoginModel();
        if (null != loginInfo) {
            boolean isOpenPhone=loginInfo.isOpenPhone();
            ctOpenPhone.setChecked(isOpenPhone);
            String userAvatar = getIntent().getStringExtra("user_avatar");
            ImageLoader.loadTransformImage(this, userAvatar, R.drawable.icon_man, R.drawable.icon_man, ivUserAvatar, 0);
        } else {
            flUserinfo.setVisibility(View.GONE);
            btnExitLogin.setVisibility(View.GONE);
            tvChangePwd.setVisibility(View.GONE);
            ctOpenPhone.setVisibility(View.GONE);

        }

        ctMsgRemind.setChecked((boolean) SPUtils.get("msg_remind", true));

        String cacheSize = "0B";
        try {
            cacheSize = CommonUtils.getTotalCacheSize(this);
        } catch (Exception e) {
            LogUtil.e("hition==", e.getMessage());
            e.printStackTrace();
        }
        mCacheSize.setText(cacheSize);

        mCurrentVersion.setText("当前版本" + AppVersionUtil.getVersionName(this));

        ctMsgRemind.setOnClickListener(this);
        flUserinfo.setOnClickListener(this);
        tvChangePwd.setOnClickListener(this);
        flClearCache.setOnClickListener(this);
        tvPraise.setOnClickListener(this);
        flCheckUpdate.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        btnExitLogin.setOnClickListener(this);
        ctOpenPhone.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_user_info:
                UserinfoDetailActivity.startActivity(this);
                break;

            case R.id.tv_change_pwd:
                ChangePwdActivity.startActivity(this);
                break;

            case R.id.ct_msg_remind:
                ctMsgRemind.toggle();
                SPUtils.put("msg_remind", ctMsgRemind.isChecked());
                if (ctMsgRemind.isChecked()) {
                    // 重启极光推送
                    if (JPushInterface.isPushStopped(this)) {
                        JPushInterface.resumePush(this);
                    }
                    // 重开云通信推送

                } else {
                    // 关闭极光推送
                    if (!JPushInterface.isPushStopped(this)) {
                        JPushInterface.stopPush(this);
                    }
                    // 关闭云通讯推送

                }
                break;
            case R.id.ct_open_phone:
                if (ctOpenPhone.isChecked()) {
presenter.exposureMobile(0);

                } else {

                    DialogManager.showSelectDialogWithTitle(this, R.string.hint,"公开手机号（"+loginInfo.getMobile()+")后，其他用户可快捷在您的个人主页及发布的视频详情页拨打您的电话，您确定公开吗？", R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                        @Override
                        public void onPositive() {
                            presenter.exposureMobile(1);

                        }

                        @Override
                        public void onNegative() {

                        }
                    });
                }



                break;
            case R.id.fl_clear_cache:
                // 清除缓存
                boolean delSuccess = CommonUtils.clearAllCache(this);
                if (delSuccess) {
                    mCacheSize.setText("0B");
                    ToastUtils.show(this, "清除完成");
                }
                break;

            case R.id.tv_praise:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.fl_check_version:
                // 版本检查更新
                CheckVersionUtils versionUtils = new CheckVersionUtils();
                versionUtils.checkUpdate(this, true);
                break;

            case R.id.tv_about_us:
                WebActivity.startActivity(this, FangConstants.URL_ABOUT_US, getString(R.string.about_us));
                break;

            case R.id.btn_exit_login:
                DialogManager.showSelectDialog(this, R.string.exit_tips, R.string.ok, R.string.cancel, true, new DialogManager.DialogListener() {
                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onPositive() {

                        // 云通讯退出
                        UserInfoCenter.getInstance().imLogout(new UserInfoCenter.OnIMLogoutListener() {
                            @Override
                            public void onLogoutSuccess() {
                                EventBus.getDefault().post("logout");
                                // 删除极光推送别名，停止推送
                                JPushInterface.deleteAlias(SettingsActivity.this,FangConstants.JPUSH_SEQUENCE);
                                finish();
                            }

                            @Override
                            public void onLogoutFail(int code, String msg) {
                                ToastUtils.show(SettingsActivity.this, "退出登录失败，请稍后重试");
                            }
                        });
                    }
                });

                break;

            default:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowroom(String event) {
        if (!TextUtils.isEmpty(event) && event.startsWith("http://")) {
            ImageLoader.loadTransformImage(this, event, R.drawable.icon_man, R.drawable.icon_man, ivUserAvatar, 0);
        }
    }

    public void savePhone(){
        ctOpenPhone.toggle();
        LoginModel loginInfo = UserInfoCenter.getInstance().getLoginModel();
        loginInfo.setOpenPhone(ctOpenPhone.isChecked());
        UserInfoCenter.getInstance().setLoginBean(loginInfo);
}









    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
            savePhone();
        }

    }

    @Override
    public void showFailedView(int flag) {

    }
}
