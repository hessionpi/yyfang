package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2017/11/13.
 */

public class BuildingMoreMessageAdapter extends XMBaseAdapter<String> {


    public BuildingMoreMessageAdapter(Context context) {
        super(context);

    }


    @Override
    public BuildingMoreMessageHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildingMoreMessageHolder(parent, R.layout.item_recycler_more_message);
    }

    private class BuildingMoreMessageHolder extends BaseViewHolder<String> {

        BuildingMoreMessageHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);


        }

        @Override
        public void setData(final String data) {

        }

    }
}
