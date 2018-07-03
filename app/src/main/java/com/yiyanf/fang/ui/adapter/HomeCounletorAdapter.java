package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Consultant;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class HomeCounletorAdapter extends XMBaseAdapter<Consultant> {


    public HomeCounletorAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CounletorHolder(parent, R.layout.item_recycler_counselor);
    }

    private class CounletorHolder extends BaseViewHolder<Consultant> {

        ImageView mAvatar;
        TextView mName;
        TextView mBuilding;
        Button mAttention;


        CounletorHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mAvatar = $(R.id.iv_avatar);
            mName = $(R.id.tv_name);
            mBuilding = $(R.id.tv_building);
            mAttention = $(R.id.btn_attention);
        }

        @Override
        public void setData(Consultant data) {
            ImageLoader.loadTransformImage(mContext,data.getConsultantthumb(),R.drawable.icon_man,R.drawable.icon_man,mAvatar,0);
            mName.setText(data.getConsultantname());
            mBuilding.setText(data.getRespbuildingname());
            if(0 == data.getIsattention()){
                mAttention.setText("+ 关注");
                mAttention.setTextColor(ContextCompat.getColor(mContext,R.color.cl_00bceb));
            }else{
                mAttention.setText("已关注");
                mAttention.setTextColor(ContextCompat.getColor(mContext,R.color.cl_cccccc));
            }

        }

    }
}

