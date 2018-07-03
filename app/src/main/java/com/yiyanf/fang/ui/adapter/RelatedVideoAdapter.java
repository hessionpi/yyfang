package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.VODinfo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.DateUtil;

/**
 * Created by Hition on 2017/11/23.
 */

public class RelatedVideoAdapter extends XMBaseAdapter<VODinfo> {

    public RelatedVideoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReleatedVideoHolder(parent, R.layout.item_related_video);
    }

    private class ReleatedVideoHolder extends BaseViewHolder<VODinfo>{

        ImageView mVideoCover;
        TextView mVideDuration;
        TextView mVideoTitle;
        TextView mPlayTimes;

        ReleatedVideoHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mVideoCover = $(R.id.iv_video_cover);
            mVideDuration = $(R.id.tv_video_duration);
            mVideoTitle = $(R.id.tv_video_title);
            mPlayTimes = $(R.id.tv_play_times);
        }

        @Override
        public void setData(VODinfo data) {
            ImageLoader.load(mContext,data.getFrontcover(),mVideoCover);
            mVideDuration.setText(DateUtil.formatSecond(data.getDuration()*1000));
            mVideoTitle.setText(data.getTitle());
            mPlayTimes.setText(data.getPlaycount()+"次播放");
        }
    }
}
