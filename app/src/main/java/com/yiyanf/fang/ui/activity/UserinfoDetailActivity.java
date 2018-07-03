package com.yiyanf.fang.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.widget.ToolbarView;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.CosUtil;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改用户资料页
 *
 * @author hition
 */
public class UserinfoDetailActivity extends PictureActivity implements View.OnClickListener, IView {

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.fl_avatar)
    FrameLayout flAvatar;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_fang_id)
    TextView tvFangId;
    @Bind(R.id.tv_nickname)
    EditText tvNickname;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.icon_more)
    ImageView iconMore;
    @Bind(R.id.titlebar)
    ToolbarView titlebar;
    @Bind(R.id.et_company)
    EditText etCompany;
    @Bind(R.id.et_introduce)
    EditText etIntroduce;

    private String faceUrl;
    private int sex;
    private UserPresenter userPresenter;
    private CosUtil uploadUtils;

    private boolean isUpdate;
    String nickName;
    String ecompany;
String signature;
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserinfoDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_detail);
        ButterKnife.bind(this);
        initView();
        userPresenter = new UserPresenter(this);
        uploadUtils = new CosUtil(this, new UploadListener());
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(filePath)){
            ImageLoader.loadTransformFromFile(UserinfoDetailActivity.this,filePath,ivAvatar);
        }
    }*/

    private void initView() {
        LoginModel userInfo = UserInfoCenter.getInstance().getLoginModel();
        if (null != userInfo) {
            faceUrl = userInfo.getHeadpic();
            String oldNickname = userInfo.getNickname();
            sex = userInfo.getSex();
            ImageLoader.loadTransformImage(this, userInfo.getThumbnail(), ivAvatar, 0);
            tvMobile.setText(userInfo.getMobile());
            tvNickname.setText(oldNickname);
            String company = userInfo.getCompany();
            if (company != null) {
                etCompany.setText(company);
            }
            String signature = userInfo.getSignature();
            if (signature != null) {
                etIntroduce.setText(signature);
            }
            tvFangId.setText(userInfo.getFangId());
            if (0 == sex) {
                tvSex.setText("男");
            } else if (1 == userInfo.getSex()) {
                tvSex.setText("女");
            }
        }
        tvNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isUpdate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etCompany.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isUpdate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etIntroduce.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isUpdate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        titlebar.setOnLeftClickListener(new ToolbarView.OnLeftClickListener() {
            @Override
            public void onLeftImgClick() {
                if (isUpdate) {
                    DialogManager.showSelectDialog(UserinfoDetailActivity.this, "是否修改资料", R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                        @Override
                        public void onPositive() {
                            nickName = tvNickname.getText().toString();
                            ecompany = etCompany.getText().toString();
                            signature=etIntroduce.getText().toString();
                            userPresenter.updateUserinfo(faceUrl, nickName, sex, ecompany,signature);
                            // finish();
                        }

                        @Override
                        public void onNegative() {
                            finish();
                        }
                    });

                } else {
                    finish();
                }
            }

            @Override
            public void onLeftTextClick() {

            }
        });

        flAvatar.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvSex.setOnClickListener(this);
    }

    Dialog dialog;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.fl_avatar:
                // 弹出选择信息，选择头像模式
                super.showPhotoDialog(true);
                break;

            case R.id.iv_avatar:
                ImageViewActivity.startActivity(this, faceUrl);
                break;

            case R.id.tv_sex:
                if (null == dialog) {
                    dialog = showSexDialog();
                }
                dialog.show();
                break;

            case R.id.tv_man:
                sex = 0;
                isUpdate = true;
                tvSex.setText("男");
                dialog.dismiss();
                break;
            case R.id.tv_woman:
                sex = 1;
                isUpdate = true;
                tvSex.setText("女");
                dialog.dismiss();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;

        }
    }

    /**
     * 弹出选择性别对话框
     */
    public Dialog showSexDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_choose_sex, null);

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout);

        Window dialogWindow = dialog.getWindow();//获取window
        //  dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView man = (TextView) dialog.findViewById(R.id.tv_man);
        TextView woman = (TextView) dialog.findViewById(R.id.tv_woman);
        TextView mCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        return dialog;
    }

    @Override
    protected void uploadPhoto(String cosSign, String path) {
        uploadUtils.upLoad(cosSign, path, false);
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        super.fillData(data, flag);
        if (0 == data.getReturnValue()) {
            switch (flag) {
                case FangConstants.USER_PERFECT_DATA:
                    // 更新本地用户头像信息
                    UserInfoCenter userInfoCenter = UserInfoCenter.getInstance();
                    LoginModel newUserInfo = userInfoCenter.getLoginModel();
                    newUserInfo.setHeadpic(faceUrl);
                    newUserInfo.setThumbnail(faceUrl);
                    newUserInfo.setNickname(nickName);
                    if (ecompany != null)
                        newUserInfo.setCompany(ecompany);
                    if (signature!=null)
                        newUserInfo.setSignature(signature);
                    userInfoCenter.setLoginBean(newUserInfo);
                    EventBus.getDefault().post(faceUrl);

                    ToastUtils.show(this, "修改成功");
                    finish();
                    break;

                default:

                    break;
            }
        } else if (500 == data.getReturnValue()) {
            LogUtil.e("hition==", data.getReturnValue() + " : " + data.getReturnMsg());
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    /**
     * 图片上传监听
     */
    private class UploadListener implements CosUtil.OnUploadListener {

        @Override
        public void onSuccess(int code, String url) {
            // 头像上传COS成功
            if (0 == code) {
                // 修改用户头像成功
                faceUrl = url;
                isUpdate = true;
                /*if(!TextUtils.isEmpty(filePath)){
                    ImageLoader.loadTransformFromFile(UserinfoDetailActivity.this,filePath,ivAvatar);
                }*/
                ImageLoader.loadTransformImage(UserinfoDetailActivity.this, faceUrl, ivAvatar, 0);
            }
        }

        @Override
        public void onFailed(int errorCode, String msg) {
            // 头像上传COS失败
            ToastUtils.show(UserinfoDetailActivity.this, "头像上传失败了");
        }
    }

    @Override
    public void onBackPressed() {
        if (isUpdate) {
            DialogManager.showSelectDialog(UserinfoDetailActivity.this, "是否修改资料", R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                @Override
                public void onPositive() {
                    nickName = tvNickname.getText().toString();
                    ecompany = etCompany.getText().toString();
                    signature=etIntroduce.getText().toString();
                    userPresenter.updateUserinfo(faceUrl, nickName, sex, ecompany,signature);
                    // finish();
                }

                @Override
                public void onNegative() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != userPresenter) {
            userPresenter.onUnsubscribe();
        }
    }
}