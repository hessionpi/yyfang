package com.yiyanf.fang.ui.adapter.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.model.im.Message;

import java.util.List;

import butterknife.ButterKnife;


/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = ButterKnife.findById(view,R.id.leftMessage);
            viewHolder.rightMessage = ButterKnife.findById(view,R.id.rightMessage);
            viewHolder.leftPanel = ButterKnife.findById(view,R.id.leftPanel);
            viewHolder.rightPanel = ButterKnife.findById(view,R.id.rightPanel);
            viewHolder.sending = ButterKnife.findById(view,R.id.sending);
            viewHolder.error = ButterKnife.findById(view,R.id.sendError);
            viewHolder.sender = ButterKnife.findById(view,R.id.sender);
            viewHolder.rightDesc = ButterKnife.findById(view,R.id.rightDesc);
            viewHolder.systemMessage = ButterKnife.findById(view,R.id.systemMessage);
            viewHolder.mLeftAvatar = ButterKnife.findById(view,R.id.leftAvatar);
            viewHolder.mRightAvatar = ButterKnife.findById(view,R.id.rightAvatar);

            view.setTag(viewHolder);
        }
        if (position < getCount()){
            final Message data = getItem(position);
            data.showMessage(viewHolder, getContext());
        }
        return view;
    }


    public class ViewHolder{
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
        public ImageView mLeftAvatar;
        public ImageView mRightAvatar;
    }
}
