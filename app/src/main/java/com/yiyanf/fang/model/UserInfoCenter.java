package com.yiyanf.fang.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.rtmp.TXLog;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.SerializeTools;

/**
 * 用于序列化用户信息
 *
 * Created by Hition on 2017/10/17.
 */

public final class UserInfoCenter {

    private static UserInfoCenter instance;
    private LoginModel loginBean;

    private boolean imLogin = false;

    private UserInfoCenter(){
        loadLoginModel();
    }

    public static UserInfoCenter getInstance() {
        if(null == instance){
            synchronized (UserInfoCenter.class){
                if (null == instance){
                    instance = new UserInfoCenter();
                }
            }
        }
        return instance;
    }

    public boolean isIMLogin(){
        return imLogin;
    }

    /**
     * 快捷方法获取用户id
     * @return userid
     */
    public String getUserId() {
        String id = "";
        if(loginBean!=null) {
            id = loginBean.getUserid();
        }
        return id;
    }

    /**
     * 获取登陆成功后的用户信息,用于判断登录状态
     *
     * @return null 未登录, else 已登录
     */
    public LoginModel getLoginModel() {
        return loginBean;
    }

    public void setLoginBean(LoginModel loginModel) {
        this.loginBean = loginModel;
        if(loginModel != null) {
            saveLoginModel(loginModel);
        }
    }

    /**
     * 登录成功后调用，将用户信息序列化到本地
     *
     * @param model 登录模型
     */
    private void saveLoginModel(LoginModel model){
        String fileName = getFileDir();
        if(!TextUtils.isEmpty(fileName)) {
            SerializeTools.serialization(fileName, model);
        }else{
            LogUtil.d("userinfo", "登录初始化数据失败");
        }
    }

    /**
     * 加载 文件下的LoginModel
     */
    private void loadLoginModel() {
        String fileName = getFileDir();
        if(!TextUtils.isEmpty(fileName)) {
            try {
                Object obj = SerializeTools.deserialization(fileName);
                if (obj instanceof LoginModel) {
                    loginBean = (LoginModel) obj;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 退出登录时清空用户信息
     */
    public void clearUserInfo(){
        String fileName = getFileDir();
        SerializeTools.deletePath(fileName);
    }

    /**
     * 恢复重置
     */
    public void reset() {
        loginBean = null;
    }

    private String getFileDir() {
        return FangApplication.getApplication().getFilesDir().toString()+ FangConstants.LOGIN_MODEL_FILENAME;
    }

    /**
     * imsdk登录接口，与tls登录验证成功后调用
     * @param identify 用户id
     * @param userSig  用户签名（托管模式下由TLSSDK生成 独立模式下由开发者在IMSDK云通信后台确定加密秘钥）
     */
    public void imLogin(@NonNull String identify, @NonNull String userSig, final OnIMLoginListener imLoginListener) {
        TIMUser user = new TIMUser();
        user.setIdentifier(identify);


        //发起登录请求
        TIMManager.getInstance().login(FangConstants.IMSDK_APPID, user, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                imLogin = false;
                imLoginListener.onLoginFail(code,desc);
            }

            @Override
            public void onSuccess() {
                imLogin = true;
                // 登录云通信成功
                imLoginListener.onLoginSuccess();
            }
        });
    }

    /**
     * imsdk登出
     */
    public void imLogout(final OnIMLogoutListener imLogoutListener) {
        LoginBusiness.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                imLogoutListener.onLogoutFail(i,s);
                TXLog.e("hition==", "IMLogout fail ：" + i + " msg " + s);
            }

            @Override
            public void onSuccess() {
                imLogin = false;
                reset();
                clearUserInfo();
                imLogoutListener.onLogoutSuccess();
                //sendBroadcast(new Intent(TCConstants.EXIT_APP));
                LogUtil.i("hition===", "IMLogout succ !");
            }
        });
    }

    public interface OnIMLoginListener {
        void onLoginSuccess();

        void onLoginFail(int code, String desc);
    }

    public interface OnIMLogoutListener {
        void onLogoutSuccess();

        void onLogoutFail(int code, String msg);
    }

}
