package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Building;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2017/10/27.
 */

public class AuthMineBuildingAdapter extends XMBaseAdapter<Building> {


    public AuthMineBuildingAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthMineBuildingHolder(parent, R.layout.item_recyclerview_authmine_building);
    }

    private class AuthMineBuildingHolder extends BaseViewHolder<Building> {
TextView building;

        AuthMineBuildingHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            building=$(R.id.building);

        }

        @Override
        public void setData(final Building data) {
            building.setText(data.getBuildingname());
        }

    }
}


