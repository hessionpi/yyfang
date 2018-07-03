package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Recentreplyinfo;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.StringUtil;

import static com.yiyanf.fang.R.id.tv_talk;

/**
 * Created by Administrator on 2017/11/27.
 */

public class CommentDetailsAdapter extends XMBaseAdapter<Recentreplyinfo> {

    private OnReplyListener replyListener;

    private String publishDiscussUid;

    public CommentDetailsAdapter(Context context) {
        super(context);
    }

    public void setPublishDiscussUid(String publishDiscussUid) {
        this.publishDiscussUid = publishDiscussUid;
    }

    public void setReplyListener(OnReplyListener replyListener) {
        this.replyListener = replyListener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TalkListDetailsHolder(parent, R.layout.item_recyclerview_talk_details);
    }

    private class TalkListDetailsHolder extends BaseViewHolder<Recentreplyinfo>{
      //  RecyclerView rv_reply;
        TextView tvTalk;
        ImageView iv_avatar;
        TextView tv_nickname;
        TextView tv_talk_text;
        TextView tv_talk_time;
        TextView mDelete;

        TalkListDetailsHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
          //  rv_reply = $(R.id.rv_reply);
            tvTalk=$(tv_talk);
            iv_avatar = $(R.id.iv_avatar);
            tv_nickname = $(R.id.tv_nickname);
            tv_talk_text = $(R.id.tv_talk_text);
            tv_talk_time = $(R.id.tv_talk_time);
            mDelete = $(R.id.tv_delete);
        }

        @Override
        public void setData(final Recentreplyinfo data) {
            ImageLoader.loadTransformImage(mContext, data.getThumbnail(), R.drawable.icon_man, R.drawable.icon_man, iv_avatar, 0);
            tv_nickname.setText(data.getReplyname());
            if(data.getReplieduserid().trim().equals(publishDiscussUid)){
                tv_talk_text.setText(data.getContent());
            }else{
                String text = data.getReplyname()+"回复"+data.getRepliedname()+"："+data.getContent();
                String targetText = data.getReplyname();
                SpannableString highlightText = StringUtil.highlight(mContext, text, targetText, R.color.cl_406599, 0, 0);
                tv_talk_text.setText(highlightText);
            }

            tv_talk_time.setText(data.getReplytime());
            String userId = UserInfoCenter.getInstance().getUserId();
            if(!TextUtils.isEmpty(userId)){
                if(userId.trim().equals(data.getReplyuserid().trim())){
                    mDelete.setVisibility(View.VISIBLE);
                }else{
                    mDelete.setVisibility(View.GONE);
                }
            }

            tvTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != replyListener){
                        replyListener.publishReply(data.getReplyid());
                    }
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != replyListener){
                        int position = mObjects.indexOf(data);
                        replyListener.deleteReply(data.getReplyid(),position);
                    }
                }
            });
        }
    }

    public interface OnReplyListener{

        void publishReply(long replyId);

        void deleteReply(long replyId,int position);
    }


}
