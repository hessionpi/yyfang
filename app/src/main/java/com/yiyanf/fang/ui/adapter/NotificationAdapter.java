package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.FangNotification;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * Created by Hition on 2018/1/19.
 */

public class NotificationAdapter extends XMBaseAdapter<FangNotification> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(parent, R.layout.recycle_item_msg_notification);
    }

    private class NotificationHolder extends BaseViewHolder<FangNotification>{

        private TextView mTime;
        private TextView mTitle;
        private TextView mMessage;

        public NotificationHolder(ViewGroup parent, int res) {
            super(parent, res);
            mTime = $(R.id.tv_time);
            mTitle = $(R.id.tv_title);
            mMessage = $(R.id.tv_msg);
        }

        @Override
        public void setData(FangNotification data) {
            mTime.setText(data.getMsgcreattime());
            mTitle.setText(data.getMsgtitle());
            mMessage.setText(data.getMsgcontent());
        }
    }
}
