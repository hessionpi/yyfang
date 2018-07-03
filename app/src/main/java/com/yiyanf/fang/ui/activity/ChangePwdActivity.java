package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改登录密码页
 * <p>
 * Created by Hition on 2017/12/29.
 */

public class ChangePwdActivity extends BaseActivity implements IView {


    @Bind(R.id.et_pwd_old)
    EditText etPwdOld;
    @Bind(R.id.et_pwd_new)
    EditText etPwdNew;
    @Bind(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @Bind(R.id.btn_change_pwd)
    Button btnChangePwd;

    private UserPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChangePwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        initView();
        presenter = new UserPresenter(this);
    }

    private void initView() {

        etPwdOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    btnChangePwd.setClickable(true);
                    btnChangePwd.setBackgroundResource(R.drawable.shape_login_blue_bg);
                }else{
                    btnChangePwd.setClickable(false);
                    btnChangePwd.setBackgroundResource(R.drawable.shape_login_grey_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用修改密码接口
                String oldPwd = etPwdOld.getText().toString();
                String newPwd = etPwdNew.getText().toString();
                String confirmPwd = etPwdConfirm.getText().toString();

                if(TextUtils.isEmpty(oldPwd)){
                    return;
                }
                if(TextUtils.isEmpty(newPwd)){
                    return;
                }
                if(TextUtils.isEmpty(confirmPwd)){
                    return;
                }

                if(newPwd.trim().equals(oldPwd.trim())){
                    ToastUtils.show(ChangePwdActivity.this,"新密码不能与旧密码一致");
                    return ;
                }

                if(!newPwd.trim().equals(confirmPwd.trim())){
                    ToastUtils.show(ChangePwdActivity.this,"两次密码输入不一致，请重新输入");
                    etPwdConfirm.setText("");
                    return ;
                }

                presenter.changePassword(oldPwd,newPwd);
            }
        });
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if(0 == data.getReturnValue()){
            ToastUtils.show(this,"修改密码成功");
            finish();
        }else if(500 == data.getReturnValue()){
            LogUtil.e("hition==",data.getReturnValue()+" "+data.getReturnMsg());
        }else{
            ToastUtils.show(this,data.getReturnMsg());
        }
    }

    @Override
    public void showFailedView(int flag) {

    }
}
