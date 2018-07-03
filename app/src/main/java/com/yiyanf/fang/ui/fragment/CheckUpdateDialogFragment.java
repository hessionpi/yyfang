package com.yiyanf.fang.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.service.DownloadVersionService;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.SPUtils;

import butterknife.ButterKnife;

/**
 * 检查版本更新弹出框
 *
 * Created by Hition on 2018/2/1.
 */

public class CheckUpdateDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mCheckUpdateDialog = new Dialog(getActivity(), R.style.dialog);
        mCheckUpdateDialog.setContentView(R.layout.dialog_check_update);
        mCheckUpdateDialog.setCancelable(false);

        TextView mVersionName = ButterKnife.findById(mCheckUpdateDialog,R.id.tv_version_name);
        TextView mUpdateDesc = ButterKnife.findById(mCheckUpdateDialog,R.id.tv_update_description);
        Button mUpdateLater = ButterKnife.findById(mCheckUpdateDialog,R.id.btn_update_later);
        Button mUpdateNow = ButterKnife.findById(mCheckUpdateDialog,R.id.btn_update_now);

        String new_version_name = getArguments().getString("version_name");
        mVersionName.setText(new_version_name);
        mUpdateDesc.setText(getArguments().getString("update_desc"));
        String downloadUrl = getArguments().getString("download_url");

        mUpdateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 稍后更新
                mCheckUpdateDialog.dismiss();
                SPUtils.put("update_ignore_time", DateUtil.getDay());
            }
        });
        mUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 立即更新
                mCheckUpdateDialog.dismiss();
                // 启动更新Service服务，进行更新
                Intent intent = new Intent(getActivity(), DownloadVersionService.class);
                intent.putExtra("downloading_version_name", new_version_name);
                intent.putExtra("downloading_url", downloadUrl);
                getActivity().startService(intent);
            }
        });

        return mCheckUpdateDialog;
    }


}
