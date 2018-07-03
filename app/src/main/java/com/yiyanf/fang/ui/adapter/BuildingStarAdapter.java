package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Counselor;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.ui.activity.LoginActivity;
import com.yiyanf.fang.ui.activity.PersonalCenterActivity;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;

/**
 * Created by Administrator on 2017/11/22.
 */

public class BuildingStarAdapter extends XMBaseAdapter<Counselor> {

    private OnConsultantListener listener;
    Context context;
    public BuildingStarAdapter(Context context) {
        super(context);
        this.context=context;
    }
    public void setOnReserveListener(OnConsultantListener listener){
        this.listener = listener;
    }
    @Override
    public BaseViewHolder<Counselor> OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildingStarHolder(parent, R.layout.item_recyclerview_building_star);
    }

    private class BuildingStarHolder extends BaseViewHolder<Counselor> {

        ImageView iv_avatar;
        TextView  tv_name;
        TextView attention;
        TextView mSendMsg;

        BuildingStarHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iv_avatar=$(R.id.iv_avatar);
            tv_name=$(R.id.tv_name);
            attention=$(R.id.attention);
            mSendMsg=$(R.id.tv_send_msg);
        }

        @Override
        public void setData(final Counselor data) {
            ImageLoader.loadTransformImage(mContext,data.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, iv_avatar, 0);
            if (data.getAttention()) {
                attention.setText("已关注");
                attention.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_authuser_bg));
            } else {
                attention.setText("+关注TA");
                attention.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_follow_authuser_bg));
            }
            tv_name.setText(data.getCounselorName());
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getCounselorId() ==new Integer( UserInfoCenter.getInstance().getUserId())) {
                        PersonalCenterActivity.startActivity(mContext, String.valueOf(data.getCounselorId()),1);
                      //  WebActivity.startActivity(mContext, FangConstants.USER +  data.getCounselorId()+ "&isu=1", "个人主页",data.getCounselorId());
                    } else {
                        PersonalCenterActivity.startActivity(mContext, String.valueOf(data.getCounselorId()),0);
                       // WebActivity.startActivity(mContext, FangConstants.USER+ data.getCounselorId()+"&isu=0","个人主页",data.getCounselorId());
                    }
                }
            });
            attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(UserInfoCenter.getInstance().getUserId())){
                        LoginActivity.startActivity(mContext);
                        return ;
                    }

                    int flag;
                    if( data.getAttention()==true){
                        flag = 0;
                        attention.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_follow_authuser_bg));
                        attention.setText("+ 关注TA");

                    }else{
                        flag = 1;
                        attention.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_authuser_bg));
                        attention.setText("已关注");

                    }
                    listener.onAttentionClick(data.getCounselorId()+"",flag);
                }
            });

            mSendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSendMsg(String.valueOf(data.getCounselorId()));
                }
            });
        }

    }
    public interface OnConsultantListener{
        void onAttentionClick(String pusherId,int flag);

        void onSendMsg(String consultantUid);
    }
}



