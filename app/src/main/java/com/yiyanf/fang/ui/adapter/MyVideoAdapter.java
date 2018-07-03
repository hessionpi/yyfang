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
 * Created by Administrator on 2017/12/28.
 */

public class MyVideoAdapter extends XMBaseAdapter<VODinfo>{
    public MyVideoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineVideoHolder(parent, R.layout.item_recycler_my_video);
    }

    private class MineVideoHolder extends BaseViewHolder<VODinfo> {
        TextView mVideoTitle;
        ImageView mFrontCover;
        TextView mDuration;
        TextView video_status;
        TextView tv_watch_count;
        TextView favorite_count;
        TextView talk_count;

        MineVideoHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mFrontCover = $(R.id.iv_img);
            mDuration = $(R.id.tv_video_time);
            mVideoTitle = $(R.id.tv_title);
            video_status=$(R.id.video_status);
            tv_watch_count=$(R.id.tv_watch_count);
            favorite_count=$(R.id.favorite_count);
            talk_count=$(R.id.talk_count);

        }

        @Override
        public void setData(VODinfo data) {
            ImageLoader.load(mContext,data.getFrontcover(),mFrontCover);
            mDuration.setText(DateUtil.formatSecond(data.getDuration()*1000));
            mVideoTitle.setText(data.getTitle());
            tv_watch_count.setText("观看"+data.getPlaycount());
            favorite_count.setText("收藏"+data.getFavoritecount());
            talk_count.setText("评论"+data.getDiscusscount());
            video_status.setText(data.getVediostatus());


        }

    }
}
