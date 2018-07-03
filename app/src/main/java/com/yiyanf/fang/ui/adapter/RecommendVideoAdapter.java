package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.SimpleVideoInfo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.PixelUtil;

/**
 * 推荐视频适配器
 *
 * Created by Hition on 2018/3/6.
 */

public class RecommendVideoAdapter extends XMBaseAdapter<SimpleVideoInfo> {

    private OnPublisherClickListener publisherClickListener;
    private OnPublisherFailedListener onPublisherFailedListener;

    public RecommendVideoAdapter(Context context) {
        super(context);
    }

    public void setPublisherClickListener(OnPublisherClickListener publisherClickListener) {
        this.publisherClickListener = publisherClickListener;
    }

    public void setOnPublisherFailedListener(OnPublisherFailedListener onPublisherFailedListener) {
        this.onPublisherFailedListener = onPublisherFailedListener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendVideoHolder(parent, R.layout.item_recycler_video_recommend);
    }

    private class RecommendVideoHolder extends BaseViewHolder<SimpleVideoInfo>{
        ImageView mFrontCover;
        LinearLayout loadingLayout;
        LinearLayout uploadFailedLayout;
        TextView mUploadAgain;
        TextView mDelete;
        TextView loadingText;
        TextView mVideoTitle;
        ImageView mUserAvatar;
        TextView mPublisher;

        public RecommendVideoHolder(ViewGroup parent, int res) {
            super(parent, res);
            mFrontCover = $(R.id.iv_front_cover);
            loadingLayout = $(R.id.ll_uploading);
            uploadFailedLayout = $(R.id.ll_upload_failed);
            mUploadAgain = $(R.id.tv_upload_again);
            mDelete = $(R.id.tv_delete);
            loadingText = $(R.id.tv_upload_progress);
            mVideoTitle = $(R.id.tv_title);
            mUserAvatar = $(R.id.iv_avatar);
            mPublisher = $(R.id.tv_publisher_name);
        }

        @Override
        public void setData(SimpleVideoInfo data) {
            switch (data.getStatus()){
                case IDLE:
                    loadingLayout.setVisibility(View.GONE);
                    uploadFailedLayout.setVisibility(View.GONE);
                    break;

                case START:
                case UPLOADING:
                    uploadFailedLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);
                    loadingText.setText(String.valueOf(data.getProgress())+"%");
                    break;

                case FAILED:
                    loadingLayout.setVisibility(View.GONE);
                    uploadFailedLayout.setVisibility(View.VISIBLE);
                    break;

                default:

                    break;
            }

            //获取item宽度，计算图片等比例缩放后的高度，为imageView设置参数
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFrontCover.getLayoutParams();
            float itemWidth = (PixelUtil.getScreenWidth(mContext)) / 2;
            layoutParams.width = (int) itemWidth;
            float scale = (itemWidth+0f)/data.getCoverwidth();
            layoutParams.height= (int) (data.getCoverheight()*scale);
            mFrontCover.setLayoutParams(layoutParams);
            if(TextUtils.isEmpty(data.getFrontcover())){
                mFrontCover.setImageBitmap(data.getCoverImg());
            }else{
                ImageLoader.load(mContext,data.getFrontcover(),mFrontCover);
            }

            mVideoTitle.setText(data.getTitle());
            ImageLoader.loadTransformImage(mContext,data.getPublisheravatar(),R.drawable.icon_man,R.drawable.icon_man,mUserAvatar,0);
            mPublisher.setText(data.getPublishername());

            mUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publisherClickListener.onPublisherclick(data.getPublisherid());
                }
            });
            mPublisher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publisherClickListener.onPublisherclick(data.getPublisherid());
                }
            });

            mUploadAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onPublisherFailedListener && data.getStatus() == SimpleVideoInfo.Status.FAILED){
                        onPublisherFailedListener.onUploadAgain(data);
                    }
                }
            });
            int position = RecommendVideoAdapter.this.getPosition(data);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onPublisherFailedListener && data.getStatus() == SimpleVideoInfo.Status.FAILED){
                        onPublisherFailedListener.onDeleteFail(position,data.getVideoid());
                    }
                }
            });
        }
    }

    public interface OnPublisherClickListener{
        void onPublisherclick(String publisherId);
    }

    public interface OnPublisherFailedListener{
        void onUploadAgain(SimpleVideoInfo publisherId);

        void onDeleteFail(int position,long videoid);
    }

}
