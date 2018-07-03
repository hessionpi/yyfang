package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tencent.ugc.TXVideoEditer;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.SimpleVideo;
import com.yiyanf.fang.ui.adapter.PickVideoGridAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.SpacesItemDecoration;
import com.yiyanf.fang.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择视频
 * <p>
 * Created by Hition on 2017/11/1.
 */

public class PickVideoActivity extends BaseActivity {


    @Bind(R.id.iv_close)
    ImageView mClosePick;
    @Bind(R.id.rv_pick_vod)
    RecyclerView mRvPickVideo;


    private PickVideoGridAdapter videoGridAdapter;



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PickVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_video);
        setStatusBarColor();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        List<SimpleVideo> mVideoList = getVideoList();

        GridLayoutManager mGridManager=new GridLayoutManager(this,3);
        mRvPickVideo.setLayoutManager(mGridManager);
        SpacesItemDecoration decor = new SpacesItemDecoration(6, 3, false);
        mRvPickVideo.addItemDecoration(decor);
        videoGridAdapter = new PickVideoGridAdapter(this,mVideoList);
        mRvPickVideo.setAdapter(videoGridAdapter);

        videoGridAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SimpleVideo video = videoGridAdapter.getItem(position);
                if(null != video && video.getDuring() >= FangConstants.IMPORT_MIN_DURATION_SECONDS*1000){
                    Intent intent = new Intent(PickVideoActivity.this, TCVideoEditerActivity.class);
                    if (video == null) {
                        return;
                    }
                   TXVideoEditer mTXVideoEditer=new TXVideoEditer(PickVideoActivity.this);
                    int ret = mTXVideoEditer.setVideoPath(video.getPath());
                    if (ret < 0) {
                        ToastUtils.show(PickVideoActivity.this,"本机型暂不支持此视频格式");
                        //showUnSupportDialog("本机型暂不支持此视频格式");
                        return;
                    }
                    intent.putExtra(FangConstants.INTENT_KEY_SINGLE_CHOOSE,  video);
                    startActivity(intent);
                  // PublishVideoActivity.startActivity(PickVideoActivity.this,video.getPath(),"");
                    finish();
                }else{
                    ToastUtils.show(PickVideoActivity.this,"视频时长不能少于"+FangConstants.IMPORT_MIN_DURATION_SECONDS+"秒");
                }

            }
        });

        mClosePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * 读取系统视频相册
     *
     * @return
     */
    private List<SimpleVideo> getVideoList() {
        List<SimpleVideo> videoList = new ArrayList<>();
        //
        String str[] = { MediaStore.Video.Media._ID,MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, str, null,null, MediaStore.Video.Media.DATE_TAKEN + " desc");
        SimpleVideo video;
        while (cursor.moveToNext()) {
            video = new SimpleVideo();
            video.setPath(cursor.getString(2));
            video.setDuring(cursor.getLong(3));
            video.setSize(cursor.getLong(4));
            videoList.add(video);
        }
        cursor.close();
        return videoList;
    }


}
