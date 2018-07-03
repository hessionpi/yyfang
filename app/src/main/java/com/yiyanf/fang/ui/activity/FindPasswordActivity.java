package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.widget.ClearEditText;
import com.yiyanf.fang.util.MyCodeTimer;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.yiyanf.fang.R.id.bt_submit;

/**
 * 找回密码
 */
public class FindPasswordActivity extends BaseActivity implements IView, View.OnClickListener {
    @Bind(R.id.cet_code)
    EditText cetCode;
    @Bind(R.id.gain_code)
    TextView gainCode;
    @Bind(R.id.cet_number)
    ClearEditText cetNumber;
    @Bind(R.id.cet_password)
    ClearEditText cetPassword;
    @Bind(bt_submit)
    Button btSubmit;
    UserPresenter userPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FindPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        userPresenter = new UserPresenter(this);
        initView();
    }

    private void initView() {
        cetCode.setOnClickListener(this);
        gainCode.setOnClickListener(this);
        cetNumber.setOnClickListener(this);
        cetPassword.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btSubmit.setEnabled(false);
        cetNumber.addTextChangedListener(new TextWatcher() {
            String code;
            String password;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code = cetCode.getText().toString();
                password = cetPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() ==11 && password.length() >=6&& code.length() == 6) {
                    btSubmit.setEnabled(true);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_blue_bg);

                } else {
                    btSubmit.setEnabled(false);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_grey_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cetPassword.addTextChangedListener(new TextWatcher() {
            String number;
            String code;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = cetNumber.getText().toString();
                code = cetCode.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0 && number.length() == 11 && code.length() == 6) {
                    btSubmit.setEnabled(true);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_blue_bg);
                } else {
                    btSubmit.setEnabled(false);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_grey_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cetCode.addTextChangedListener(new TextWatcher(){
            String number;
            String password;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = cetNumber.getText().toString();
                password = cetPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && number.length() == 11 && password.length() >=6) {
                    btSubmit.setEnabled(true);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_blue_bg);

                } else {
                    btSubmit.setEnabled(false);
                    btSubmit.setBackgroundResource(R.drawable.shape_login_grey_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String mobile = cetNumber.getText().toString();
      //  Boolean isMobile = RegularUtils.isMobileExact(mobile);
      /*  if (!isMobile) {
            ToastUtils.show(this, "手机号格式不正确", Toast.LENGTH_LONG);
            return;
        }*/
        switch (view.getId()) {
            case R.id.gain_code:
                if (NetWorkUtil.isNetworkConnected(this)) {
                    MyCodeTimer timer = new MyCodeTimer(60 * 1000, 1000, gainCode, this);
                    timer.start();
                    gainCode.setTextColor(ContextCompat.getColor(this, R.color.cl_aaaaaa));
                    gainCode.setEnabled(false);

                    userPresenter.sendCode(mobile);
                }
                break;
            case bt_submit:
                String smsCode = cetCode.getText().toString();
                String pwd = cetPassword.getText().toString();
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
                userPresenter.findPassword(mobile,pwd,smsCode);
                break;
        }
    }

    @Override
    public void fillData(BaseResponse data, int flag) {

        switch (flag) {
            case FangConstants.USER_FIND_PASSWORD:
            if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
                Toast.makeText(this, "修改密码完成", Toast.LENGTH_LONG).show();
                LoginActivity.startActivity(this);
                finish();
            }else{
                ToastUtils.show(this, data.getReturnMsg());
            }
            break;
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
