package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.SimpleVideo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.PixelUtil;
import java.util.List;

/**
 * 选择视频适配器
 * Created by Hition on 2017/11/2.
 */

public class PickVideoGridAdapter extends XMBaseAdapter<SimpleVideo> {


    public PickVideoGridAdapter(Context context, List<SimpleVideo> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PickVideoHolder(parent, R.layout.layout_item_pick_video);
    }

    private class PickVideoHolder extends BaseViewHolder<SimpleVideo>{
        ImageView mCover;
        TextView mDuration;

        public PickVideoHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mCover = $(R.id.iv_video_cover);
            mDuration = $(R.id.tv_video_duration);
        }

        @Override
        public void setData(SimpleVideo data) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCover.getLayoutParams();
            float itemWidth = (PixelUtil.getScreenWidth(mContext)) / 3;
            layoutParams.width = (int) itemWidth;
            layoutParams.height= (int) itemWidth;
            mCover.setLayoutParams(layoutParams);
            ImageLoader.loadFromFile(mContext,data.getPath(),mCover);
            mDuration.setText(DateUtil.formatSecond(data.getDuring()));
        }
    }



}
