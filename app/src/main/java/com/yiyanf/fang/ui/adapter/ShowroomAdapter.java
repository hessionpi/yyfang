package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.HouseType;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Hition on 2017/12/19.
 */

public class ShowroomAdapter extends XMBaseAdapter<HouseType> {

    public ShowroomAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShowroomHolder(parent, R.layout.layout_item_showroom);
    }

    private class ShowroomHolder extends BaseViewHolder<HouseType>{

        TextView mShowroom;

        public ShowroomHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mShowroom = $(R.id.tv_showroom);
        }

        @Override
        public void setData(HouseType data) {
            mShowroom.setText(data.getHoutypename());
        }
    }
}
