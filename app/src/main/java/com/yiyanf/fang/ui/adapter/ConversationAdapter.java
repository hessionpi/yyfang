package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.yiyanf.fang.R;
import com.yiyanf.fang.model.im.Conversation;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.LeftSlideView;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.PixelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 回话列表适配器
 *
 * Created by Hition on 2017/12/22.
 */

public class ConversationAdapter extends XMBaseAdapter<Conversation> implements LeftSlideView.IonSlidingButtonListener {

    private OnConversationDeleteListener onDeleteListener;
    private LeftSlideView mMenu = null;

    public ConversationAdapter(Context context) {
        super(context);
    }

    public void setOnDeleteListener(OnConversationDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationHolder(parent, R.layout.item_conversation);
    }

    private class ConversationHolder extends BaseViewHolder<Conversation>{
        LeftSlideView mSlideMenu;
        TextView mDelete;
        LinearLayout mConversationLayout;
        ImageView avatar;
        ImageView unread;
        TextView tvName;
        TextView lastMessage;
        TextView time;

        ConversationHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mSlideMenu = $(R.id.slide_menu);
            mDelete = $(R.id.tv_delete);
            mConversationLayout = $(R.id.ll_content);
            avatar = $(R.id.avatar);
            unread = $(R.id.iv_red_point);
            tvName = $(R.id.tv_name);
            lastMessage = $(R.id.tv_message);
            time = $(R.id.tv_time);
        }

        @Override
        public void setData(final Conversation data) {
            //设置内容布局的宽为屏幕宽度
            mConversationLayout.getLayoutParams().width = PixelUtil.getScreenWidth(mContext);

            String name = data.getName();
            String identify = data.getIdentify();
            if(name.trim().equals(data.getIdentify())){
                //待获取用户资料的用户列表
                List<String> users = new ArrayList<>();
                users.add(identify);

                //获取用户资料
                TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
                    @Override
                    public void onError(int code, String desc){
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        LogUtil.e("hition==", code+" "+desc);
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result){
                        if(null!=result && !result.isEmpty()){
                            tvName.setText(result.get(0).getNickName());
                            ImageLoader.loadTransformImage(mContext,result.get(0).getFaceUrl(),R.drawable.head_other,R.drawable.head_other,avatar,0);
                        }else{
                            tvName.setText(data.getName());
                            ImageLoader.loadTransformImage(mContext,data.getFaceUrl(),R.drawable.head_other,R.drawable.head_other,avatar,0);
                        }
                    }
                });
            }

            ImageLoader.loadTransformImage(mContext,data.getFaceUrl(),R.drawable.head_other,R.drawable.head_other,avatar,0);
            if(data.getUnreadNum() > 0 ){
                unread.setVisibility(View.VISIBLE);
            }else{
                unread.setVisibility(View.GONE);
            }
            lastMessage.setText(data.getLastMessageSummary());
            time.setText(DateUtil.getTimeStr(data.getLastMessageTime()));

            // 侧滑删除监听事件
            mSlideMenu.setSlidingButtonListener(ConversationAdapter.this);
            final int position = ConversationAdapter.this.getPosition(data);
            mConversationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        if(null != mItemClickListener){
                            mItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onDeleteListener){
                        onDeleteListener.onConversationDelte(data);
                    }
                }
            });

        }
    }

    /**
     * 侧滑菜单打开
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }

    /**
     * 滑动或者点击了Item监听
     **/
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断菜单是否打开
     **/
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    /**
     * 侧滑删除监听
     */
    public interface OnConversationDeleteListener {
        void onConversationDelte(Conversation conversation);//点击“删除”
    }

}
