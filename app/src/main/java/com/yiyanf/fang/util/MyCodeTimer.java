package com.yiyanf.fang.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.yiyanf.fang.R;

/**
 * Created by Administrator on 2017/10/12.
 */

    public class MyCodeTimer extends CountDownTimer {
    TextView view;
    Context context;
     public   MyCodeTimer(long millisInFuture, long countDownInterval, TextView view, Context context) {
            super(millisInFuture, countDownInterval);
            this.view=view;
            this.context=context;
        }



    @Override
        public void onTick(long millisUntilFinished) {
        view.setText("重新发送("+millisUntilFinished / 1000 + " S)");

        }

        @Override
        public void onFinish() {
            view.setEnabled(true);
            view.setTextColor(ContextCompat.getColor(context, R.color.cl_main));
            view.setText("发送验证码");
        }
    }

