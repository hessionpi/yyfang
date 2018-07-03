package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.RegionInfo;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.PixelUtil;

/**
 * Created by Administrator on 2017/11/10.
 */

public class HotRegionAdapter extends XMBaseAdapter<RegionInfo>{
Context context;


        public HotRegionAdapter(Context context) {
            super(context);
            this.context=context;
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new HotRegionHolder(parent, R.layout.item_recycler_region);
        }

        private class HotRegionHolder extends BaseViewHolder<RegionInfo> {

           ImageView iv_building;
           TextView tv_area_name;
           TextView tv_price_range;
           TextView tv_distance;
           LinearLayout ll_building_tag;
           TextView tv_amount_building;
           TextView tv_amount_video;
           TextView tv_amount_live;
           TextView tv_unit;
            HotRegionHolder(ViewGroup parent, @LayoutRes int res) {
                super(parent, res);
                iv_building=$(R.id.iv_building);
                tv_area_name=$(R.id.tv_area_name);
                tv_price_range=$(R.id.tv_price_range);
                tv_distance=$(R.id.tv_distance);
                ll_building_tag=$(R.id.ll_building_tag);
                tv_amount_building=$(R.id.tv_amount_building);
                tv_amount_video=$(R.id.tv_amount_video);
                tv_amount_live=$(R.id.tv_amount_live);
                tv_unit=$(R.id.tv_unit);
            }

            @Override
            public void setData(RegionInfo data) {
                ImageLoader.load(mContext, data.getThumbnail(), 0,0, iv_building);
                tv_area_name.setText(data.getRegionName());
                tv_price_range.setText(data.getRegionPrice());
                tv_unit.setText(data.getRegionPriceUnit());
                tv_distance.setText(data.getDistance());
                if ( data.getRegionTag()!=null&&data.getRegionTag().size()>0) {
                    TypedArray colors = context.getResources().obtainTypedArray(R.array.tag_color);
                    TypedArray  drawables = context.getResources().obtainTypedArray (R.array.tag_drawable);
                    for (int i = 0; i < data.getRegionTag().size(); i++) {
                        TextView tagView = new TextView(mContext);
                        LinearLayout.LayoutParams tagLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tagLP.setMargins(0, 0, PixelUtil.dp2px(5), 0);
                        tagView.setLayoutParams(tagLP);
                        tagView.setTextColor(colors.getColor(i%6,0));
                        tagView.setTextSize(9);
                        tagView.setText(data.getRegionTag().get(i));
                        tagView.setBackground(drawables.getDrawable(i%6));
                        ll_building_tag.addView(tagView);
                    }
                }
                tv_amount_building.setText("楼盘："+data.getRegionbuilding());
                tv_amount_video.setText("视频："+data.getRegionVideo());
                tv_amount_live.setText("直播："+data.getRegionLive());
            }

        }
    }

