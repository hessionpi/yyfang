package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

/**
 * 视频封面
 *
 * Created by Hition on 2017/11/6.
 */

public class PickCoverAdapter extends XMBaseAdapter<Bitmap> {

    private int selectPosition = -1;

    public PickCoverAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoverHolder(parent,R.layout.layout_item_pick_cover);
    }

    private class CoverHolder extends BaseViewHolder<Bitmap> {
        ImageView mCover;

        CoverHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            mCover = $(R.id.iv_pick_cover);
        }

        @Override
        public void setData(Bitmap data) {
            final int position = PickCoverAdapter.this.getPosition(data);
            mCover.setTag(position);
            mCover.setImageBitmap(data);

            if (selectPosition == position) {
                mCover.setBackgroundResource(R.drawable.ic_cover_selected);
            } else {
                mCover.setBackgroundResource(0);
            }

            mCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionTag = (int) mCover.getTag();
                    if(selectPosition != positionTag){
                        selectPosition = positionTag;
                    }
                    notifyDataSetChanged();

                    PickCoverAdapter.super.mItemClickListener.onItemClick(position);
                }
            });

        }
    }

}
