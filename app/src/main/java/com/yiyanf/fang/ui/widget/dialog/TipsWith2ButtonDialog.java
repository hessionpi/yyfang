package com.yiyanf.fang.ui.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yiyanf.fang.R;

import butterknife.ButterKnife;

/**
 * 带有两个按钮的提示框
 *
 * Created by Hition on 2018/3/16.
 */
public class TipsWith2ButtonDialog extends DialogFragment {

    private TipsDialogListener tipsDialogListener;

    public void setDialogListener(TipsDialogListener tipsDialogListener) {
        this.tipsDialogListener = tipsDialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mWithouwifiDialog = new Dialog(getActivity(), R.style.dialog);
        mWithouwifiDialog.setContentView(R.layout.dialog_tips_with_double_button);

        TextView mTipsMsg = ButterKnife.findById(mWithouwifiDialog,R.id.tv_message);
        Button mNegativeBtn = ButterKnife.findById(mWithouwifiDialog,R.id.btn_negative);
        Button mPositiveBtn = ButterKnife.findById(mWithouwifiDialog,R.id.btn_positive);

        String tipsMsg = getArguments().getString("tips_msg");
        String tipsNegText = getArguments().getString("negative_txt");
        String tipsPosText = getArguments().getString("positive_txt");
        mTipsMsg.setText(tipsMsg);
        mNegativeBtn.setText(tipsNegText);
        mPositiveBtn.setText(tipsPosText);

        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsDialogListener.onNegative();
            }
        });
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsDialogListener.onPositive();
            }
        });
        return mWithouwifiDialog;
    }

    public interface TipsDialogListener {
        void onNegative();
        void onPositive();
    }

}
