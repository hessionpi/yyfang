package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */

public class BuildingFilterDetailsAdapter extends XMBaseAdapter<String> {

List<String> select;
    public BuildingFilterDetailsAdapter(Context context,List<String> select) {
        super(context);
        this.select=select;
    }

    public BuildingFilterDetailsAdapter(Context context, List<String> objects,List<String> select) {
        super(context, objects);
        this.select=select;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildingFilterDetailsHolder(parent, R.layout.item_recyclerview_building_filter_details);
    }

    private class BuildingFilterDetailsHolder extends BaseViewHolder<String> {
      TextView tv_building_type;
       boolean isClick=false;
        BuildingFilterDetailsHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tv_building_type=$(R.id.tv_building_type);
        }

        @Override
        public void setData(final String data) {

            tv_building_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isClick){
                        isClick=false;
                        tv_building_type.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_addbuilding_gray_bg));
                        select.remove(data);
                    }else{
                        isClick=true;
                        tv_building_type.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_addbuilding_blue_bg));
                        select.add(data);
                    }

                }
            });
        }

    }
}
