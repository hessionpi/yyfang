package com.yiyanf.fang.ui.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.yiyanf.fang.R;
import butterknife.ButterKnife;

/**
 * 没有wifi下播放视频提示框
 *
 * Created by Hition on 2018/3/16.
 */
public class WithoutWifiDialog extends DialogFragment {

    private OnPlayContinueWithoutWifi playContinueListener;

    public void setPlayContinueListener(OnPlayContinueWithoutWifi playContinueListener) {
        this.playContinueListener = playContinueListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mWithouwifiDialog = new Dialog(getActivity(), R.style.dialog);
        mWithouwifiDialog.setContentView(R.layout.dialog_without_wifi);

        Button mQuitPlay = ButterKnife.findById(mWithouwifiDialog,R.id.btn_quit);
        Button mPlayContinue = ButterKnife.findById(mWithouwifiDialog,R.id.btn_continue);
        mQuitPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mPlayContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playContinueListener != null){
                    playContinueListener.onPlayContinue();
                    dismiss();
                }
            }
        });
        return mWithouwifiDialog;
    }

    public interface OnPlayContinueWithoutWifi{
        void onPlayContinue();
    }
}
