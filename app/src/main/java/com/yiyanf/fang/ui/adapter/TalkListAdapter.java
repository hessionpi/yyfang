package com.yiyanf.fang.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Discussinfo;
import com.yiyanf.fang.api.model.Recentreplyinfo;
import com.yiyanf.fang.api.model.ResGetDiscussList;
import com.yiyanf.fang.api.model.ResGetReplyList;
import com.yiyanf.fang.api.model.ResLogin;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.CommentPresenter;
import com.yiyanf.fang.ui.activity.LoginActivity;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.view.IView;

import java.util.List;

import butterknife.ButterKnife;

import static com.yiyanf.fang.FangConstants.VOD_DISCUSS_REPLY_LIST;

/**
 * Created by Administrator on 2017/11/28.
 */

public class TalkListAdapter extends XMBaseAdapter<Discussinfo> {
    CommentPresenter presenter;
    CommentDetailsAdapter talkListDetailsAdapter;
    int page;
    int typeId;
    String id;

    public TalkListAdapter(Context context, int typeId, String id) {
        super(context);
        this.typeId = typeId;
        this.id = id;

    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        return new TalkListHolder(parent, R.layout.item_recyclerview_talk);
    }


    private class TalkListHolder extends BaseViewHolder<Discussinfo> implements IView, XMBaseAdapter.OnLoadMoreListener {
        LinearLayout ll_reply;
        TextView tvTalk;
        ImageView iv_headpic;
        TextView tv_nickname;
        TextView tv_talk_text;
        TextView tv_talk_time;
        Discussinfo discussinfo;
        Recentreplyinfo recentreplyinfo;


        public TalkListHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iv_headpic = $(R.id.iv_headpic);
            ll_reply = $(R.id.ll_reply);
            tvTalk = $(R.id.tv_talk);
            tv_nickname = $(R.id.tv_nickname);
            tv_talk_text = $(R.id.tv_talk_text);
            tv_talk_time = $(R.id.tv_talk_time);
            presenter = new CommentPresenter(this);

        }

