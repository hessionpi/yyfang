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
 * Created by Administrator on 2017/12/4.
 */

public class SearchVideoAdapter extends XMBaseAdapter<VODinfo> {



    public SearchVideoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchVedioHolder(parent, R.layout.item_recycler_search_vedio);
    }

    private class SearchVedioHolder extends BaseViewHolder<VODinfo> {
        ImageView iv_img;
        TextView tv_video_time;
        TextView tv_title;
        TextView play_count;

        SearchVedioHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iv_img=$(R.id.iv_img);
            tv_video_time=$(R.id.tv_video_time);
            tv_title=$(R.id.tv_title);
            play_count=$(R.id.play_count);
        }

        @Override
        public void setData(VODinfo data) {
            ImageLoader.load(mContext,data.getFrontcover(),iv_img);
            tv_video_time.setText(DateUtil.formatSecond(data.getDuration()*1000)+"");
            tv_title.setText(data.getTitle());
            play_count.setText(data.getPlaycount()+"次播放");

        }

    }
}
