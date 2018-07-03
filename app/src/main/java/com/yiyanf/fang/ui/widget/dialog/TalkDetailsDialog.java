package com.yiyanf.fang.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Discussinfo;
import com.yiyanf.fang.api.model.ResGetReplyList;
import com.yiyanf.fang.api.model.ResLogin;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.CommentPresenter;
import com.yiyanf.fang.ui.activity.BaseActivity;
import com.yiyanf.fang.ui.activity.LoginActivity;
import com.yiyanf.fang.ui.adapter.CommentDetailsAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.TalkUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import butterknife.ButterKnife;

import static com.yiyanf.fang.FangConstants.VOD_DISCUSS_REPLY;
import static com.yiyanf.fang.FangConstants.VOD_DISCUSS_REPLY_LIST;

/**
 * Created by Administrator on 2017/12/13.
 */

public class TalkDetailsDialog extends Dialog implements IView,View.OnLayoutChangeListener{
    CommentPresenter commentPresenter;
    Context context;
    CommentDetailsAdapter talkListDetailsAdapter;
    private int deletePosition;
    int talkPage = 1;LinearLayout mWriteComment;
    Discussinfo discussinfo;
    EditText et_input_comment;
    TextView tv_publish;
    View layout;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    public TalkDetailsDialog(@NonNull Context context, LinearLayout mWriteComment,Discussinfo discussinfo) {
        super(context);
        commentPresenter=new CommentPresenter(this);
        this.context=context;
        this.mWriteComment=mWriteComment;
        this.discussinfo=discussinfo;
    }
    /**
     * 删除自己回复的内容
     */
    private void delReply(long replyId){
        String userId = UserInfoCenter.getInstance().getUserId();
        if(!TextUtils.isEmpty(userId)){
            if(NetWorkUtil.isNetworkConnected(context)){
                commentPresenter.deleteReply(replyId);
            }else{
                ToastUtils.show(context,R.string.no_network);
            }
        }else{
            LoginActivity.startActivity(context);
        }
    }
    //讨论详情
    public Dialog showTalkDetailsDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.dialog_talk_view, null);
        final Dialog dialog = new Dialog(context);
        BaseActivity activity=(BaseActivity)context;
        //获取屏幕高度
        screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        dialog.setCanceledOnTouchOutside(true);
        //添加layout大小发生改变监听器
        layout.addOnLayoutChangeListener(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.show();

        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setWindowAnimations(R.style.predict_factor_dialog_anim);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        RecyclerView talkDetails = ButterKnife.findById(layout, R.id.rv_talk_details);
        ImageView ivClose = ButterKnife.findById(layout, R.id.iv_close);
        ImageView iv_avatar = ButterKnife.findById(layout, R.id.iv_avatar);
        TextView tv_nickname = ButterKnife.findById(layout, R.id.tv_nickname);
        TextView tv_talk_text = ButterKnife.findById(layout, R.id.tv_talk_text);
        TextView tv_talk_time = ButterKnife.findById(layout, R.id.tv_talk_time);
        TextView tv_count = ButterKnife.findById(layout, R.id.tv_count);
        TextView tv_talk = ButterKnife.findById(layout, R.id.tv_talk);
         et_input_comment = ButterKnife.findById(layout, R.id.et_input_comment);
         tv_publish = ButterKnife.findById(layout, R.id.tv_publish);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        talkDetails.setLayoutManager(linearLayoutManager);
        talkListDetailsAdapter = new CommentDetailsAdapter(context);
        talkListDetailsAdapter.setMore(R.layout.view_recyclerview_more, new XMBaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                talkPage++;
                commentPresenter.getReplyList(discussinfo.getDiscussid(), talkPage, FangConstants.PAGE_SIZE_DEFAULT);
            }
        });
        talkListDetailsAdapter.setNoMore(R.layout.view_recyclerview_nomore);
        talkListDetailsAdapter.setError(R.layout.view_recyclerview_error);
        talkDetails.setAdapter(talkListDetailsAdapter);
        talkListDetailsAdapter.setReplyListener(new MyReplyListener());
        talkPage = 1;
        commentPresenter.getReplyList(discussinfo.getDiscussid(), talkPage, FangConstants.PAGE_SIZE_DEFAULT);
        ImageLoader.loadTransformImage(context, discussinfo.getThumbnail(), R.drawable.camera_img, R.drawable.camera_img, iv_avatar, 0);
        tv_count.setText("全部回复（" + discussinfo.getReplycount() + "）");
        tv_nickname.setText(discussinfo.getNickname());
        tv_talk_text.setText(discussinfo.getContent());
        tv_talk_time.setText(discussinfo.getDiscusstime());
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialog.dismiss();
            }
        });
        tv_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TalkUtil.showTalkInputMsg("", (Activity) context, et_input_comment, tv_publish, new TalkUtil.OnPublishListener() {
                    @Override
                    public void onPublish(String inputText) {
                        LogUtil.e("hition===reply", inputText);
                        LoginModel userInfo = UserInfoCenter.getInstance().getLoginModel();
                        if (null != userInfo) {
                            String nickname = userInfo.getNickname();
                            commentPresenter.reply(nickname, discussinfo.getDiscussid(), 0, inputText);
                            hideSoftkeyboard();
                        } else {
                            LoginActivity.startActivity(context);
                        }
                    }
                });
            }
        });
        return dialog;
    }
    @Override
    public void fillData(BaseResponse data, int flag) {
        int returnValue = data.getReturnValue();
        switch (flag) {

            case VOD_DISCUSS_REPLY_LIST:
                if (FangConstants.RETURN_VALUE_OK == returnValue) {
                    ResGetReplyList resGetReplyList = (ResGetReplyList) data.getReturnData();
                    talkListDetailsAdapter.addAll(resGetReplyList.getReplylist());
                    talkListDetailsAdapter.notifyDataSetChanged();
                }

                break;
            case FangConstants.VOD_DISCUSS_REPLY_DELETE:
                // 删除回复成功，刷新所有回复列表和评论列表
                talkListDetailsAdapter.remove(deletePosition);
                break;
            case VOD_DISCUSS_REPLY:
                et_input_comment.setText("");
                if (FangConstants.RETURN_VALUE_OK == returnValue) {
                    // 发表回复成功后记得刷新回复列表
                    if (null != talkListDetailsAdapter) {
                        talkListDetailsAdapter.clear();
                    }
                    talkPage = 1;
                    commentPresenter.getReplyList(discussinfo.getDiscussid(), talkPage, FangConstants.PAGE_SIZE_DEFAULT);
                }

                break;
        }
    }

    @Override
    public void showFailedView(int flag) {

    }


    /**
     * 隐藏软件盘
     */
    private void hideSoftkeyboard() {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != inputManager) {
            inputManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            et_input_comment.clearFocus();
            et_input_comment.setFocusable(false);

        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            Toast.makeText(context, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            et_input_comment.clearFocus();
            et_input_comment.setFocusable(false);
        }


    }

    class  MyReplyListener implements CommentDetailsAdapter.OnReplyListener {

        @Override
        public void publishReply(final long replyId) {
            TalkUtil.showTalkInputMsg("", (Activity) context, et_input_comment, tv_publish, new TalkUtil.OnPublishListener() {
                @Override
                public void onPublish(String inputText) {
                    LogUtil.e("hition===reply", inputText);
                    LoginModel userInfo = UserInfoCenter.getInstance().getLoginModel();
                    if (null != userInfo) {
                        String nickname = userInfo.getNickname();
                        commentPresenter.reply(nickname,discussinfo.getDiscussid() , replyId, inputText);
                       // mWriteComment.setVisibility(View.GONE);
                        hideSoftkeyboard();
                    } else {
                        LoginActivity.startActivity(context);
                    }
                }
            });
        }
        @Override
        public void deleteReply(long replyId, int position) {
            // 删除回复
            deletePosition = position;
            delReply(replyId);
        }
    }

}
