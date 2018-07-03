package com.yiyanf.fang.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyanf.fang.R;

/**
 * Created by Administrator on 2017/12/13.
 */

public class TalkUtil {

    public static void showTalkInputMsg(String replyName, final Activity context, final EditText inputEt, final TextView tv_send, final OnPublishListener publishListener) {
        if (!replyName.isEmpty()) {
            inputEt.setHint("回复" + replyName);
        }
        tv_send.setClickable(false);
        inputEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //获取系统 InputMethodManager
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!hasFocus) {
                    //隐藏 软键盘
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    // mWriteComment.setVisibility(View.GONE);
                } else {
                    //显示 软键盘
                    imm.showSoftInput(view, 0);
                }
            }
        });
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_send.setClickable(true);
                    tv_send.setTextColor(ContextCompat.getColor(context, R.color.cl_406599));
                } else {
                    tv_send.setClickable(false);
                    tv_send.setTextColor(ContextCompat.getColor(context, R.color.cl_d5d5d5));
                }
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishListener.onPublish(inputEt.getText().toString());
            }
        });
        inputEt.setFocusable(true);
        inputEt.setFocusableInTouchMode(true);
        inputEt.requestFocus();

    }

    public interface OnPublishListener {
        void onPublish(String inputText);
    }
}
