package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Hition on 2017/10/27.
 */

public class DynamicPhotoGridAdapter extends XMBaseAdapter {

    public DynamicPhotoGridAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DynamicPhotoGridHolder(parent, R.layout.layout_item_dynamic_grid);
    }

    private class DynamicPhotoGridHolder extends BaseViewHolder<String>{

        ImageView mPhoto;
        ImageButton mDelete;

        public DynamicPhotoGridHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mPhoto = $(R.id.iv_photo);
            mDelete = $(R.id.tv_delete);
        }

        @Override
        public void setData(String data) {

        }
    }
}
