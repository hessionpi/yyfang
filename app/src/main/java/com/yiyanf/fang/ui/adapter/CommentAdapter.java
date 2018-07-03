package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Discussinfo;
import com.yiyanf.fang.api.model.Recentreplyinfo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import java.util.List;


/**
 * 全部评论适配器
 *
 * Created by Hition on 2018/03/20.
 */

public class CommentAdapter extends XMBaseAdapter<Discussinfo> {

    private OnCommentClickListener commentClickListener;

    public CommentAdapter(Context context) {
        super(context);
    }

    public void setCommentClickListener(OnCommentClickListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(parent, R.layout.item_layout_comment);
    }

    private class CommentHolder extends BaseViewHolder<Discussinfo> {
        ImageView mHeadpic;
        TextView mNickname;
        TextView mCommentTime;
        TextView mCommentContent;
        TextView tvCommentTotal;
        View mDividerView;

        CommentHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mHeadpic = $(R.id.iv_headpic);
            mNickname = $(R.id.tv_nickname);
            mCommentTime = $(R.id.tv_comment_time);
            mCommentContent = $(R.id.tv_comment_content);
            tvCommentTotal = $(R.id.tv_comment_total);
            mDividerView = $(R.id.v_divider);
        }

        @Override
        public void setData(final Discussinfo data) {
            ImageLoader.loadTransformImage(mContext,data.getThumbnail(),R.drawable.icon_man,R.drawable.icon_man,mHeadpic,0);
            mNickname.setText(data.getNickname());
            mCommentContent.setText(data.getContent());
            mCommentTime.setText(data.getDiscusstime());

            List<Recentreplyinfo> recentreplylist = data.getRecentreplylist();
            if(null != recentreplylist){
                tvCommentTotal.setText("查看全部"+data.getReplycount()+"条回复");
            }else{
                tvCommentTotal.setVisibility(View.GONE);
            }

            mHeadpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentClickListener.onUserClick(data.getUserid(),data);
                }
            });

            mNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentClickListener.onUserClick(data.getUserid(),data);
                }
            });

            tvCommentTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentClickListener.onMoreReply(data);
                }
            });
        }
    }

    public interface OnCommentClickListener {
        void onUserClick(String userid,Discussinfo data);

        void onMoreReply(Discussinfo discuss);
    }

}
