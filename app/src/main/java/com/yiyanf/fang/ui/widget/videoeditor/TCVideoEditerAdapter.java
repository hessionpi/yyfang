package com.yiyanf.fang.ui.widget.videoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by yuejiaoli on 2017/4/30.
 */
public class TCVideoEditerAdapter extends RecyclerView.Adapter<TCVideoEditerAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<Bitmap> data = new ArrayList<Bitmap>();
    long mVideoDuration;

    public TCVideoEditerAdapter(Context context, long videoDuration) {
        mContext = context;
        mVideoDuration = videoDuration;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     /*   int itemCount = 0;
        if (mVideoDuration <= 180000) {
            itemCount = 10;
        } else {
            itemCount= (int) (mVideoDuration/18000);
        }*/
        int padding = 16;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        final int itemWidth = (screenWidth - 2 * padding) / 10;
        int height = 150;
        ImageView view = new ImageView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, height));
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new TCVideoEditerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.thumb.setImageBitmap(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(int position, Bitmap b) {
        data.add(b);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView;
        }
    }

    public void addAll(ArrayList<Bitmap> bitmap) {
        recycleAllBitmap();

        data.addAll(bitmap);
        notifyDataSetChanged();
    }

    public void recycleAllBitmap() {
        for (Bitmap b : data) {
            if (!b.isRecycled())
                b.recycle();
        }
        data.clear();
        notifyDataSetChanged();
    }
}
