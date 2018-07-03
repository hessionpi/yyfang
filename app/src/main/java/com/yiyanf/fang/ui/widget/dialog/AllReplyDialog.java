package com.yiyanf.fang.ui.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Recentreplyinfo;
import com.yiyanf.fang.api.model.ResGetReplyList;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.CommentPresenter;
import com.yiyanf.fang.ui.activity.PersonalCenterActivity;
import com.yiyanf.fang.ui.adapter.ReplyAllAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;
import java.util.List;
import butterknife.ButterKnife;

/**
 * 查看全部回复
 *
 * Created by Hition on 2018/3/20.
 */
public class AllReplyDialog extends DialogFragment implements IView {

    private RecyclerView mRvReply;
    private TextView mAllReplyCount;
    private EditText mReplyInput;
    private TextView mPublishReply;

    private CommentPresenter presenter;
    private ReplyAllAdapter replyAdapter;
    private int pageNo = 1;
    private long discussId;
    private long replyId;
    private String talkUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CommentPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        talkUid = args.getString("talk_publisher_uid");
        String talkAvatarUrl = args.getString("talk_avatar");
        String talkUname = args.getString("talk_publisher_uname");
        discussId = args.getLong("discuss_id");
        String talkPublishTime = args.getString("talk_publish_time");
        String content = args.getString("talk_content");

        final Dialog mAllReplyDialog = new Dialog(getActivity(), R.style.dialog);
        mAllReplyDialog.setContentView(R.layout.dialog_reply_all);

        ImageView mClose = ButterKnife.findById(mAllReplyDialog,R.id.iv_all_reply_close);
        ImageView mUserAvatar = ButterKnife.findById(mAllReplyDialog,R.id.iv_talker_avatar);
        RelativeLayout mDiscussLayout = ButterKnife.findById(mAllReplyDialog,R.id.rl_discuss_layout);
        TextView mNickname = ButterKnife.findById(mAllReplyDialog,R.id.tv_talker_nickname);
        TextView mTime = ButterKnife.findById(mAllReplyDialog,R.id.tv_talker_publish_time);
        TextView mContent = ButterKnife.findById(mAllReplyDialog,R.id.tv_talker_publish_content);
        mAllReplyCount = ButterKnife.findById(mAllReplyDialog,R.id.tv_all_reply);
        mRvReply = ButterKnife.findById(mAllReplyDialog,R.id.rv_reply);
        mReplyInput = ButterKnife.findById(mAllReplyDialog,R.id.et_input_reply);
        mPublishReply = ButterKnife.findById(mAllReplyDialog,R.id.tv_publish_reply);

        ImageLoader.loadTransformImage(getActivity(),talkAvatarUrl,R.drawable.icon_man,R.drawable.icon_man,mUserAvatar,0);
        mNickname.setText(talkUname);
        mTime.setText(talkPublishTime);
        mContent.setText(content);
        LinearLayoutManager llManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRvReply.setLayoutManager(llManager);
        replyAdapter = new ReplyAllAdapter(getActivity());
        replyAdapter.setMainPublisherId(talkUid);
        mRvReply.setAdapter(replyAdapter);
        replyAdapter.setMore(R.layout.view_recyclerview_more, new OnLoadMoreReplyListener());
        replyAdapter.setError(R.layout.view_recyclerview_error);
        mReplyInput.setHint("回复@"+talkUname+":");

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mUserAvatar.setOnClickListener(new OnUserClickListener());
        mNickname.setOnClickListener(new OnUserClickListener());

        mReplyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    mPublishReply.setClickable(false);
                    mPublishReply.setTextColor(ContextCompat.getColor(getActivity(),R.color.cl_b2b2b2));
                    mPublishReply.setBackgroundResource(R.drawable.ic_discuss_send_unable);
                }else{
                    mPublishReply.setClickable(true);
                    mPublishReply.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                    mPublishReply.setBackgroundResource(R.drawable.ic_discuss_send);
                }
            }
        });

        mDiscussLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyId = 0;
                mReplyInput.setHint("回复@"+talkUname+":");
            }
        });
        mPublishReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 回复主评论内容
                String replyContent = mReplyInput.getText().toString();
                makeReply(talkUname,replyContent);
            }
        });

        replyAdapter.setUserClickListener(new ReplyAllAdapter.OnUserClickListener() {
            @Override
            public void onUserclick(String publisherId) {
                PersonalCenterActivity.startActivity(getActivity(),publisherId,0);
            }
        });

        replyAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 回复评论者发的内容,自己不能回复自己
                Recentreplyinfo disInfo = replyAdapter.getItem(position);
                String replyuserid = disInfo.getReplyuserid();
                if(replyuserid.equals(UserInfoCenter.getInstance().getUserId())){
                    return ;
                }

                // 针对评论内容进行回复
                mReplyInput.setHint("回复@"+disInfo.getReplyname()+":");

                // 升起软键盘
                mReplyInput.requestFocus();
                showSoftKeyboard(mReplyInput);
                replyId = disInfo.getReplyid();
            }
        });
        requestReply();
        return mAllReplyDialog;
    }

    private void showSoftKeyboard(View editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    private void requestReply(){
        if(NetWorkUtil.isNetworkConnected(getActivity())){
            presenter.getReplyList(discussId,pageNo, FangConstants.PAGE_SIZE_DEFAULT);
        }else{
            ToastUtils.show(getActivity(),R.string.no_network);
        }
    }

    private void makeReply(String name,String replyContent){
        if(NetWorkUtil.isNetworkConnected(getActivity())){
            presenter.reply(name,discussId,replyId,replyContent);
        }else{
            ToastUtils.show(getActivity(),R.string.no_network);
        }
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        switch (flag){
            case FangConstants.VOD_DISCUSS_REPLY_LIST:
                if(0 == data.getReturnValue()){
                    ResGetReplyList replyData = (ResGetReplyList) data.getReturnData();
                    if(null!=replyData){
                        long total = replyData.getTotalcount();
                        mAllReplyCount.setText("全部回复("+total+")");
                        int totalPage = replyData.getTotalpage();
                        if(pageNo > totalPage){
                            replyAdapter.stopMore();
                            return;
                        }

                        List<Recentreplyinfo> replyinfoList = replyData.getReplylist();
                        if(null != replyinfoList && !replyinfoList.isEmpty()){
                            replyAdapter.addAll(replyinfoList);
                        }
                    }
                }
                break;

            case FangConstants.VOD_DISCUSS_REPLY:
                if(0 == data.getReturnValue()){
                    // 回复成功
                    dismiss();
                }
                break;
        }


    }

    @Override
    public void showFailedView(int flag) {

    }

    private class OnLoadMoreReplyListener implements XMBaseAdapter.OnLoadMoreListener{

        @Override
        public void onLoadMore() {
            pageNo++ ;
            requestReply();
        }
    }

    /**
     * 跳转至个人中心页面
     */
    private class OnUserClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            PersonalCenterActivity.startActivity(getActivity(),talkUid,0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onUnsubscribe();
    }

}
