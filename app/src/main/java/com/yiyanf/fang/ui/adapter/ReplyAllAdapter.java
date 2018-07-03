package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Recentreplyinfo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;

/**
 * 全部回复列表适配器
 *
 * Created by hition on 2018/03/21.
 */

public class ReplyAllAdapter extends XMBaseAdapter<Recentreplyinfo> {

    private OnUserClickListener userClickListener;

    private String mainPublisherId;

    public void setUserClickListener(OnUserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    public void setMainPublisherId(String mainPublisherId) {
        this.mainPublisherId = mainPublisherId;
    }

    public ReplyAllAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReplyHolder(parent, R.layout.item_recyclerview_reply);
    }

    private class ReplyHolder extends BaseViewHolder<Recentreplyinfo>{
        ImageView mReplyUserAvatar;
        TextView mReplyUname;
        TextView mReplyTime;
        TextView mReplyContent;

        public ReplyHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mReplyUserAvatar = $(R.id.iv_replyer_headpic);
            mReplyUname = $(R.id.tv_reply_nickname);
            mReplyTime = $(R.id.tv_reply_time);
            mReplyContent = $(R.id.tv_reply_content);
        }

        @Override
        public void setData(Recentreplyinfo data) {
            ImageLoader.loadTransformImage(mContext,data.getThumbnail(),R.drawable.icon_man,R.drawable.icon_man,mReplyUserAvatar,0);
            mReplyUname.setText(data.getReplyname());
            mReplyTime.setText(data.getReplytime());
            if(data.getIsformain()){// 针对主评论进行回复
                mReplyContent.setText(data.getContent());
            }else{
                mReplyContent.setText("回复@"+data.getRepliedname()+":"+data.getContent());
            }
            mReplyUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userClickListener.onUserclick(data.getReplyuserid());
                }
            });
            mReplyUname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userClickListener.onUserclick(data.getReplyuserid());
                }
            });
        }
    }

    public interface OnUserClickListener{
        void onUserclick(String publisherId);
    }
}
