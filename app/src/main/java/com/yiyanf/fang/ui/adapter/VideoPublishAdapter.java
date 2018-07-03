package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.SimpleVideo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.PixelUtil;

/**
 * Created by Administrator on 2018/3/22.
 */

public class VideoPublishAdapter extends XMBaseAdapter<SimpleVideo> {

    public VideoPublishAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoPublishHolder(parent, R.layout.item_recycler_video_publish);
    }

    private class VideoPublishHolder extends BaseViewHolder<SimpleVideo>{
        ImageView mFrontCover;
        TextView mVideoTitle;
       /* ImageView mUserAvatar;
        ImageView mVip;
        TextView mPublisher;
        TextView mCategory;*/

        public VideoPublishHolder(ViewGroup parent, int res) {
            super(parent, res);
            mFrontCover = $(R.id.iv_front_cover);
            mVideoTitle = $(R.id.tv_title);
           /* mUserAvatar = $(R.id.iv_avatar);
            mVip = $(R.id.iv_plus_v);
            mPublisher = $(R.id.tv_publisher_name);
            mCategory = $(R.id.tv_category);*/
        }

        @Override
        public void setData(SimpleVideo data) {
            //获取item宽度，计算图片等比例缩放后的高度，为imageView设置参数
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFrontCover.getLayoutParams();
            float itemWidth = (PixelUtil.getScreenWidth(mContext)) / 2;
            layoutParams.width = (int) itemWidth;
            float scale = (itemWidth+0f)/data.getCoverwidth();
            layoutParams.height= (int) (data.getCoverheight()*scale);
            mFrontCover.setLayoutParams(layoutParams);
            ImageLoader.load(mContext,data.getFrontcover(),mFrontCover);
            mVideoTitle.setText(data.getTitle());

        }
    }
}

