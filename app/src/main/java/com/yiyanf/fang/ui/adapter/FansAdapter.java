package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.AttentionUser;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;

/**
 * Created by Administrator on 2017/12/28.
 */

public class FansAdapter extends XMBaseAdapter<AttentionUser> {
    Context context;

    public FansAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FansHolder(parent, R.layout.item_mine_attention_user);
    }

    private class FansHolder extends BaseViewHolder<AttentionUser> {

        ImageView avatar;
        TextView tv_nickname;


        FansHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);

            avatar = $(R.id.avatar);
            tv_nickname = $(R.id.tv_nickname);

        }

        @Override
        public void setData(AttentionUser data) {

            ImageLoader.loadTransformImage(context, data.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, avatar, 0);
            tv_nickname.setText(data.getNickname());

        }

    }
}
