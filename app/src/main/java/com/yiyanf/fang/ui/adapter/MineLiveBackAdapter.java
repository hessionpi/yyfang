package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.FetchItemEntity;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.DateUtil;

/**
 * Created by Administrator on 2018/1/10.
 */

public class MineLiveBackAdapter extends XMBaseAdapter<FetchItemEntity> {

    public MineLiveBackAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineLiveBackHolder(parent, R.layout.item_recycler_mine_liveback);
    }

    private class MineLiveBackHolder extends BaseViewHolder<FetchItemEntity> {
        TextView mTitle;

        LinearLayout llStatus;
        ImageView ivStatus;
        TextView tvStatus;
        TextView tvArea;
        TextView tvBuilding;

        ImageView mCover;
        TextView tv_watch_count;
        TextView favorite_count;
        TextView talk_count;
        TextView publish_time;



        public MineLiveBackHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mTitle = $(R.id.tv_title);
            llStatus = $(R.id.ll_status);
            ivStatus = $(R.id.iv_status);
            tvStatus = $(R.id.tv_status);
            tvArea = $(R.id.tv_area);
            tvBuilding = $(R.id.tv_building);
            mCover = $(R.id.iv_cover);
            publish_time = $(R.id.publish_time);
            tv_watch_count = $(R.id.tv_watch_count);
            favorite_count = $(R.id.favorite_count);
            talk_count = $(R.id.talk_count);

        }

        @Override
        public void setData(FetchItemEntity data) {
            mTitle.setText(data.getTitle());
            ImageLoader.load(mContext, data.getCoverpic(), 0, 0, mCover);

            // 回放
            llStatus.setBackgroundResource(R.drawable.hot_playback_bg);
            ivStatus.setImageResource(R.drawable.ic_playback);
            tvStatus.setText("精彩回放  " + DateUtil.formatSecond(data.getDuration() * 1000));
            publish_time.setText("已发布：" + data.getPublishTime());

            if (!TextUtils.isEmpty(data.getArea())) {
                tvArea.setText(data.getArea());
            } else {
                tvArea.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getBuilding())) {
                tvBuilding.setText(data.getBuilding());
            } else {
                tvBuilding.setVisibility(View.GONE);
            }
            tv_watch_count.setText("播放" + data.getPlaytimes());
             favorite_count.setText("收藏"+data.getFavoritecount());
            talk_count.setText("评论" + data.getDiscusscount());

        }
    }


}

