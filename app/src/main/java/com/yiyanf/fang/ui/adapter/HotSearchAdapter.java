package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Administrator on 2017/12/4.
 */

public class HotSearchAdapter extends XMBaseAdapter<String> {



    public HotSearchAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotSearchHolder(parent, R.layout.item_recyclerview_search_history);
    }

    private class HotSearchHolder extends BaseViewHolder<String> {
TextView tv_hot_text;

        HotSearchHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tv_hot_text=$(R.id.tv_hot_text);
        }

        @Override
        public void setData(String data) {
            tv_hot_text.setText(data);
        }

    }
}


