package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.InteractiveMsg;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;

/**
 * 互动消息adapter
 *
 * Created by Hition on 2018/1/24.
 */

public class InteractiveAdapter extends XMBaseAdapter<InteractiveMsg> {

    public InteractiveAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new InteractiveHolder(parent, R.layout.recycler_item_interactive_msg);
    }

    private class InteractiveHolder extends BaseViewHolder<InteractiveMsg>{
        ImageView mAvatar;
        TextView mNickname;
        TextView mCreateTime;
        TextView mPublishContent;
        TextView mQuoteContent;
        TextView mSource;
        TextView mReply;

        public InteractiveHolder(ViewGroup parent, int res) {
            super(parent, res);
            mAvatar = $(R.id.iv_avatar);
            mNickname = $(R.id.tv_nickname);
            mCreateTime = $(R.id.tv_createtime);
            mPublishContent = $(R.id.tv_publish_comment);
            mQuoteContent = $(R.id.tv_quote);
            mSource = $(R.id.tv_source);
            mReply = $(R.id.tv_reply);
        }

        @Override
        public void setData(InteractiveMsg data) {
            ImageLoader.loadTransformImage(mContext,data.getThumbnail(),R.drawable.icon_man,R.drawable.icon_man,mAvatar,0);
            mNickname.setText(data.getNickname());
            mCreateTime.setText(data.getCreatetime());
            mPublishContent.setText(data.getContent());
            mQuoteContent.setText(data.getOrigincontent());
            mSource.setText(data.getSource());

            mReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出回复对话框提示




                }
            });
        }
    }

}
