package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.UmengErrorCode;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResLogin;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.widget.ClearEditText;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.MyCodeTimer;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 *
 * Created by Administrator on 2017/10/16.
 */

public class LoginActivity extends BaseActivity implements IView<ResLogin>, View.OnClickListener {

    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.login_line)
    View loginLine;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.register_line)
    View registerLine;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.login_number)
    ClearEditText loginNumber;
    @Bind(R.id.login_password)
    ClearEditText loginPassword;
    @Bind(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.login_wechat)
    ImageView loginWechat;
    @Bind(R.id.login_qq)
    ImageView loginQq;
    @Bind(R.id.register_number)
    ClearEditText registerNumber;
    @Bind(R.id.register_code)
    EditText registerCode;
    @Bind(R.id.gain_code)
    TextView gainCode;
    @Bind(R.id.register_password)
    ClearEditText registerPassword;
    @Bind(R.id.bt_register)
    Button btRegister;
    @Bind(R.id.login)
    LinearLayout login;
    @Bind(R.id.register)
    LinearLayout register;
    @Bind(R.id.tv_service_protocol)
    TextView mServiceProtocl;
    @Bind(R.id.tv_service_protocol_register)
    TextView mServiceProtoclRegist;


    private SHARE_MEDIA platform = null;
    UserPresenter userPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.anim_in, R.anim.anim_no);
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        userPresenter = new UserPresenter(this);
        initView();
    }

    private void initView() {
        mServiceProtocl.setOnClickListener(this);
        mServiceProtoclRegist.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        loginWechat.setOnClickListener(this);
        loginQq.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btLogin.setEnabled(false);
        loginNumber.addTextChangedListener(new TextWatcher() {
          String password;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = loginPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() ==11 && password.length() >=6) {
                    btLogin.setEnabled(true);
                    btLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_blue_bg));

                } else {
                    btLogin.setEnabled(false);
                    btLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_grey_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginPassword.addTextChangedListener(new TextWatcher() {
            String number;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = loginNumber.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0 && number.length() == 11) {
                    btLogin.setEnabled(true);
                    btLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_blue_bg));

                } else {
                    btLogin.setEnabled(false);
                    btLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_grey_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btRegister.setOnClickListener(this);
        gainCode.setOnClickListener(this);
        btRegister.setEnabled(false);
        registerNumber.addTextChangedListener(new TextWatcher() {
            String code;
            String password;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code = registerCode.getText().toString();
                password = registerPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() ==11 && password.length() >=6&& code.length() == 6) {
                    btRegister.setEnabled(true);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_blue_bg));

                } else {
                    btRegister.setEnabled(false);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_grey_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        registerPassword.addTextChangedListener(new TextWatcher() {
            String number;
            String code;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = registerNumber.getText().toString();
                code = registerCode.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0 && number.length() == 11 && code.length() == 6) {
                    btRegister.setEnabled(true);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_blue_bg));

                } else {
                    btRegister.setEnabled(false);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_grey_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        registerCode.addTextChangedListener(new TextWatcher(){
            String number;
            String password;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = registerNumber.getText().toString();
                password = registerPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && number.length() == 11 && password.length() >=6) {
                    btRegister.setEnabled(true);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_blue_bg));

                } else {
                    btRegister.setEnabled(false);
                    btRegister.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_login_grey_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_service_protocol:
            case R.id.tv_service_protocol_register:
                WebActivity.startActivity(this, FangConstants.URL_PROTOCOL,"用户服务条款");
                break;

            case R.id.tv_login:
                clearStatus();
                login.setVisibility(View.VISIBLE);
                tvLogin.setTextColor(ContextCompat.getColor(this, R.color.black));
                loginLine.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_register:
                clearStatus();
                register.setVisibility(View.VISIBLE);
                tvRegister.setTextColor(ContextCompat.getColor(this, R.color.black));
                registerLine.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close:
                finish();
                //  overridePendingTransition(R.anim.anim_no, R.anim.dialog_window_out);
                break;
            case R.id.tv_forget_password:
                FindPasswordActivity.startActivity(this);
                //  finish();
                break;
            case R.id.login_wechat:
                platform = SHARE_MEDIA.WEIXIN;
                oauthLogin();

                break;
            case R.id.login_qq:
                platform = SHARE_MEDIA.QQ;
                oauthLogin();

                break;
            case R.id.bt_login:
                String loginMobile = loginNumber.getText().toString();
                String password = loginPassword.getText().toString();
                if (loginMobile.length()!=11) {
                    ToastUtils.show(this, "手机号应为11位数字", Toast.LENGTH_LONG);
                    return;
                }

                if (TextUtils.isEmpty(password.trim())) {
                    ToastUtils.show(this, "密码不能为空！");
                    return;
                }
                if (password.trim().length()<6) {
                    ToastUtils.show(this, "密码不能小于6个字符！");
                    return;
                }
                userPresenter.login(loginMobile, password);
                break;

            case R.id.bt_register:
                String mobile = registerNumber.getText().toString();
                if (mobile.length()!=11) {
                    ToastUtils.show(this, "手机号应为11位数字", Toast.LENGTH_LONG);
                    return;
                }
                String smsCode = registerCode.getText().toString();
                String pwd = registerPassword.getText().toString();
                if (TextUtils.isEmpty(smsCode.trim())) {
                    ToastUtils.show(this, "验证码不能为空！");
                    return;
                }

                if (TextUtils.isEmpty(pwd.trim())) {
                    ToastUtils.show(this, "密码不能为空！");
                    return;
                }
                if (pwd.trim().length()<6) {
                    ToastUtils.show(this, "密码不能小于6个字符！");
                    return;
                }
                userPresenter.register(mobile, pwd, smsCode);
                break;
            case R.id.gain_code:
                String mobil = registerNumber.getText().toString();

                if (mobil.length()!=11) {
                    ToastUtils.show(this, "手机号应为11位数字", Toast.LENGTH_LONG);
                    return;
                }
                if (NetWorkUtil.isNetworkConnected(this)) {
                    MyCodeTimer timer = new MyCodeTimer(60 * 1000, 1000, gainCode, this);
                    timer.start();
                    gainCode.setTextColor(ContextCompat.getColor(this, R.color.cl_aaaaaa));
                    gainCode.setEnabled(false);

                    userPresenter.sendCode(mobil);
                }
        }
    }

    private void clearStatus() {
        login.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        tvLogin.setTextColor(ContextCompat.getColor(this, R.color.cl_888888));
        tvRegister.setTextColor(ContextCompat.getColor(this, R.color.cl_888888));
        loginLine.setVisibility(View.GONE);
        registerLine.setVisibility(View.GONE);
    }


    @Override
    public void fillData(BaseResponse<ResLogin> data, final int flag) {
        //登录
        switch (flag) {
            case FangConstants.UESR_LOGIN:
                int returnLoginValue = data.getReturnValue();
                if (FangConstants.RETURN_VALUE_OK == returnLoginValue) {
                    if (data.getReturnData() instanceof ResLogin) {
                        final ResLogin resLogin =  data.getReturnData();
                        if(null != resLogin){
                            String userId = resLogin.getUserid();
                            String userSign = resLogin.getUsersign();
                            final String nickname = resLogin.getNickname();
                            final String headpic = resLogin.getHeadpic();

                            // IMLogin
                            UserInfoCenter.getInstance().imLogin(userId, userSign, new UserInfoCenter.OnIMLoginListener() {
                                @Override
                                public void onLoginSuccess() {
                                    FriendshipManagerPresenter.setMyNick(nickname, new TIMCallBack() {
                                        @Override
                                        public void onError(int code, String msg) {
                                            LogUtil.e("hition==","设置昵称失败："+code+" "+msg);
                                        }

                                        @Override
                                        public void onSuccess() {
                                            LogUtil.v("hition==","set the nickname success ……");
                                        }
                                    });
                                    FriendshipManagerPresenter.setMyFaceUrl(headpic, new TIMCallBack() {
                                        @Override
                                        public void onError(int code, String msg) {
                                            LogUtil.e("hition==","设置头像失败："+code+" "+msg);
                                        }

                                        @Override
                                        public void onSuccess() {
                                            LogUtil.v("hition==","set the avatar success ……");
                                        }
                                    });
                                }

                                @Override
                                public void onLoginFail(int code, String desc) {
                                    LogUtil.d("hition==", "login failed. code: " + code + " errmsg: " + desc);
                                }
                            });

                            LoginModel loginInfo = new LoginModel(userId,headpic,resLogin.getThumbnail(),nickname);
                            loginInfo.setRole(resLogin.getFlag());
                            loginInfo.setSex(resLogin.getSex());
                            if (resLogin.getCompany()!=null)
                            loginInfo.setCompany(resLogin.getCompany());
                            if (resLogin.getSignature()!=null)
                            loginInfo.setSignature(resLogin.getSignature());
                            loginInfo.setUsersign(userSign);
                            loginInfo.setVideominduration(resLogin.getVideominduration());
                            loginInfo.setVideomaxduration(resLogin.getVideomaxduration());
                            loginInfo.setOpenPhone(resLogin.getMobilevisible());
                            UserInfoCenter.getInstance().setLoginBean(loginInfo);

                            EventBus.getDefault().post("login");
                            // 注册JPush别名
                            JPushInterface.setAlias(this,FangConstants.JPUSH_SEQUENCE,userId);
                            finish();
                        }
                    }
                } else if (FangConstants.RETURN_VALUE_SYSTEM_ERROE == returnLoginValue) {
                    LogUtil.e("hition==", getString(R.string.system_error));
                    //  LogUtil.e("hition==", "系统错误");

                } else {
                    ToastUtils.show(this, data.getReturnMsg());

                }

                break;
            //注册
            case FangConstants.USER_REGISTER:
                int returnRegisterValue = data.getReturnValue();
                if (FangConstants.RETURN_VALUE_OK == returnRegisterValue) {
                    if (data.getReturnData() instanceof ResLogin) {
                        ResLogin resLogin =  data.getReturnData();
                        if(null!=resLogin){
                            // 注册JPush别名
                            JPushInterface.setAlias(this,FangConstants.JPUSH_SEQUENCE,resLogin.getUserid());
                            LoginModel loginInfo = new LoginModel(resLogin.getUserid(),resLogin.getHeadpic(),resLogin.getThumbnail(),resLogin.getNickname());
                            loginInfo.setRole(resLogin.getFlag());
                            loginInfo.setSex(resLogin.getSex());
                            loginInfo.setUsersign(resLogin.getUsersign());
                            loginInfo.setVideominduration(resLogin.getVideominduration());
                            loginInfo.setVideomaxduration(resLogin.getVideomaxduration());
                            loginInfo.setOpenPhone(resLogin.getMobilevisible());
                            if (resLogin.getCompany()!=null)
                                loginInfo.setCompany(resLogin.getCompany());
                            if (resLogin.getSignature()!=null)
                                loginInfo.setSignature(resLogin.getSignature());
                            UserInfoCenter.getInstance().setLoginBean(loginInfo);
//                            UserInfoCenter.getInstance().imLogin(loginUserInfo.getUserid(),loginUserInfo.getUsersign());
                            PerfectDataActivity.startActivity(this);
                            finish();
                        }
                    }
                    return;
                }

                if (FangConstants.RETURN_VALUE_SYSTEM_ERROE == returnRegisterValue) {
                    LogUtil.e("hition==", getString(R.string.system_error));
                    // LogUtil.e("hition==", "系统错误");
                } else {
                    ToastUtils.show(this, data.getReturnMsg());
                }
                break;
        }
    }

    @Override
    public void showFailedView(int flag) {
        switch (flag) {
            case FangConstants.UESR_LOGIN:
                Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
                break;

            case FangConstants.USER_REGISTER:
                Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show();
                break;
        }
    }
    //第三方登录

    public void oauthLogin() {
        UMShareAPI.get(getApplicationContext()).getPlatformInfo(LoginActivity.this, platform, umAuthListener);

    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            ToastUtils.show(getApplicationContext(), "授权开始", Toast.LENGTH_SHORT);
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
            ToastUtils.show(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT);
            if (map != null) {
//                ResLogin resLogin = new ResLogin();
                LoginModel loginInfo = new LoginModel();
                if (share_media == SHARE_MEDIA.QQ) {
                    loginInfo.setUserid(map.get("uid"));
                    loginInfo.setNickname(map.get("name"));
                    loginInfo.setHeadpic(map.get("iconurl"));
                    loginInfo.setThumbnail(map.get("iconurl"));
                    String sex = map.get("gender");
                    if ("女".equals(sex)) {
                        loginInfo.setSex(1);
                    } else {
                        loginInfo.setSex(0);
                    }

                } else if (share_media == SHARE_MEDIA.SINA) {

                }
                UserInfoCenter.getInstance().setLoginBean(loginInfo);
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            String mess = throwable.getMessage();
            if (mess.equals(UmengErrorCode.NotInstall.getMessage())) {
                ToastUtils.show(getApplicationContext(), "授权失败:未安装应用", Toast.LENGTH_SHORT);
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.show(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_no, R.anim.anim_out);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        userPresenter.onUnsubscribe();

    }

}
