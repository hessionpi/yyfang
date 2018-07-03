package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MineDynamicAdapter extends XMBaseAdapter<String> {


    public MineDynamicAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineDynamicHolder(parent, R.layout.item_recyclerview_mine_dynamic);
    }

    private class MineDynamicHolder extends BaseViewHolder<String> {


        MineDynamicHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);

        }

        @Override
        public void setData(String data) {

        }

    }
}
