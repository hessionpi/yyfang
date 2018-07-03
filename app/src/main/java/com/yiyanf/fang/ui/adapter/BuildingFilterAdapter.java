package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */

public class BuildingFilterAdapter extends XMBaseAdapter<String> {
    private Context mContext;
List<String> select;
    public BuildingFilterAdapter(Context context,List<String> select) {
        super(context);
        this.mContext = context;
        this.select=select;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildingfilterHolder(parent, R.layout.item_recyclerview_building_filter);
    }

    private class BuildingfilterHolder extends BaseViewHolder<String> {
        RecyclerView rv_building_type;

        BuildingfilterHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            rv_building_type = $(R.id.rv_building_type);
        }

        @Override
        public void setData(final String data) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
            rv_building_type.setLayoutManager(gridLayoutManager);
            final BuildingFilterDetailsAdapter adapter = new BuildingFilterDetailsAdapter(mContext,select);
            rv_building_type.setAdapter(adapter);
            List<String> lists = new ArrayList<>();
            lists.add("");
            lists.add("");
            lists.add("");
            lists.add("");
            lists.add("");
            lists.add("");
            lists.add("");
            lists.add("");
            adapter.setData(lists);

        }

    }
}
