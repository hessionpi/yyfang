package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tencent.TIMConversation;
import com.tencent.TIMFriendFutureItem;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMMessage;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.im.PushUtil;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.model.im.Conversation;
import com.yiyanf.fang.model.im.CustomMessage;
import com.yiyanf.fang.model.im.Message;
import com.yiyanf.fang.model.im.MessageFactory;
import com.yiyanf.fang.model.im.NomalConversation;
import com.yiyanf.fang.ui.adapter.ConversationAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.MyItemDecoration;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息通知
 * <p>
 * Created by Hition on 2017/12/22.
 */

public class MessageActivity extends BaseActivity implements ConversationView,FriendshipMessageView{


    @Bind(R.id.rv_msg)
    RecyclerView rvMsg;

    private TextView notifyContentView;
    private TextView notifyTimeView;
    private TextView interactiveView;
    private TextView interactiveTimeView;

    private List<Conversation> conversationList = new LinkedList<>();
    private LayoutInflater mInflater;
    private ConversationAdapter conversationAdapter;

    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;

    private LoginModel mUser;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(this);
        initLayoutView();
        mUser = UserInfoCenter.getInstance().getLoginModel();
        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        presenter = new ConversationPresenter(this);
        if(null != mUser){
            presenter.getConversation();
        }
    }

    private void initLayoutView() {
        MyItemDecoration decoration = new MyItemDecoration(this, LinearLayoutManager.HORIZONTAL, 1, R.color.colorLine);
        rvMsg.addItemDecoration(decoration);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMsg.setLayoutManager(verticalLayoutManager);
        HeaderView header = new HeaderView();
        conversationAdapter = new ConversationAdapter(this);
        conversationAdapter.addHeader(header);
        rvMsg.setAdapter(conversationAdapter);

        conversationAdapter.setOnDeleteListener(new OnSlideDeleteMenuClickListener());
        conversationAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                conversationAdapter.getItem(position).navToDetail(MessageActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        PushUtil.getInstance().reset();
    }

    /**
     * 初始化界面或刷新界面
     *
     * @param timConversations
     */
    @Override
    public void initView(List<TIMConversation> timConversations) {
        this.conversationList.clear();
        for (TIMConversation item:timConversations){
            switch (item.getType()){
                case C2C:
                    conversationList.add(new NomalConversation(item));
                    break;
            }
        }

        conversationAdapter.addAll(conversationList);
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null){
            conversationAdapter.notifyDataSetChanged();
            return;
        }
        /*if (message.getConversation().getType() == TIMConversationType.System){
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }*/

        if (MessageFactory.getMessage(message) instanceof CustomMessage){
            return;
        }
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                conversationAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList){
            if (conversation.getIdentify()!=null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())){
                conversationAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        Collections.sort(conversationList);
        conversationAdapter.notifyDataSetChanged();
        /*if (getActivity() instanceof  HomeActivity)
            ((HomeActivity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);*/
    }

    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message 最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        /*if (friendshipConversation == null){
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        }else{
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        refresh();*/
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    private class HeaderView implements XMBaseAdapter.ItemView{

        @Override
        public View onCreateView(ViewGroup parent) {
            View mView = mInflater.inflate(R.layout.layout_msg_header,null);
            notifyContentView = ButterKnife.findById(mView,R.id.tv_msg_content);
            notifyTimeView = ButterKnife.findById(mView,R.id.tv_msg_time);
            interactiveView = ButterKnife.findById(mView,R.id.tv_msg_interactive);
            interactiveTimeView = ButterKnife.findById(mView,R.id.tv_interactive_time);

            RelativeLayout mNotificationLayout =  ButterKnife.findById(mView,R.id.rl_notification_msg);
            RelativeLayout mInteractiveLayout =  ButterKnife.findById(mView,R.id.rl_interactive_msg);

            mNotificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转至通知详情页面
                    NotificationMsgActivity.startActivity(MessageActivity.this);
                }
            });
            mInteractiveLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 点击跳转到互动消息页面
                    InteractiveMsgActivity.startActivity(MessageActivity.this);
                }
            });
            return mView;
        }

        @Override
        public void onBindView(View headerView) {

        }
    }

    private long getTotalUnreadNum(){
        long num = 0;
        for (Conversation conversation : conversationList){
            num += conversation.getUnreadNum();
        }
        return num;
    }

    private class OnSlideDeleteMenuClickListener implements ConversationAdapter.OnConversationDeleteListener{

        @Override
        public void onConversationDelte(Conversation conversation) {
            if(1 == mUser.getRole()){
                // 经纪人用户需要弹框提示删除，否则直接删除
                DialogManager.showSelectDialog(MessageActivity.this, R.string.tips_delete_conversation,
                        R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onPositive() {
                        deleteConversation(conversation);
                    }
                });
            }else{
                deleteConversation(conversation);
            }
        }
    }

    /**
     * 删除回话
     * @param conversation  回话对象
     */
    private void deleteConversation(Conversation conversation){
        NomalConversation nomalConversation = (NomalConversation) conversation;
        if (nomalConversation != null){
            if (presenter.delConversation(nomalConversation.getType(), conversation.getIdentify())){
                conversationAdapter.remove(nomalConversation);
            }
        }
    }

}
