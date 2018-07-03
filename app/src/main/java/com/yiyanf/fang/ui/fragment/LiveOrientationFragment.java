package com.yiyanf.fang.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import com.tencent.rtmp.TXLiveConstants;
import com.yiyanf.fang.R;
import butterknife.ButterKnife;

/**
 * 直播屏幕方向选择（横屏直播  or  竖屏直播）
 *
 * Created by Hition on 2018/1/30.
 */

public class LiveOrientationFragment extends DialogFragment {

    private int screenOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
    private OnLiveStartListener listener;

    public void setListener(OnLiveStartListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mOrientationDialog = new Dialog(getActivity(), R.style.dialog);
        mOrientationDialog.setContentView(R.layout.dialog_screen_orientation);
        mOrientationDialog.setCancelable(false);

        ImageButton mClose = ButterKnife.findById(mOrientationDialog,R.id.ib_close);
        RadioGroup rgScreenOrientation = ButterKnife.findById(mOrientationDialog,R.id.rg_screen_orientation);
        Button mStartLive = ButterKnife.findById(mOrientationDialog,R.id.btn_live_start);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrientationDialog.dismiss();
                getActivity().finish();
            }
        });

        rgScreenOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb_screen_vertical:// 竖屏
                        screenOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;

                    case R.id.rb_screen_horizontal:// 横屏
                        screenOrientation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                }
            }
        });

        mStartLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != listener){
                    mOrientationDialog.dismiss();
                    listener.onLiveStart(screenOrientation);
                }
            }
        });

        return mOrientationDialog;
    }

    public interface OnLiveStartListener{
        void onLiveStart(int orientation);
    }

}