        @Override
        public void setData(final Discussinfo data) {
            this.discussinfo = data;
            ImageLoader.loadTransformImage(mContext, data.getThumbnail(), R.drawable.camera_img, R.drawable.camera_img, iv_headpic, 0);
            tv_nickname.setText(data.getNickname());
            tv_talk_text.setText(data.getContent());
            tv_talk_time.setText(data.getDiscusstime());
            // discussinfos= data.getRecentreplylist();
            if (data.getRecentreplylist() != null)
                addView(data.getRecentreplylist());
            if (data.getReplycount() > 3) {
                TextView moreView = new TextView(mContext);
                LinearLayout.LayoutParams moreLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                moreLP.setMargins(8, 12, 0, 12);
                moreView.setLayoutParams(moreLP);
                moreView.setTextColor(Color.parseColor("#406599"));
                moreView.setTextSize(12);
                int count = data.getReplycount() - 3;
                moreView.setText("更多" + count + "条评论");
                moreView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTalkDetailsDialog(mContext, discussinfo);
                        page = 1;
                        presenter.getReplyList(data.getDiscussid(), page, FangConstants.PAGE_SIZE_DEFAULT);

                    }
                });
                ll_reply.addView(moreView);
            }
            tvTalk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Dialog inputDialog = DialogManager.showTalkInputMsg("", (Activity) mContext, new DialogManager.OnPublishListener() {
                        @Override
                        public void onPublish(String inputText) {
                            LogUtil.e("hition===reply", inputText);
                            ResLogin userInfo = UserInfoCenter.getInstance().getLoginModel();
                            if (null != userInfo) {
                                presenter.reply(userInfo.getNickname(), data.getDiscussid(), 0, inputText);
                            } else {
                                LoginActivity.startActivity(mContext);
                            }
                        }
                    });

                    inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(null!= inputManager){
                                inputManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    });*/
                }
            });

        }

        void addView(final List<Recentreplyinfo> lists) {
            for (int i = 0; i < lists.size(); i++) {
                recentreplyinfo = lists.get(i);
                TextView talkView = new TextView(mContext);
                LinearLayout.LayoutParams talkLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                talkLP.setMargins(0, 0, 0, 10);
                talkView.setLayoutParams(talkLP);
                talkView.setTextColor(Color.parseColor("#333333"));
                talkView.setTextSize(12);

                if (recentreplyinfo.getReplieduserid().equals(recentreplyinfo.getReplyuserid())) {
                    talkView.setText(lists.get(i).getReplyname() + ": " + lists.get(i).getContent());
                } else {
                    talkView.setText(lists.get(i).getReplyname() + "回复" + lists.get(i).getRepliedname() + "：" + lists.get(i).getContent());
                }
                ll_reply.addView(talkView);
            }
        }

        @Override
        public void fillData(BaseResponse data, int flag) {
            int returnValue = data.getReturnValue();
            switch (flag) {
                case FangConstants.VOD_DISCUSS_REPLY:
                    if (FangConstants.RETURN_VALUE_OK == returnValue) {
                        //   presenter.getReplyList(discussinfo.getDiscussid(),page,FangConstants.PAGE_SIZE_DEFAULT);
                        presenter.getDiscussList(typeId, id, page, 2);
                    }


                    break;
                case FangConstants.VOD_DISCUSS_LIST:

                    if (FangConstants.RETURN_VALUE_OK == returnValue) {
                        ResGetDiscussList fetchVodData = (ResGetDiscussList) data.getReturnData();
                        notifyDataSetChanged();
                    }
                    break;
                case VOD_DISCUSS_REPLY_LIST:
                    if (FangConstants.RETURN_VALUE_OK == returnValue) {
                        ResGetReplyList resGetReplyList = (ResGetReplyList) data.getReturnData();
                        talkListDetailsAdapter.setData(resGetReplyList.getReplylist());
                        // talkListDetailsAdapter.notifyDataSetChanged();
                    }
                    break;
            }

        }

        @Override
        public void showFailedView(int flag) {

        }

        //讨论详情
        public Dialog showTalkDetailsDialog(Context activity, final Discussinfo discussinfo) {
            // public static Dialog showTalkDetailsDialog(Context activity, CommentPresenter presenter, Discussinfo discussinfo) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            View layout = inflater.inflate(R.layout.dialog_talk_view, null);
            final Dialog dialog = new Dialog(activity);
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(layout);
            dialog.show();

            Window dialogWindow = dialog.getWindow();//获取window
            dialogWindow.setWindowAnimations(R.style.predict_factor_dialog_anim);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            RecyclerView talkDetails = ButterKnife.findById(layout, R.id.rv_talk_details);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            talkDetails.setLayoutManager(linearLayoutManager);
//            talkListDetailsAdapter = new CommentDetailsAdapter(activity, discussinfo.getDiscussid());

            talkListDetailsAdapter.setMore(R.layout.view_recyclerview_more, new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    page++;
                    presenter.getReplyList(discussinfo.getDiscussid(), page, FangConstants.PAGE_SIZE_DEFAULT);
                }
            });
            talkListDetailsAdapter.setNoMore(R.layout.view_recyclerview_nomore);
            talkListDetailsAdapter.setError(R.layout.view_recyclerview_error);
            talkDetails.setAdapter(talkListDetailsAdapter);
            page = 1;
            presenter.getReplyList(discussinfo.getDiscussid(), page, FangConstants.PAGE_SIZE_DEFAULT);
            ImageView ivClose = ButterKnife.findById(layout, R.id.iv_close);
            ImageView iv_avatar = ButterKnife.findById(layout, R.id.iv_avatar);
            TextView tv_nickname = ButterKnife.findById(layout, R.id.tv_nickname);
            TextView tv_talk_text = ButterKnife.findById(layout, R.id.tv_talk_text);
            TextView tv_talk_time = ButterKnife.findById(layout, R.id.tv_talk_time);
            TextView tv_count = ButterKnife.findById(layout, R.id.tv_count);
            TextView tv_talk = ButterKnife.findById(layout, R.id.tv_talk);
            LinearLayout ll_comment = ButterKnife.findById(layout, R.id.ll_comment);

            ImageLoader.loadTransformImage(activity, discussinfo.getThumbnail(), R.drawable.camera_img, R.drawable.camera_img, iv_avatar, 0);
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
            tv_talk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Dialog inputDialog = DialogManager.showTalkInputMsg("", (Activity) mContext, new DialogManager.OnPublishListener() {
                        @Override
                        public void onPublish(String inputText) {
                            LogUtil.e("hition===reply", inputText);
                            ResLogin userInfo = UserInfoCenter.getInstance().getLoginModel();
                            if (null != userInfo) {
                                presenter.reply(userInfo.getNickname(), discussinfo.getDiscussid(), 0, inputText);
                            } else {
                                LoginActivity.startActivity(mContext);
                            }
                        }
                    });

                    inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(null!= inputManager){
                                inputManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    });*/
                }
            });
            ll_comment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Dialog inputDialog = DialogManager.showTalkInputMsg("", (Activity) mContext, new DialogManager.OnPublishListener() {
                        @Override
                        public void onPublish(String inputText) {
                            LogUtil.e("hition===reply", inputText);
                            ResLogin userInfo = UserInfoCenter.getInstance().getLoginModel();
                            if (null != userInfo) {
                                presenter.reply(userInfo.getNickname(), discussinfo.getDiscussid(), 0, inputText);
                            } else {
                                LoginActivity.startActivity(mContext);
                            }
                        }
                    });

                    inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(null!= inputManager){
                                inputManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    });*/
                }
            });
            return dialog;
        }

        @Override
        public void onLoadMore() {

        }
    /*    @Override
        public void onLoadMore() {
            page++;
            presenter.getReplyList(discussinfo.getDiscussid(),page,FangConstants.PAGE_SIZE_DEFAULT);
        }*/
    }

}

