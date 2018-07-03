package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.Video;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.LeftSlideView;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.PixelUtil;

import java.util.List;

/**
 * 版权：壹眼房 版权所有
 *
 * 作者：hition
 *
 * 创建时间：2018/1/11
 *
 * 功能描述：发布视频失败适配器展示
 *
 * 修订记录：
 */

public class FailVideoAdapter extends XMBaseAdapter<Video> implements LeftSlideView.IonSlidingButtonListener {

    private IonSlidingViewClickListener mSlidingMenuClickListener;
    private LeftSlideView mMenu = null;

    public FailVideoAdapter(Context context, List<Video> objects) {
        super(context, objects);
    }


    public void setSlidingMenuClickListener(IonSlidingViewClickListener mSlidingMenuClickListener) {
        this.mSlidingMenuClickListener = mSlidingMenuClickListener;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FailedHolder(parent, R.layout.layout_reupload_video_item);
    }

    private class FailedHolder extends BaseViewHolder<Video>{

        LeftSlideView mSlideMenu;
        ViewGroup layout_content;
        ImageView mCover;
        TextView mDuration;
        TextView mVideoTitle;
        TextView mVideoStatus;

        TextView mReuploadView;
        TextView mDeleteView;

        public FailedHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mSlideMenu = $(R.id.slide_menu);
            layout_content = $(R.id.layout_content);
            mCover = $(R.id.iv_img);
            mDuration = $(R.id.tv_video_duration);
            mVideoTitle = $(R.id.tv_title);
            mVideoStatus = $(R.id.video_status);
            mReuploadView = $(R.id.tv_upload_again);
            mDeleteView = $(R.id.tv_delete);

        }

        @Override
        public void setData(final Video data) {
            //设置内容布局的宽为屏幕宽度
            layout_content.getLayoutParams().width = PixelUtil.getScreenWidth(mContext);

            Bitmap coverBitmap = BitmapFactory.decodeFile(data.getCoverPath());
            if(null != coverBitmap){
                mCover.setImageBitmap(coverBitmap);
            }
//            mDuration.setText(DateUtil.formatSecond(data.getDuration()));
            mVideoTitle.setText(data.getTitle());



            mSlideMenu.setSlidingButtonListener(FailVideoAdapter.this);

            final int position = FailVideoAdapter.this.getPosition(data);
            //item正文点击事件
            layout_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        if(null != mItemClickListener){
                            mItemClickListener.onItemClick(position);
                        }
                    }
                }
            });


            //左滑设置点击事件
            mReuploadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSlidingMenuClickListener.onReuploadBtnCilck(data, position);
                    mVideoStatus.setText("上传中");
                    closeMenu();
                }
            });


            //左滑删除点击事件
            mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSlidingMenuClickListener.onDeleteBtnCilck(data.getVideoid(), position);
                }
            });
        }
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }

    /**
     * 滑动或者点击了Item监听
     **/
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断菜单是否打开
     **/
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    /**
     * 注册接口的方法：点击事件。在Mactivity.java实现这些方法。
     */
    public interface IonSlidingViewClickListener {
        void onDeleteBtnCilck(long videoId, int position);//点击“删除”

        void onReuploadBtnCilck(Video video, int position);//点击“重新上传”
    }

}
