package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
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
 * Created by Administrator on 2017/12/27.
 */

public class MineFavoriteAdapter extends XMBaseAdapter<VODinfo> {

    private OnFavoriteClickListener favoriteClickListener;





    public MineFavoriteAdapter(Context context) {
        super(context);
    }
    public void setFavoriteClickListener(OnFavoriteClickListener favoriteClickListener) {
        this.favoriteClickListener = favoriteClickListener;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineFavoriteHolder(parent, R.layout.item_recycler_mine_favorite);
    }

    private class MineFavoriteHolder extends BaseViewHolder<VODinfo> {
        ImageView iv_img;
        TextView tv_video_time;
        TextView tv_title;
        TextView play_count;
        ImageView iv_favorite;

        MineFavoriteHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iv_img=$(R.id.iv_img);
            tv_video_time=$(R.id.tv_video_time);
            tv_title=$(R.id.tv_title);
            play_count=$(R.id.play_count);
            iv_favorite=$(R.id.iv_favorite);
        }

        @Override
        public void setData(final VODinfo data) {
            ImageLoader.load(mContext,data.getFrontcover(),iv_img);
            tv_video_time.setText(DateUtil.formatSecond(data.getDuration()*1000));
            tv_title.setText(data.getTitle());
            play_count.setText(data.getPlaycount()+"次播放");
            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favoriteClickListener.onFavoriteClick(iv_favorite,data);
                }
            });
        }

    }
    public interface OnFavoriteClickListener {
        void onFavoriteClick(ImageView iv_favorite,VODinfo data);

    }
}

