package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Respsbuilding;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MineBuildingAdapter extends XMBaseAdapter<Respsbuilding> {


    public MineBuildingAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineBuildingHolder(parent, R.layout.item_presonal_center_building);
    }

    private class MineBuildingHolder extends BaseViewHolder<Respsbuilding> {
        TextView building;

        MineBuildingHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            building=$(R.id.building);

        }

        @Override
        public void setData(final Respsbuilding data) {
            if (data.getAreaname()==null||"".equals(data.getAreaname())){
                building.setText(data.getBuildingname());
            }else{
            building.setText(data.getAreaname()+"-"+data.getBuildingname());}
        }

    }
}


