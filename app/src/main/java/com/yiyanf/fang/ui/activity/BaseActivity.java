package com.yiyanf.fang.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.IPresenter;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.util.CustomToast;
import com.yiyanf.fang.util.StatisticalTools;
import com.yiyanf.fang.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Hition on 2017/9/25.
 */

public class BaseActivity extends AppCompatActivity {


    //被踢下线广播监听
    private LocalBroadcastManager mLocalBroadcatManager;
    private BroadcastReceiver mExitBroadcastReceiver;

    protected IPresenter mPresenter;

    //loading用
    public Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocalBroadcatManager = LocalBroadcastManager.getInstance(this);
        mExitBroadcastReceiver = new ExitBroadcastRecevier();
        mLocalBroadcatManager.registerReceiver(mExitBroadcastReceiver, new IntentFilter(FangConstants.EXIT_APP));

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    private void logout(){
        // 退出登录
        UserInfoCenter.getInstance().clearUserInfo();
        UserInfoCenter.getInstance().reset();
        EventBus.getDefault().post("logout");
        // 删除极光推送别名，停止推送
        JPushInterface.deleteAlias(this,FangConstants.JPUSH_SEQUENCE);
    }

    public void onReceiveExitMsg() {
//        TCUtils.showKickOutDialog(this);
        ToastUtils.show(this,"被踢下线通知！");
        DialogManager.showSelectDialog(this, R.string.kick_logout , R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
            @Override
            public void onNegative() {
                // 取消重新登录，则需要执行登出操作
                logout();
            }

            @Override
            public void onPositive() {
                // 确定需要重新登陆
                LoginActivity.startActivity(BaseActivity.this);
            }
        });
    }

    public class ExitBroadcastRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FangConstants.EXIT_APP)) {
                //在被踢下线的情况下，执行退出前的处理操作：停止推流、关闭群组
                onReceiveExitMsg();
            }
        }
    }


    /**
     * 显示loading对话框
     */
    private void showDialog() {
        loadingDialog = new Dialog(this, R.style.loading_dialog);
        LayoutInflater inflater2 = LayoutInflater.from(this);
        View v = inflater2.inflate(R.layout.dialog_common_layout, null);// 得到加载view
        loadingDialog.setCancelable(false);//true 点击空白处或返回键消失   false 不消失
        loadingDialog.setContentView(v);// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadingDialog.dismiss();
                }
                return false;
            }
        });
        loadingDialog.show();
    }

    /**
     * 显示dialog加载进度
     */
    public void showLoadingView() {
        showDialog();
    }

    public void dismissLoading(){
        if(null != loadingDialog && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    public void showToast(int msgResId) {
        showToast(getString(msgResId));
    }

    public void showToast(String msg) {
        ToastUtils.show(this,msg);
    }

    public void showNetworkToast(int msgResId){
        CustomToast.show(this,msgResId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPresenter){
            mPresenter.onUnsubscribe();
        }
        mLocalBroadcatManager.unregisterReceiver(mExitBroadcastReceiver);
    }
}
