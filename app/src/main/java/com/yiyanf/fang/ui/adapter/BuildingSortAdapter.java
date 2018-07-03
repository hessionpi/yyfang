package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2017/11/23.
 */

public class BuildingSortAdapter extends XMBaseAdapter<String> {



    public BuildingSortAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuildingSortHolder(parent, R.layout.item_recycler_building_sort);
    }

    private class BuildingSortHolder extends BaseViewHolder<String> {
       ImageView iv_sort_close;

        BuildingSortHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iv_sort_close=$(R.id.iv_sort_close);
        }

        @Override
        public void setData(final String data) {
            iv_sort_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  remove(data);
                    notifyDataSetChanged();
                }
            });
        }

    }
}
