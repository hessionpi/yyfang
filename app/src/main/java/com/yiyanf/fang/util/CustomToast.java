package com.yiyanf.fang.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyanf.fang.R;

import butterknife.ButterKnife;

/**
 * Created by Hition on 2018/4/27.
 */

public class CustomToast {
    static Toast toast;

    private CustomToast() {
        throw new UnsupportedOperationException("cannot be instantiated ...");
    }

    public static void show(Context paramContext, int msgId, int duration) {
        show(paramContext,paramContext.getResources().getText(msgId), duration);
    }

    public static void show(Context paramContext, int msgId) {
        show(paramContext, paramContext.getResources().getText(msgId), Toast.LENGTH_SHORT);
    }

    public static void show(Context paramContext, CharSequence paramCharSequence) {
        show(paramContext, paramCharSequence, Toast.LENGTH_SHORT);
    }

    private static void show(Context context, CharSequence paramCharSequence, int duration) {
        if(null == toast){
            toast = new Toast(context);
            toast.setGravity(Gravity.TOP,0,120);// 位置会比原来的Toast偏上一些
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toastRoot = mInflater.inflate(R.layout.toast_view, null);
            TextView mToastMsg = ButterKnife.findById(toastRoot,R.id.tv_toast_msg);
            mToastMsg.setText(paramCharSequence);
            toast.setView(toastRoot);
            toast.setDuration(duration);
        }
        toast.show();
    }
}
