package com.yiyanf.fang.ui.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.tencent.rtmp.TXLiveConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.widget.beauty.TCHorizontalScrollView;
import com.yiyanf.fang.util.CommonUtils;
import java.util.ArrayList;
import butterknife.ButterKnife;

/**
 * 美颜 美白
 *
 * @author Hition at 2018-1-29
 **/
public class BeautyDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = BeautyDialogFragment.class.getSimpleName();

    public static final int BEAUTYPARAM_BEAUTY = 1;
    public static final int BEAUTYPARAM_WHITE = 2;
    /*public static final int BEAUTYPARAM_FACE_LIFT = 3;
    public static final int BEAUTYPARAM_BIG_EYE = 4;*/
    public static final int BEAUTYPARAM_FILTER = 5;
    /*public static final int BEAUTYPARAM_MOTION_TMPL = 6;
    public static final int BEAUTYPARAM_GREEN = 7;*/

    static public class BeautyParams{
        public int mBeautyProgress = 6;
        public int mWhiteProgress = 3;
        public int mRuddyProgress = 0;
        public int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH;

        public int mFilterIdx;

    }

    public interface OnBeautyParamsChangeListener{
        void onBeautyParamsChange(BeautyParams params, int key);
    }

    public interface OnDismissListener{
        void onDismiss();
    }

    private View mLayoutBeauty;

    private SeekBar mBeautySeekbar;
    private SeekBar mWhitenSeekbar;
    private TextView mTVBeauty;
    private TextView mTVFilter;

    private TCHorizontalScrollView mFilterPicker;
    private ArrayList<Integer> mFilterIDList;
    private ArrayAdapter<Integer> mFilterAdapter;

    private BeautyParams    mBeautyParams;
    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;
    private OnDismissListener mOnDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_beauty_area);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Log.d(TAG, "create fragment");
        mFilterPicker = ButterKnife.findById(dialog,R.id.filterPicker);
        mLayoutBeauty = ButterKnife.findById(dialog,R.id.layoutFaceBeauty);


        mFilterPicker = ButterKnife.findById(dialog,R.id.filterPicker);
        mFilterPicker.setVisibility(View.GONE);

        mBeautySeekbar = ButterKnife.findById(dialog,R.id.beauty_seekbar);
        mBeautySeekbar.setOnSeekBarChangeListener(this);
        mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);

        mWhitenSeekbar = ButterKnife.findById(dialog,R.id.whiten_seekbar);
        mWhitenSeekbar.setOnSeekBarChangeListener(this);
        mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);


        mFilterIDList = new ArrayList<>();
        mFilterIDList.add(R.drawable.orginal);
        mFilterIDList.add(R.drawable.langman);
        mFilterIDList.add(R.drawable.qingxin);
        mFilterIDList.add(R.drawable.weimei);
        mFilterIDList.add(R.drawable.fennen);
        mFilterIDList.add(R.drawable.huaijiu);
        mFilterIDList.add(R.drawable.landiao);
        mFilterIDList.add(R.drawable.qingliang);
        mFilterIDList.add(R.drawable.rixi);
        mFilterAdapter = new ArrayAdapter<Integer>(dialog.getContext(),0, mFilterIDList){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.filter_layout,null);
                }
                ImageView view = ButterKnife.findById(convertView,R.id.filter_image);
                if (position == 0) {
                    ImageView view_tint = ButterKnife.findById(convertView,R.id.filter_image_tint);
                    if (view_tint != null)
                        view_tint.setVisibility(View.VISIBLE);
                }
                view.setTag(position);
                view.setImageDrawable(getResources().getDrawable(getItem(position)));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        mBeautyParams.mFilterIdx = index;
                        selectFilter(mBeautyParams.mFilterIdx);
                        if(mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener){
                            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_FILTER);
                        }
                    }
                });
                return convertView;

            }
        };
        mFilterPicker.setAdapter(mFilterAdapter);
        if (mBeautyParams.mFilterIdx >=0 && mBeautyParams.mFilterIdx < mFilterAdapter.getCount()) {
            mFilterPicker.setClicked(mBeautyParams.mFilterIdx);
            selectFilter(mBeautyParams.mFilterIdx);
        } else {
            mFilterPicker.setClicked(0);
        }


        mTVBeauty = ButterKnife.findById(dialog,R.id.tv_face_beauty);
        mTVFilter = ButterKnife.findById(dialog,R.id.tv_face_filter);
        mTVBeauty.setSelected(true);
        mTVFilter.setSelected(false);

        mTVBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(true);
                mTVFilter.setSelected(false);

                mLayoutBeauty.setVisibility(View.VISIBLE);
                mFilterPicker.setVisibility(View.GONE);

                mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);
                mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);
            }
        });

        mTVFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(true);

                mFilterPicker.setVisibility(View.VISIBLE);
                mLayoutBeauty.setVisibility(View.GONE);
            }
        });


        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        //pitu
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnDismissListener != null){
            mOnDismissListener.onDismiss();
        }
    }

    public void setmOnDismissListener(OnDismissListener onDismissListener){
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.beauty_seekbar:
                mBeautyParams.mBeautyProgress = CommonUtils.filtNumber(9,mBeautySeekbar.getMax(),progress);
                if(mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener){
                    mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
                }
                break;

            case R.id.whiten_seekbar:
                mBeautyParams.mWhiteProgress = CommonUtils.filtNumber(9,mWhitenSeekbar.getMax(),progress);
                if(mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener){
                    mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void selectFilter(int index) {
        ViewGroup group = (ViewGroup)mFilterPicker.getChildAt(0);
        for (int i = 0; i < mFilterAdapter.getCount(); i++) {
            View v = group.getChildAt(i);
            ImageView IVTint = ButterKnife.findById(v,R.id.filter_image_tint);
            if (i == index) {
                IVTint.setVisibility(View.VISIBLE);
            } else {
                IVTint.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setBeautyParamsListner(BeautyParams params, OnBeautyParamsChangeListener listener){
        mBeautyParams = params;
        mBeautyParamsChangeListener = listener;
        //当BeautyDialogFragment重置时，先刷新一遍配置
        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener){
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
        }
    }
}
